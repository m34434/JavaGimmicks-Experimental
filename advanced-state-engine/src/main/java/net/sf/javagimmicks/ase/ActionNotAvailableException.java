package net.sf.javagimmicks.ase;

import static java.lang.String.format;

public class ActionNotAvailableException extends Exception
{
   private static final long serialVersionUID = 1L;

   private final String actionName;
   private final boolean dependenciesFailed;

   public ActionNotAvailableException(String actionName, boolean depdenciesFailed)
   {
      super(buildMessage(actionName, depdenciesFailed));

      this.actionName = actionName;
      this.dependenciesFailed = depdenciesFailed;
   }

   public String getActionName()
   {
      return actionName;
   }

   public boolean isDependenciesFailed()
   {
      return dependenciesFailed;
   }

   private static String buildMessage(String actionName, boolean depdenciesFailed)
   {
      return depdenciesFailed ? format("Action '%s' not available due to unsatisfied dependencies!", actionName)
            : format("Action '%s' not found in current node or global scope!", actionName);
   }

}
