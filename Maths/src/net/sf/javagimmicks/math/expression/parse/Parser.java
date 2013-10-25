package net.sf.javagimmicks.math.expression.parse;

import net.sf.javagimmicks.math.expression.Expression;

public interface Parser
{
    void addPlugin(ParserPlugin oPlugin);
    
    Expression parse(String sExpressionString);
}
