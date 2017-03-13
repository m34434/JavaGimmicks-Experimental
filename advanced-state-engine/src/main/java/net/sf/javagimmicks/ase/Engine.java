package net.sf.javagimmicks.ase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sf.javagimmicks.ase.config.ActionConfig;
import net.sf.javagimmicks.ase.config.DependencyConfig;
import net.sf.javagimmicks.ase.config.EngineConfig;
import net.sf.javagimmicks.ase.config.EngineState;
import net.sf.javagimmicks.ase.config.NodeConfig;
import net.sf.javagimmicks.ase.config.NodeConfig.TransitionConfig;

public class Engine
{
   public static final String VAR_NODE = "___node___";
   public static final String VAR_REF_VALUE = "___ref___value___";

   private static final ScriptEngine js = new ScriptEngineManager().getEngineByExtension("js");

   private final EngineState state;
   private final EngineConfig config;

   private final Map<String, ActionConfig> globalActions = new HashMap<>();

   private final Map<String, NodeConfig> nodes = new HashMap<>();
   private final Map<String, Map<String, TransitionConfig>> nodeTransitions = new HashMap<>();
   private final Map<String, Map<String, ActionConfig>> nodeActions = new HashMap<>();

   public Engine(EngineConfig config, Optional<EngineState> state)
   {
      Objects.requireNonNull(config, "Configuration must not be null!");

      this.config = config;

      config.getActions().forEach(a -> globalActions.put(a.getName(), a));
      config.getNodes().forEach(n -> {
         nodes.put(n.getName(), n);
         n.getTransitions()
               .forEach(t -> nodeTransitions.computeIfAbsent(n.getName(), k -> new HashMap<>()).put(t.getName(), t));
         n.getActions()
               .forEach(a -> nodeActions.computeIfAbsent(n.getName(), k -> new HashMap<>()).put(a.getName(), a));
      });

      this.state = state.orElseGet(() -> {
         final EngineState s = new EngineState(config.getStartNode());
         s.getState().putAll(config.getState());

         return s;
      });
   }

   public Engine(EngineConfig config)
   {
      this(config, Optional.empty());
   }

   public Engine(String startNodeName)
   {
      final NodeConfig startNode = new NodeConfig(startNodeName);
      nodes.put(startNodeName, startNode);

      this.config = new EngineConfig(startNodeName);
      config.getNodes().add(startNode);

      state = new EngineState(startNodeName);
   }

   public EngineState getState()
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

   private void performAction(ActionConfig action) throws ActionScriptFailedException
   {
      final String script = action.getScript() != null ? action.getScript()
            : String.format("%s = %s", action.getStateKey(), VAR_REF_VALUE);

      final Bindings b = js.createBindings();
      b.putAll(state.getState());
      b.put(VAR_NODE, state.getNode());
      b.put(VAR_REF_VALUE, action.getNewValue());
      try
      {
         js.eval(script, b);

         final String newNodeFromScript = (String) b.remove(VAR_NODE);
         if (newNodeFromScript != null && !newNodeFromScript.equals(state.getNode()))
         {
            state.setNode(newNodeFromScript);
         }
         else if (action.getNewNode() != null)
         {
            state.setNode(action.getNewNode());
         }

         b.remove(VAR_REF_VALUE);

         // state.getState().clear();
         state.getState().putAll(b);
      }
      catch (Exception e)
      {
         throw new ActionScriptFailedException(action.getName(), script, e);
      }
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

   private boolean checkDependencies(ActionConfig actionConfig)
   {
      return actionConfig.getDependencies().stream().allMatch(d -> checkDependency(actionConfig, d));
   }

   private boolean checkDependency(ActionConfig a, DependencyConfig d)
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
}
