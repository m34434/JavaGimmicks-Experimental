package math.expression.impl;

import java.math.BigDecimal;

import math.expression.Expression;

public class MultiplyOperation extends AbstractBinaryOperation
{
    public MultiplyOperation(Expression oLeftOperand, Expression oRightOperand)
    {
        super(oLeftOperand, oRightOperand);
    }

    @Override
    protected BigDecimal evaluate(BigDecimal oEvaluatedLeftOperand, BigDecimal oEvaluatedRightOperand)
    {
        return oEvaluatedLeftOperand.multiply(oEvaluatedRightOperand);
    }

    @Override
    protected String getSymbol()
    {
        return "*";
    }
}
