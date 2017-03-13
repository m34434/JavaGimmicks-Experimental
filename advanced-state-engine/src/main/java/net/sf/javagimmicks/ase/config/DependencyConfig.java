package net.sf.javagimmicks.ase.config;

import java.util.List;

public class DependencyConfig extends CustomPropertiesHolder
{
   private String stateKey;
   private Object value;

   private String script;

   public DependencyConfig(String stateKey, Object value)
   {
      this.stateKey = stateKey;
      this.value = value;
   }

   public DependencyConfig(String script)
   {
      this.script = script;
   }

   DependencyConfig()
   {}

   public String getStateKey()
   {
      return stateKey;
   }

   public Object getValue()
   {
      return value;
   }

   public String getScript()
   {
      return script;
   }

   public static interface Container
   {
      List<DependencyConfig> getDependencies();
   }
}