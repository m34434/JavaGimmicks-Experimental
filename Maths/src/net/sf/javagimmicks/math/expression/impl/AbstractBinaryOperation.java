package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.BinaryOperationExpression;
import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.ExpressionContext;

public abstract class AbstractBinaryOperation implements BinaryOperationExpression
{
    private final Expression m_oLeftOperand;
    private final Expression m_oRightOperand;

    protected AbstractBinaryOperation(Expression oLeftOperand, Expression oRightOperand)
    {
        m_oLeftOperand = oLeftOperand;
        m_oRightOperand = oRightOperand;
    }
    
    @Override
    public Expression getLeftOperand()
    {
        return m_oLeftOperand;
    }

    @Override
    public Expression getRightOperand()
    {
        return m_oRightOperand;
    }

    @Override
    public BigDecimal evaluate(ExpressionContext oContext)
    {
        final BigDecimal oEvaluatedLeftOperand = m_oLeftOperand.evaluate(oContext);
        final BigDecimal oEvaluatedRightOperand = m_oRightOperand.evaluate(oContext);
        
        return evaluate(oEvaluatedLeftOperand, oEvaluatedRightOperand);
    }
    
    public String toString()
    {
        return getLeftOperand().toString() + " " + getSymbol() + " " + getRightOperand();
    }

    abstract protected BigDecimal evaluate(BigDecimal oEvaluatedLeftOperand, BigDecimal oEvaluatedRightOperand);
    abstract protected String getSymbol();
}