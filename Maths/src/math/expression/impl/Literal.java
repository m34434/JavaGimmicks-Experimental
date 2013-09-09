package math.expression.impl;

import java.math.BigDecimal;

import math.expression.Expression;
import math.expression.ExpressionContext;

public class Literal implements Expression
{
    private final BigDecimal m_oValue;
    
    public Literal(BigDecimal oValue)
    {
        m_oValue = oValue;
    }

    @Override
    public BigDecimal evaluate(ExpressionContext oContext)
    {
        return m_oValue;
    }
    
    public String toString()
    {
        return m_oValue.toString();
    }
}