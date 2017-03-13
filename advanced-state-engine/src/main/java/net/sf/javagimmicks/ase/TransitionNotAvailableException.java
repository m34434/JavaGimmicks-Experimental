package net.sf.javagimmicks.ase;

import static java.lang.String.format;

public class TransitionNotAvailableException extends Exception
{
   private static final long serialVersionUID = 1L;

   private final String targetNodeName;
   private final boolean dependenciesFailed;

   public TransitionNotAvailableException(String targetNodeName, boolean depdenciesFailed)
   {
      super(buildMessage(targetNodeName, depdenciesFailed));

      this.targetNodeName = targetNodeName;
      this.dependenciesFailed = depdenciesFailed;
   }

   public String getTargetNodeName()
   {
      return targetNodeName;
   }

   public boolean isDependenciesFailed()
   {
      return dependenciesFailed;
   }

   private static String buildMessage(String targetNodeName, boolean depdenciesFailed)
   {
      return depdenciesFailed
            ? format("Transition to node '%s' not available due to unsatisfied dependencies!", targetNodeName)
            : format("Transition to node '%s' not found in current node!", targetNodeName);
   }

}
