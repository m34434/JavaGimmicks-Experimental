package net.sf.javagimmicks.adventure.model;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DatabindTest
{
   @Test
   public void testSerilize() throws JsonProcessingException
   {
      final Game g = new Game();

      final Location a = g.addLocation("a");
      final Location b = g.addLocation("b");
      final Location c = g.addLocation("c");
      
      a.connectTo(b);
      b.connectTo(c);
      c.connectTo(a);
      a.connectTo(c);
      
      final String gameString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(g);
      
      System.out.println(gameString);
   }
   
   @Test
   public void testParse() throws IOException
   {
      final Game g = new ObjectMapper().readValue(getClass().getResourceAsStream("databind-1.json"), Game.class);
      
      System.out.println(g);
   }
}
