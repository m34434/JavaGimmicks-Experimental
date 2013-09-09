package math.expression;

public interface UnaryOperationExpression extends Expression
{
    Expression getInnerExpression();
}
