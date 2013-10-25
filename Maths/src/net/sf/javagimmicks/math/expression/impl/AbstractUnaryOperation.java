package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.ExpressionContext;
import net.sf.javagimmicks.math.expression.UnaryOperationExpression;

public abstract class AbstractUnaryOperation implements UnaryOperationExpression
{
    private final Expression m_oInnerExpression;

    protected AbstractUnaryOperation(Expression oInnerExpression)
    {
        m_oInnerExpression = oInnerExpression;
    }
    
    @Override
    public Expression getInnerExpression()
    {
        return m_oInnerExpression;
    }

    @Override
    public BigDecimal evaluate(ExpressionContext oContext)
    {
        return evaluate(m_oInnerExpression.evaluate(oContext));
    }

    abstract protected BigDecimal evaluate(BigDecimal oEvaluatedExpression);
}