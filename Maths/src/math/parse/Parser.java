package math.parse;

import math.expression.Expression;

public interface Parser
{
    void addPlugin(ParserPlugin oPlugin);
    
    Expression parse(String sExpressionString);
}
