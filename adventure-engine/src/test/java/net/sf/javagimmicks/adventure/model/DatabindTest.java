package net.sf.javagimmicks.adventure.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.javagimmicks.adventure.model.json.Model;
import net.sf.javagimmicks.adventure.model.json.Node;

public class DatabindTest
{
   @Test
   public void testSerilize() throws JsonProcessingException
   {
      final Model g = new Model();

      final Node a = new Node("a");
      final Node b = new Node("b");
      final Node c = new Node("c");

      g.getNodes().add(a);
      g.getNodes().add(b);
      g.getNodes().add(c);

      a.connectTo(b);
      b.connectTo(c);
      c.connectTo(a);
      a.connectTo(c);

      g.getState().put("foo", "bar");
      g.getState().put("num", 1000L);

      final Map<String, Object> subState = new LinkedHashMap<>();
      subState.put("subFoo", "subBar");
      g.getState().put("subState", subState);

      final String gameString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(g);

      System.out.println(gameString);
   }

   @Test
   public void testParse() throws IOException
   {
      final Model g = new ObjectMapper().readValue(getClass().getResourceAsStream("databind-1.json"), Model.class);

      System.out.println(g);
   }
}
