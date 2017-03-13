package net.sf.javagimmicks.adventure.model;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.javagimmicks.adventure.Engine;
import net.sf.javagimmicks.adventure.model.json.ActionConfig;
import net.sf.javagimmicks.adventure.model.json.EngineConfig;
import net.sf.javagimmicks.adventure.model.json.NodeConfig;
import net.sf.javagimmicks.adventure.model.json.NodeConfig.TransitionConfig;

public class DatabindTest
{
   @Test
   public void testSerialize() throws JsonProcessingException
   {
      final EngineConfig ec = new EngineConfig("a");

      final NodeConfig a = new NodeConfig("a");
      final NodeConfig b = new NodeConfig("b");
      final NodeConfig c = new NodeConfig("c");

      ec.getNodes().add(a);
      ec.getNodes().add(b);
      ec.getNodes().add(c);

      a.getTransitions().add(new TransitionConfig("b"));
      a.getTransitions().add(new TransitionConfig("c"));
      b.getTransitions().add(new TransitionConfig("c"));
      c.getTransitions().add(new TransitionConfig("a"));

      ec.getState().put("foo", "bar");
      ec.getState().put("num", 1000L);

      final Map<String, Object> subState = new LinkedHashMap<>();
      subState.put("subFoo", "subBar");
      ec.getState().put("subState", subState);

      final ActionConfig actionChangeFoo = new ActionConfig("changeFoo", "foo = \"argh!\"\nsubState.subFoo = \"dummy\"",
            "b");
      ec.getActions().add(actionChangeFoo);

      final String configString = new ObjectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(ec);
      System.out.println(configString);

      final Engine engine = new Engine(ec);
      engine.performAction("changeFoo");

      final String stateString = new ObjectMapper().writerWithDefaultPrettyPrinter()
            .writeValueAsString(engine.getState());
      System.out.println(stateString);
   }

   @Test
   public void testParse() throws IOException
   {
      final EngineConfig g = new ObjectMapper().readValue(getClass().getResourceAsStream("databind-1.json"),
            EngineConfig.class);

      System.out.println(g);
   }
}
