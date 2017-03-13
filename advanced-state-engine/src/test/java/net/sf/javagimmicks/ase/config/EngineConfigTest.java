package net.sf.javagimmicks.ase.config;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.script.ScriptException;

import org.apache.commons.io.IOUtils;
import org.junit.Assert;
import org.junit.Test;

import net.sf.javagimmicks.ase.ActionNotAvailableException;
import net.sf.javagimmicks.ase.ActionScriptFailedException;
import net.sf.javagimmicks.ase.EngineTest;
import net.sf.javagimmicks.ase.config.NodeConfig.TransitionConfig;

public class EngineConfigTest
{
   @Test
   public void testSerialize()
         throws ActionNotAvailableException, ActionScriptFailedException, ScriptException, IOException
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
      final TransitionConfig transB2C = new TransitionConfig("c");
      a.getTransitions().add(new TransitionConfig("b"));
      a.getTransitions().add(new TransitionConfig("c"));
      b.getTransitions().add(transB2C);
      c.getTransitions().add(new TransitionConfig("a"));

      // Transition dependency
      final DependencyConfig depTransB2C = new DependencyConfig("num >= 1003");
      transB2C.getDependencies().add(depTransB2C);

      // Setup initial state
      ec.getState().put("foo", "bar");
      ec.getState().put("num", 1000L);

      final Map<String, Object> subState = new LinkedHashMap<>();
      subState.put("subFoo", "subBar");
      ec.getState().put("subState", subState);

      // Action "changeFoo"
      final ActionConfig actionChangeFoo = new ActionConfig("changeFoo", "foo = 'argh!';subState.subFoo = 'dummy';",
            "b");
      final DependencyConfig changeFooDep1 = new DependencyConfig("foo", "bar");
      actionChangeFoo.getDependencies().add(changeFooDep1);

      ec.getActions().add(actionChangeFoo);

      // Action "incNum"
      final ActionConfig incNum = new ActionConfig("incNum", "num = num + 1;");
      ec.getActions().add(incNum);

      // Action "incNum2"
      final ActionConfig incNum2 = new ActionConfig("incNum2", "num = num + 2;");
      b.getActions().add(incNum2);

      final String json = ec.asJson(true);
      System.out.println(json);

      Assert.assertEquals(IOUtils.toString(EngineTest.class.getResourceAsStream("engine-1.json"), "UTF-8"),
            json);
   }
}