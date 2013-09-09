package math.expression.impl;

import java.math.BigDecimal;

import math.expression.Expression;

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
