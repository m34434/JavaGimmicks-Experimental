package net.sf.javagimmicks.ase;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.javagimmicks.ase.config.ActionConfig;
import net.sf.javagimmicks.ase.config.DependencyConfig;
import net.sf.javagimmicks.ase.config.EngineConfig;
import net.sf.javagimmicks.ase.config.NodeConfig;
import net.sf.javagimmicks.ase.config.NodeConfig.TransitionConfig;

public class Engine
{
   public static final String VAR_NODE = "___node___";
   public static final String VAR_REF_VALUE = "___ref___value___";

   private static final ScriptEngine js = new ScriptEngineManager().getEngineByExtension("js");

   private final State state;
   private final EngineConfig config;

   private final Map<String, ActionConfig> globalActions = new HashMap<>();

   private final Map<String, NodeConfig> nodes = new HashMap<>();
   private final Map<String, Map<String, TransitionConfig>> nodeTransitions = new HashMap<>();
   private final Map<String, Map<String, ActionConfig>> nodeActions = new HashMap<>();

   public Engine(EngineConfig config, State state)
   {
      Objects.requireNonNull(config, "Configuration must not be null!");

      this.config = config;

      config.getActions().forEach(a -> globalActions.put(a.getName(), a));
      config.getNodes().forEach(n -> {
         nodes.put(n.getName(), n);
         n.getTransitions()
               .forEach(t -> nodeTransitions.computeIfAbsent(n.getName(), k -> new HashMap<>()).put(t.getTarget(), t));
         n.getActions()
               .forEach(a -> nodeActions.computeIfAbsent(n.getName(), k -> new HashMap<>()).put(a.getName(), a));
      });

      if (state == null)
      {
         state = new State(config.getStartNode());
         state.getState().putAll(config.getState());
      }

      this.state = state;
   }

   public Engine(EngineConfig config)
   {
      this(config, null);
   }

   public Engine(String startNodeName)
   {
      final NodeConfig startNode = new NodeConfig(startNodeName);
      nodes.put(startNodeName, startNode);

      this.config = new EngineConfig(startNodeName);
      config.getNodes().add(startNode);

      state = new State(startNodeName);
   }

   public State getState()
   {
      return state;
   }

   public EngineConfig getConfig()
   {
      return config;
   }

   public NodeConfig getCurrentNode()
   {
      return nodes.get(state.getNode());
   }

   public Object getState(String expression) throws ScriptException
   {
      final Bindings b = js.createBindings();
      b.putAll(state.getState());

      return js.eval(expression, b);
   }

   public void performAction(String actionName) throws ActionNotAvailableException, ActionScriptFailedException
   {
      performAction(searchAction(actionName));
   }

   public void takeTransition(String targetNodeName) throws TransitionNotAvailableException
   {
      performTransition(searchTransition(targetNodeName));
   }

   public Set<String> getAvailableActions()
   {
      final Set<String> result = new HashSet<>();

      if (state.getNode() != null)
      {
         final Map<String, ActionConfig> nodeActions = this.nodeActions.get(state.getNode());

         if (nodeActions != null)
         {
            nodeActions.values().stream().filter(this::checkDependencies).map(ActionConfig::getName)
                  .forEach(result::add);
         }
      }

      globalActions.entrySet().stream().filter(e -> !result.contains(e.getKey()))
            .filter(e -> checkDependencies(e.getValue())).map(Entry::getKey).forEach(result::add);

      return result;
   }

   public Set<String> getAvailableTransitions()
   {
      final Set<String> result = new HashSet<>();

      if (state.getNode() != null)
      {
         final Map<String, TransitionConfig> nodeTransitions = this.nodeTransitions.get(state.getNode());

         if (nodeTransitions != null)
         {
            nodeTransitions.values().stream().filter(this::checkDependencies).map(TransitionConfig::getTarget)
                  .forEach(result::add);
         }
      }

      return result;
   }

   private ActionConfig searchAction(String actionName) throws ActionNotAvailableException
   {
      Objects.requireNonNull(actionName, "Action name not specified!");

      ActionConfig actionConfig = null;

      if (state.getNode() != null)
      {
         final Map<String, ActionConfig> nodeActions = this.nodeActions.get(state.getNode());

         if (nodeActions != null)
         {
            actionConfig = nodeActions.get(actionName);
         }
      }

      if (actionConfig == null)
      {
         actionConfig = globalActions.get(actionName);
      }

      if (actionConfig == null)
      {
         throw new ActionNotAvailableException(actionName, false);
      }

      if (!checkDependencies(actionConfig))
      {
         throw new ActionNotAvailableException(actionName, true);
      }

      return actionConfig;
   }

   private void performAction(ActionConfig action) throws ActionScriptFailedException
   {
      final Bindings b = js.createBindings();
      b.putAll(state.getState());

      final boolean customScript = action.getScript() != null;
      final String script;

      if (customScript)
      {
         script = action.getScript();
         b.put(VAR_NODE, action.getNewNode());
      }
      else
      {
         script = String.format("%s = %s;", action.getStateKey(), VAR_REF_VALUE);
         b.put(VAR_REF_VALUE, action.getNewValue());
      }

      try
      {
         js.eval(script, b);

         if (customScript)
         {
            final String newNodeFromScript = (String) b.remove(VAR_NODE);
            if (newNodeFromScript != null)
            {
               state.setNode(newNodeFromScript);
            }
         }
         else
         {
            b.remove(VAR_REF_VALUE);

            final String newNodeFromAction = action.getNewNode();
            if (newNodeFromAction != null)
            {
               state.setNode(newNodeFromAction);
            }
         }

         state.getState().putAll(b);
      }
      catch (Exception e)
      {
         throw new ActionScriptFailedException(action.getName(), script, e);
      }
   }

   private TransitionConfig searchTransition(String targetNodeName) throws TransitionNotAvailableException
   {
      Objects.requireNonNull(targetNodeName, "Target node name not specified!");

      if (state.getNode() == null)
      {
         throw new IllegalStateException("Current node is unkown! Check the state configuration!");
      }

      final Map<String, TransitionConfig> transitions = nodeTransitions.get(state.getNode());
      if (transitions == null || !transitions.containsKey(targetNodeName))
      {
         throw new TransitionNotAvailableException(targetNodeName, false);
      }

      final TransitionConfig result = transitions.get(targetNodeName);
      if (!checkDependencies(result))
      {
         throw new TransitionNotAvailableException(targetNodeName, true);
      }

      return result;
   }

   private void performTransition(TransitionConfig transition)
   {
      state.setNode(transition.getTarget());
   }

   private boolean checkDependencies(DependencyConfig.Container c)
   {
      return c.getDependencies().stream().allMatch(d -> checkDependency(c, d));
   }

   private boolean checkDependency(DependencyConfig.Container c, DependencyConfig d)
   {
      final String script = d.getScript() != null ? d.getScript()
            : String.format("%s == %s", d.getStateKey(), VAR_REF_VALUE);

      final Bindings b = js.createBindings();
      b.putAll(state.getState());
      b.put(VAR_NODE, state.getNode());
      b.put(VAR_REF_VALUE, d.getValue());

      try
      {
         return (Boolean) js.eval(script, b);
      }
      catch (Exception e)
      {
         return false;
      }
   }

   public static class State
   {
      private String node;

      private Map<String, Object> state;

      private State(String node)
      {
         Objects.requireNonNull("Start node may not be null!", node);
         this.node = node;
      }

      State()
      {}

      public String getNode()
      {
         return node;
      }

      public void setNode(String node)
      {
         this.node = node;
      }

      public Map<String, Object> getState()
      {
         if (state == null)
         {
            state = new LinkedHashMap<>();
         }

         return state;
      }

      public String asJson(boolean prettyPrint) throws JsonProcessingException
      {
         if (prettyPrint)
         {
            return new ObjectMapper().writerWithDefaultPrettyPrinter()
                  .writeValueAsString(this);
         }
         else
         {
            return new ObjectMapper().writeValueAsString(this);
         }
      }

      public String asJson() throws JsonProcessingException
      {
         return asJson(false);
      }

      public static State fromJson(InputStream is) throws JsonParseException, JsonMappingException, IOException
      {
         return new ObjectMapper().readValue(is, State.class);
      }

      public static State fromJson(Reader r) throws JsonParseException, JsonMappingException, IOException
      {
         return new ObjectMapper().readValue(r, State.class);
      }

      public static State fromJson(String s) throws JsonParseException, JsonMappingException, IOException
      {
         return new ObjectMapper().readValue(s, State.class);
      }

   }
}
