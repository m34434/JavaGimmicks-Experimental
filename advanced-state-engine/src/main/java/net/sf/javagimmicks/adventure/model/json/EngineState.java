package net.sf.javagimmicks.adventure.model.json;

import java.util.LinkedHashMap;
import java.util.Map;

public class EngineState
{
   private String node;

   private Map<String, Object> state;

   public EngineState(String node)
   {
      this.node = node;
   }

   EngineState()
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

}
