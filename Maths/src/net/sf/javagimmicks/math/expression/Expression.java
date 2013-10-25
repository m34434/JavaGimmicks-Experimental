package net.sf.javagimmicks.math.expression;

import java.math.BigDecimal;

public interface Expression
{
    BigDecimal evaluate(ExpressionContext oContext);
}
