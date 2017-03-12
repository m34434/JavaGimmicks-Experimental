package net.sf.javagimmicks.adventure.model.json;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonCreator.Mode;

public class Node extends CustomPropertiesHolder
{
   private String name;

   private List<NodeTransition> transitions;

   @JsonCreator(mode = Mode.PROPERTIES)
   public Node(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<NodeTransition> getTransitions()
   {
      if (transitions == null)
      {
         transitions = new ArrayList<>();
      }

      return transitions;
   }

   public void setTransitions(List<NodeTransition> transitions)
   {
      this.transitions = transitions;
   }

   public NodeTransition connectTo(Node other)
   {
      final NodeTransition result = new NodeTransition(other.name);
      getTransitions().add(result);

      return result;
   }

   public static class NodeTransition extends CustomPropertiesHolder
   {
      private String target;

      @JsonCreator(mode = Mode.PROPERTIES)
      public NodeTransition(String target)
      {
         this.target = target;
      }

      public String getTarget()
      {
         return target;
      }
   }
}