package net.sf.javagimmicks.adventure.model.json;

public class DependencyConfig extends CustomPropertiesHolder
{
   private String stateKey;
   private String value;

   private String script;

   public DependencyConfig(String stateKey, String value)
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

   public String getValue()
   {
      return value;
   }

   public String getScript()
   {
      return script;
   }

}