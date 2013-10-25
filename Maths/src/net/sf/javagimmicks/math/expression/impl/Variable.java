package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.ExpressionContext;

public class Variable implements Expression
{
    private String m_sVarName;
    
    public Variable(String sVarName)
    {
        m_sVarName = sVarName;
    }

    public String getVariableName()
    {
        return m_sVarName;
    }

    @Override
    public BigDecimal evaluate(ExpressionContext oContext)
    {
        return oContext.resolveVariable(m_sVarName);
    }
}
