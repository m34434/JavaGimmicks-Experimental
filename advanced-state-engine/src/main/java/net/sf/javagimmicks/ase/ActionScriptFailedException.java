package net.sf.javagimmicks.ase;

import static java.lang.String.format;

public class ActionScriptFailedException extends Exception
{
   private static final long serialVersionUID = 1L;

   private final String actionName;
   private final String script;

   public ActionScriptFailedException(String actionName, String script, Exception cause)
   {
      super(format("Failed to execute action '%s'! Script execution caused an exception!", actionName), cause);

      this.actionName = actionName;
      this.script = script;
   }

   public String getActionName()
   {
      return actionName;
   }

   public String getScript()
   {
      return script;
   }

}
