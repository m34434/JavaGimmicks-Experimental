package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.Expression;

public class ModuloOperation extends AbstractBinaryOperation
{
    public ModuloOperation(Expression oLeftOperand, Expression oRightOperand)
    {
        super(oLeftOperand, oRightOperand);
    }

    @Override
    protected BigDecimal evaluate(BigDecimal oEvaluatedLeftOperand, BigDecimal oEvaluatedRightOperand)
    {
        return oEvaluatedLeftOperand.remainder(oEvaluatedRightOperand);
    }

    @Override
    protected String getSymbol()
    {
        return "%";
    }

}
