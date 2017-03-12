package net.sf.javagimmicks.adventure;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import net.sf.javagimmicks.adventure.model.json.ActionConfig;
import net.sf.javagimmicks.adventure.model.json.EngineConfig;
import net.sf.javagimmicks.adventure.model.json.EngineState;
import net.sf.javagimmicks.adventure.model.json.NodeConfig;
import net.sf.javagimmicks.adventure.model.json.NodeConfig.TransitionConfig;

public class Engine
{
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
               .forEach(a -> nodeActions.computeIfAbsent(a.getName(), k -> new HashMap<>()).put(a.getName(), a));
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

   public void performAction(String actionName)
   {
      performAction(searchAction(actionName));
   }

   private void performAction(Optional<ActionConfig> optionalSearchAction)
   {
      final ActionConfig action = optionalSearchAction.orElseThrow(IllegalStateException::new);

      final String script = action.getScript() != null ? action.getScript()
            : String.format("%s = %s", action.getStateKey(), action.getNewValue());

      final Bindings b = js.createBindings();
      b.putAll(state.getState());
      try
      {
         js.eval(script, b);

         final String newNodeFromScript = (String) b.remove("node");
         if (newNodeFromScript != null)
         {
            state.setNode(newNodeFromScript);
         }
         else if (action.getNewNode() != null)
         {
            state.setNode(action.getNewNode());
         }

         // state.getState().clear();
         state.getState().putAll(b);
      }
      catch (ScriptException e)
      {
         throw new RuntimeException(e);
      }
   }

   private Optional<ActionConfig> searchAction(String actionName)
   {
      Objects.requireNonNull(actionName, "Action name not specified!");

      if (state.getNode() != null)
      {
         final Map<String, ActionConfig> nodeActions = this.nodeActions.get(state.getNode());

         if (nodeActions != null)
         {
            final ActionConfig actionConfig = nodeActions.get(actionName);
            if (actionConfig != null)
            {
               return Optional.of(actionConfig);
            }
         }
      }

      return Optional.ofNullable(globalActions.get(actionName));
   }

}
