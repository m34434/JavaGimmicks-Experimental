package net.sf.javagimmicks.ase;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.sf.javagimmicks.ase.ActionNotAvailableException;
import net.sf.javagimmicks.ase.ActionScriptFailedException;
import net.sf.javagimmicks.ase.Engine;
import net.sf.javagimmicks.ase.config.ActionConfig;
import net.sf.javagimmicks.ase.config.DependencyConfig;
import net.sf.javagimmicks.ase.config.EngineConfig;
import net.sf.javagimmicks.ase.config.NodeConfig;
import net.sf.javagimmicks.ase.config.NodeConfig.TransitionConfig;

public class EngineTest
{
   @Test
   public void testSerialize()
         throws JsonProcessingException, ActionNotAvailableException, ActionScriptFailedException, ScriptException
   {
      //////////////////
      // Setup engine //
      //////////////////

      // Create engine and nodes
      final EngineConfig ec = new EngineConfig("a");

      final NodeConfig a = new NodeConfig("a");
      final NodeConfig b = new NodeConfig("b");
      final NodeConfig c = new NodeConfig("c");

      ec.getNodes().add(a);
      ec.getNodes().add(b);
      ec.getNodes().add(c);

      // Add node transitions
      a.getTransitions().add(new TransitionConfig("b"));
      a.getTransitions().add(new TransitionConfig("c"));
      b.getTransitions().add(new TransitionConfig("c"));
      c.getTransitions().add(new TransitionConfig("a"));

      // Setup initial state
      ec.getState().put("foo", "bar");
      ec.getState().put("num", 1000L);

      final Map<String, Object> subState = new LinkedHashMap<>();
      subState.put("subFoo", "subBar");
      ec.getState().put("subState", subState);

      // Action "changeFoo"
      final ActionConfig actionChangeFoo = new ActionConfig("changeFoo", "foo = 'argh!'\nsubState.subFoo = 'dummy'",
            "b");
      final DependencyConfig changeFooDep1 = new DependencyConfig("foo", "bar");
      actionChangeFoo.getDependencies().add(changeFooDep1);

      ec.getActions().add(actionChangeFoo);

      // Action "incNum"
      final ActionConfig incNum = new ActionConfig("incNum", "num = num + 1");
      ec.getActions().add(incNum);

      // Action "incNum2"
      final ActionConfig incNum2 = new ActionConfig("incNum2", "num = num + 2");
      b.getActions().add(incNum2);

      // Serialize config
      System.out.println(ec.asJson(true));

      // Create engine, make first checks
      final Engine engine = new Engine(ec);

      /////////////////
      // Test engine //
      /////////////////
      Assert.assertEquals("a", engine.getState().getNode());
      Assert.assertEquals(new HashSet<>(asList("changeFoo", "incNum")), engine.getAvailableActions());
      System.out.println(engine.getState().asJson(true));

      // Try to invoke action from a different node
      try
      {
         engine.performAction("incNum2");
         Assert.fail("ActionNotAvailableException expected!");
      }
      catch (ActionNotAvailableException ex)
      {
         Assert.assertFalse(ex.isDependenciesFailed());
         Assert.assertEquals("incNum2", ex.getActionName());
      }

      // Invoke global action that updates state and node
      engine.performAction("changeFoo");
      Assert.assertEquals("b", engine.getState().getNode()); // Node changed
      Assert.assertEquals(new HashSet<>(asList("incNum", "incNum2")), engine.getAvailableActions());
      System.out.println(engine.getState().asJson(true));

      // Try to invoke global action with (now) unsatisfied dependencies
      try
      {
         engine.performAction("changeFoo");
         Assert.fail("ActionNotAvailableException expected!");
      }
      catch (ActionNotAvailableException ex)
      {
         Assert.assertTrue(ex.isDependenciesFailed());
         Assert.assertEquals("changeFoo", ex.getActionName());
      }

      // Invoke other global action that has no dependencies
      engine.performAction("incNum");
      Assert.assertEquals(1001d, engine.getState("num"));
      System.out.println(engine.getState().asJson(true));
      Assert.assertEquals(new HashSet<>(asList("incNum", "incNum2")), engine.getAvailableActions());

      // Invoke node-local action that was not available before
      engine.performAction("incNum2");
      Assert.assertEquals(1003d, engine.getState("num"));
      System.out.println(engine.getState().asJson(true));
      Assert.assertEquals(new HashSet<>(asList("incNum", "incNum2")), engine.getAvailableActions());
   }

   @Test
   public void testParse() throws IOException
   {
      final EngineConfig g = new ObjectMapper().readValue(getClass().getResourceAsStream("engine-1.json"),
            EngineConfig.class);

      System.out.println(g);
   }
}
