package net.sf.javagimmicks.math.expression;

public interface BinaryOperationExpression extends Expression
{
    Expression getLeftOperand();
    Expression getRightOperand();
}
