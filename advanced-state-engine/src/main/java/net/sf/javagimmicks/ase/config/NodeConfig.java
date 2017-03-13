package net.sf.javagimmicks.ase.config;

import java.util.ArrayList;
import java.util.List;

public class NodeConfig extends CustomPropertiesHolder
{
   private String name;

   private List<TransitionConfig> transitions;

   private List<ActionConfig> actions;

   public NodeConfig(String name)
   {
      this.name = name;
   }

   NodeConfig()
   {}

   public String getName()
   {
      return name;
   }

   public List<TransitionConfig> getTransitions()
   {
      if (transitions == null)
      {
         transitions = new ArrayList<>();
      }

      return transitions;
   }

   public List<ActionConfig> getActions()
   {
      if (actions == null)
      {
         actions = new ArrayList<>();
      }

      return actions;
   }

   public static class TransitionConfig extends CustomPropertiesHolder implements DependencyConfig.Container
   {
      private String target;

      private List<DependencyConfig> dependencies;

      public TransitionConfig(String target)
      {
         this.target = target;
      }

      TransitionConfig()
      {}

      public String getTarget()
      {
         return target;
      }

      public List<DependencyConfig> getDependencies()
      {
         if (dependencies == null)
         {
            dependencies = new ArrayList<>();
         }

         return dependencies;
      }

   }
}