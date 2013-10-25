package net.sf.javagimmicks.math.expression;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.impl.DefaultExpressionContext;
import net.sf.javagimmicks.math.expression.parse.impl.DefaultParser;

import org.junit.Assert;
import org.junit.Test;

public class ExpressionTest
{
    @Test
    public void test1() throws Exception
    {
        test("1 + 2 + 3 + 4", BigDecimal.valueOf(10));
    }
    
    @Test
    public void test2() throws Exception
    {
        test("(1 + 2) - (3 - 4)", BigDecimal.valueOf(4));
    }
    
    @Test
    public void test3() throws Exception
    {
        test("1 + 2 * 3 - 4", BigDecimal.valueOf(3));
    }
    
    @Test
    public void test4() throws Exception
    {
        test("(1 + 2) * (3 - 4)", BigDecimal.valueOf(-3));
    }
    
    @Test
    public void test5() throws Exception
    {
        test("((1 - 2) + 3) * 4", BigDecimal.valueOf(8));
    }
    
    @Test
    public void test6() throws Exception
    {
        test("((1 + 2) - (3 - 4) ) * (5 + 6)", BigDecimal.valueOf(44));
    }
    
    @Test
    public void test7() throws Exception
    {
        test("((1 + 2) - (3 - 4) ) * (5 + 13 % 7)", BigDecimal.valueOf(44));
    }
    
    private void test(String sExpression, BigDecimal oExpected) throws Exception
    {
        
        final Expression oExpression = new DefaultParser().parse(sExpression);
        System.out.println(oExpression);
        
        final BigDecimal oResult = oExpression.evaluate(new DefaultExpressionContext());
        System.out.println(oResult);
        
        Assert.assertEquals(oExpected, oResult);
    }
}
