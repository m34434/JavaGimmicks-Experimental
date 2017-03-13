package net.sf.javagimmicks.ase.config;

import java.util.ArrayList;
import java.util.List;

public class ActionConfig extends CustomPropertiesHolder implements DependencyConfig.Container
{
   private String name;

   private List<DependencyConfig> dependencies;

   private String stateKey;
   private Object newValue;
   private String newNode;

   private String script;

   public ActionConfig(String name, String stateKey, Object newValue, String newNode)
   {
      this.name = name;
      this.stateKey = stateKey;
      this.newValue = newValue;
      this.newNode = newNode;
   }

   public ActionConfig(String name, String stateKey, Object newValue)
   {
      this(name, stateKey, newValue, null);
   }

   public ActionConfig(String name, String script, String newNode)
   {
      this.name = name;
      this.newNode = newNode;
      this.script = script;
   }

   public ActionConfig(String name, String script)
   {
      this(name, script, null);
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

   public Object getNewValue()
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