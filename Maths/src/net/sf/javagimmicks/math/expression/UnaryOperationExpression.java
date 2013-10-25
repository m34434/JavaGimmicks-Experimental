package net.sf.javagimmicks.math.expression;

public interface UnaryOperationExpression extends Expression
{
    Expression getInnerExpression();
}
