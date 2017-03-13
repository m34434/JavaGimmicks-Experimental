package net.sf.javagimmicks.ase.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EngineConfig extends CustomPropertiesHolder
{
   private String startNode;

   private List<NodeConfig> nodes;

   private List<ActionConfig> actions;

   private Map<String, Object> state;

   public EngineConfig(String startNode)
   {
      this.startNode = startNode;
   }

   EngineConfig()
   {}

   public String getStartNode()
   {
      return startNode;
   }

   public List<NodeConfig> getNodes()
   {
      if (nodes == null)
      {
         nodes = new ArrayList<>();
      }

      return nodes;
   }

   public List<ActionConfig> getActions()
   {
      if (actions == null)
      {
         actions = new ArrayList<>();
      }

      return actions;
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

   public static EngineConfig fromJson(InputStream is) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(is, EngineConfig.class);
   }

   public static EngineConfig fromJson(Reader r) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(r, EngineConfig.class);
   }

   public static EngineConfig fromJson(String s) throws JsonParseException, JsonMappingException, IOException
   {
      return new ObjectMapper().readValue(s, EngineConfig.class);
   }

}
