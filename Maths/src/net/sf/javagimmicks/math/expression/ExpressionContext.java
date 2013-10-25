package net.sf.javagimmicks.math.expression;

import java.math.BigDecimal;

public interface ExpressionContext
{
    BigDecimal resolveVariable(String sVarName);
}
