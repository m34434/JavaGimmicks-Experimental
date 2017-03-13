package net.sf.javagimmicks.ase.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineState
{
   private String node;

   private Map<String, Object> state;

   public EngineState(String node)
   {
      this.node = node;
   }

   EngineState()
   {}

   public String getNode()
   {
      return node;
   }

   public void setNode(String node)
   {
      this.node = node;
   }

   public Map<String, Object> getState()
   {
      if (state == null)
      {
         state = new LinkedHashMap<>();
      }

      return state;
   }

   public String asJson(boolean prettyPrint) throws JsonProcessingException
   {
      if (prettyPrint)
      {
         return new ObjectMapper().writerWithDefaultPrettyPrinter()
               .writeValueAsString(this);
      }
      else
      {
         return new ObjectMapper().writeValueAsString(this);
      }
   }

   public String asJson() throws JsonProcessingException
   {
      return asJson(false);
   }

   public static EngineState fromJson(InputStream is) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(is, EngineState.class);
   }

   public static EngineState fromJson(Reader r) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(r, EngineState.class);
   }

   public static EngineState fromJson(String s) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(s, EngineState.class);
   }

}
