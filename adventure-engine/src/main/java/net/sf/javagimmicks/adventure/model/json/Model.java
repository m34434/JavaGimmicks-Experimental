package net.sf.javagimmicks.adventure.model.json;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Model extends CustomPropertiesHolder
{
   private List<Node> nodes;

   private Map<String, Object> state;

   public List<Node> getNodes()
   {
      if (nodes == null)
      {
         nodes = new ArrayList<>();
      }

      return nodes;
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
