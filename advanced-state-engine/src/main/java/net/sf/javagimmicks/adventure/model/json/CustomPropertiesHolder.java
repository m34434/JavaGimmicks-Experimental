package net.sf.javagimmicks.adventure.model.json;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class CustomPropertiesHolder
{
   private Map<String, Object> customProperties;

   public Map<String, Object> getCustomProperties()
   {
      if (customProperties == null)
      {
         customProperties = new LinkedHashMap<>();
      }

      return customProperties;
   }
}
