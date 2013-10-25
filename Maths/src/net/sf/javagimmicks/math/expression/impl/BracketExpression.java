package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.Expression;

public class BracketExpression extends AbstractUnaryOperation
{
    public BracketExpression(Expression oInnerExpression)
    {
        super(oInnerExpression);
    }

    @Override
    protected BigDecimal evaluate(BigDecimal oEvaluatedExpression)
    {
        return oEvaluatedExpression;
    }
    
    public String toString()
    {
        return "( " + getInnerExpression() + " )";
    }
}
