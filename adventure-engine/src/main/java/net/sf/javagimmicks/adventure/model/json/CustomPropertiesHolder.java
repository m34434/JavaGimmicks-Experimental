package net.sf.javagimmicks.adventure.model.json;

import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
abstract class CustomPropertiesHolder
{
   @JsonProperty(required = false)
   private Map<String, Object> customProperties;

   public Map<String, Object> getCustomProperties()
   {
      if (customProperties == null)
      {
         customProperties = new LinkedHashMap<>();
      }

      return customProperties;
   }

   public void setCustomProperties(Map<String, Object> customProperties)
   {
      this.customProperties = customProperties;
   }
}
