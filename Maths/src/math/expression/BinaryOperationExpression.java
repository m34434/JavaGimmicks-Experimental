package math.expression;

public interface BinaryOperationExpression extends Expression
{
    Expression getLeftOperand();
    Expression getRightOperand();
}
