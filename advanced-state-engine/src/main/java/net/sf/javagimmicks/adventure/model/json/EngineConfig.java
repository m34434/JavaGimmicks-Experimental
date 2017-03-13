package net.sf.javagimmicks.adventure.model.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EngineConfig extends CustomPropertiesHolder
{
   private String startNode;

   private List<NodeConfig> nodes;

   private List<ActionConfig> actions;

   private Map<String, Object> state;

   public EngineConfig(String startNode)
   {
      this.startNode = startNode;
   }

   EngineConfig()
   {}

   public String getStartNode()
   {
      return startNode;
   }

   public List<NodeConfig> getNodes()
   {
      if (nodes == null)
      {
         nodes = new ArrayList<>();
      }

      return nodes;
   }

   public List<ActionConfig> getActions()
   {
      if (actions == null)
      {
         actions = new ArrayList<>();
      }

      return actions;
   }

   public Map<String, Object> getState()
   {
      if (state == null)
      {
         state = new LinkedHashMap<>();
      }

      return state;
   }

}
