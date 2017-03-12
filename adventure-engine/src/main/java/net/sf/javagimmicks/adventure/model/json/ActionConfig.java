package net.sf.javagimmicks.adventure.model.json;

import java.util.ArrayList;
import java.util.List;

public class ActionConfig extends CustomPropertiesHolder
{
   private String name;

   private List<DependencyConfig> dependencies;

   private String stateKey;
   private String newValue;
   private String newNode;

   private String script;

   public ActionConfig(String name, String stateKey, String newValue, String newNode)
   {
      this.name = name;
      this.stateKey = stateKey;
      this.newValue = newValue;
      this.newNode = newNode;
   }

   public ActionConfig(String name, String script, String newNode)
   {
      this.name = name;
      this.newNode = newNode;
      this.script = script;
   }

   ActionConfig()
   {}

   public String getName()
   {
      return name;
   }

   public List<DependencyConfig> getDependencies()
   {
      if (dependencies == null)
      {
         dependencies = new ArrayList<>();
      }

      return dependencies;
   }

   public String getStateKey()
   {
      return stateKey;
   }

   public String getNewValue()
   {
      return newValue;
   }

   public String getScript()
   {
      return script;
   }

   public String getNewNode()
   {
      return newNode;
   }

}