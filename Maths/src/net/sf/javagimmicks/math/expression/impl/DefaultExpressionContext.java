package net.sf.javagimmicks.math.expression.impl;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import net.sf.javagimmicks.math.expression.ExpressionContext;

public class DefaultExpressionContext implements ExpressionContext
{
    private final Map<String, BigDecimal> m_oVariables = new HashMap<String, BigDecimal>();
    
    @Override
    public BigDecimal resolveVariable(String sVarName)
    {
        return m_oVariables.get(sVarName);
    }
    
    public void registerVariable(String sVarName, BigDecimal oValue)
    {
        m_oVariables.put(sVarName, oValue);
    }
}
