package math;

import java.math.BigDecimal;

import math.expression.Expression;
import math.expression.impl.DefaultExpressionContext;
import math.parse.impl.DefaultParser;

import org.junit.Assert;
import org.junit.Test;

public class MathTest
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
    
    private void test(String sExpression, BigDecimal oExpected) throws Exception
    {
        
        final Expression oExpression = new DefaultParser().parse(sExpression);
        System.out.println(oExpression);
        
        final BigDecimal oResult = oExpression.evaluate(new DefaultExpressionContext());
        System.out.println(oResult);
        
        Assert.assertEquals(oExpected, oResult);
    }
}
