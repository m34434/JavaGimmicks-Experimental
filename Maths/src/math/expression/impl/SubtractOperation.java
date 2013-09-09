package math.expression.impl;

import java.math.BigDecimal;

import math.expression.Expression;

public class SubtractOperation extends AbstractBinaryOperation
{
    public SubtractOperation(Expression oLeftOperand, Expression oRightOperand)
    {
        super(oLeftOperand, oRightOperand);
    }

    @Override
    protected BigDecimal evaluate(BigDecimal oEvaluatedLeftOperand, BigDecimal oEvaluatedRightOperand)
    {
        return oEvaluatedLeftOperand.subtract(oEvaluatedRightOperand);
    }

    @Override
    protected String getSymbol()
    {
        return "-";
    }
}
