package net.sf.javagimmicks.math.expression.parse.plugin;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.impl.BracketExpression;
import net.sf.javagimmicks.math.expression.parse.ParseContext;
import net.sf.javagimmicks.math.expression.parse.ParserPlugin;

public class BracketParserPlugin implements ParserPlugin
{
    public static final int PRIORITY = 90;

    @Override
    public int getPriority()
    {
        return PRIORITY;
    }

    @Override
    public boolean parse(ParseContext oContext)
    {
        final String sExpressionString = oContext.getParseExpression();
        int iLastOpeningBracket = sExpressionString.lastIndexOf('(');
        
        if(iLastOpeningBracket < 0)
        {
            return false;
        }
        
        int iMatchingClosingBracket = sExpressionString.indexOf(')', iLastOpeningBracket);
        if(iMatchingClosingBracket < 0)
        {
            throw new IllegalStateException("No matching closing bracket found for last opening bracket in '" + sExpressionString + "'!");
        }
        
        final Expression oInnerExpression = oContext.parseSubexpression(iLastOpeningBracket + 1, iMatchingClosingBracket);
        final BracketExpression oOuterExpression = new BracketExpression(oInnerExpression);
        oContext.registerExpression(iLastOpeningBracket, iMatchingClosingBracket + 1, oOuterExpression);
        
        return true;
    }
}
