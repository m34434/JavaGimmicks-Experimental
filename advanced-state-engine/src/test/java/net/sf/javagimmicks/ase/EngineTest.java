package net.sf.javagimmicks.ase;

import static java.util.Arrays.asList;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;

import javax.script.ScriptException;

import org.junit.Assert;
import org.junit.Test;

import net.sf.javagimmicks.ase.config.EngineConfig;

public class EngineTest
{
   @Test
   public void testIt()
         throws ActionNotAvailableException, ActionScriptFailedException, ScriptException, IOException,
         TransitionNotAvailableException
   {
      // Create engine, make first checks
      final Engine engine = new Engine(EngineConfig.fromJson(getClass().getResourceAsStream("engine-1.json")));

      Assert.assertEquals("a", engine.getState().getNode());
      Assert.assertEquals("subBar", engine.getState("subState.subFoo"));
      Assert.assertEquals(new HashSet<>(asList("changeFoo", "incNum")), engine.getAvailableActions());
      Assert.assertEquals(new HashSet<>(asList("b", "c")), engine.getAvailableTransitions());
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

      // Try to take transition not existing transition
      try
      {
         engine.takeTransition("d");
         Assert.fail("TransitionNotAvailableException expected!");
      }
      catch (TransitionNotAvailableException e)
      {
         Assert.assertEquals("d", e.getTargetNodeName());
         Assert.assertFalse(e.isDependenciesFailed());
      }

      // Invoke global action that updates state and node
      engine.performAction("changeFoo");
      Assert.assertEquals("b", engine.getState().getNode()); // Node changed
      Assert.assertEquals("dummy", engine.getState("subState.subFoo"));
      Assert.assertEquals(new HashSet<>(asList("incNum", "incNum2")), engine.getAvailableActions());
      Assert.assertTrue(engine.getAvailableTransitions().isEmpty());
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

      // Try to take transition with unsatisfied dependencies
      try
      {
         engine.takeTransition("c");
         Assert.fail("TransitionNotAvailableException expected!");
      }
      catch (TransitionNotAvailableException e)
      {
         Assert.assertEquals("c", e.getTargetNodeName());
         Assert.assertTrue(e.isDependenciesFailed());
      }

      // Invoke node-local action that was not available before
      engine.performAction("incNum2");
      Assert.assertEquals(1003d, engine.getState("num"));
      System.out.println(engine.getState().asJson(true));
      Assert.assertEquals(new HashSet<>(asList("incNum", "incNum2")), engine.getAvailableActions());
      Assert.assertEquals(Collections.singleton("c"), engine.getAvailableTransitions());

      // Take now available transition
      engine.takeTransition("c");
      Assert.assertEquals("c", engine.getState().getNode());
      Assert.assertEquals(Collections.singleton("a"), engine.getAvailableTransitions());
   }
}
