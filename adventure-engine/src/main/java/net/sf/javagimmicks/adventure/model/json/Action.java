package net.sf.javagimmicks.adventure.model.json;

import java.util.ArrayList;
import java.util.List;

public class Action extends CustomPropertiesHolder
{
   private String name;

   private List<Dependency> dependencies;

   private String stateKey;
   private String newValue;
   private String newNode;

   private String script;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<Dependency> getDependencies()
   {
      if (dependencies == null)
      {
         dependencies = new ArrayList<>();
      }

      return dependencies;
   }

   public void setDependencies(List<Dependency> dependencies)
   {
      this.dependencies = dependencies;
   }

   public String getStateKey()
   {
      return stateKey;
   }

   public void setStateKey(String stateKey)
   {
      this.stateKey = stateKey;
   }

   public String getNewValue()
   {
      return newValue;
   }

   public void setNewValue(String newValue)
   {
      this.newValue = newValue;
   }

   public String getScript()
   {
      return script;
   }

   public void setScript(String script)
   {
      this.script = script;
   }

   public String getNewNode()
   {
      return newNode;
   }

   public void setNewNode(String newNode)
   {
      this.newNode = newNode;
   }

   public static class Dependency extends CustomPropertiesHolder
   {
      private String stateKey;
      private String value;

      private String script;

      public String getStateKey()
      {
         return stateKey;
      }

      public void setStateKey(String stateKey)
      {
         this.stateKey = stateKey;
      }

      public String getValue()
      {
         return value;
      }

      public void setValue(String value)
      {
         this.value = value;
      }

      public String getScript()
      {
         return script;
      }

      public void setScript(String script)
      {
         this.script = script;
      }

   }

}