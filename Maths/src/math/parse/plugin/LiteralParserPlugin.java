package math.parse.plugin;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import math.expression.Expression;
import math.expression.impl.Literal;
import math.parse.ParseContext;
import math.parse.ParserPlugin;

public class LiteralParserPlugin implements ParserPlugin
{
    private static final Pattern PATTERN = Pattern.compile("((\\d+)(\\.\\d+)?)");
    
    public static final int PRIORITY = Integer.MAX_VALUE;
    
    @Override
    public int getPriority()
    {
        return PRIORITY;
    }

    @Override
    public boolean parse(ParseContext oContext)
    {
        boolean bAppliedChanges = false;
        
        final String sStringExpression = oContext.getParseExpression();
        final Matcher oMatcher = PATTERN.matcher(sStringExpression);
        
        while(oMatcher.find())
        {
            if(isExpressionMarker(sStringExpression, oMatcher))
            {
                continue;
            }

            final Expression oLiteral = new Literal(new BigDecimal(oMatcher.group()));
            
            oContext.registerExpression(oMatcher.start(), oMatcher.end(), oLiteral);
            
            bAppliedChanges = true;
            
            break;
        }
        
        return bAppliedChanges;
    }

    private boolean isExpressionMarker(String sStringExpression, Matcher oMatcher)
    {
        final int iRegionStart = oMatcher.start();
        final int iRegionEnd = oMatcher.end();
        
        // No space for "$$" at the beginning
        if(iRegionStart < 2)
        {
            return false;
        }
        
        // No space for "$$" at the end
        if(iRegionEnd + 2 > sStringExpression.length())
        {
            return false;
        }
        
        final String sBadString = "$$" + oMatcher.group() + "$$";
        
        return sStringExpression.substring(iRegionStart - 2, iRegionEnd + 2).equals(sBadString);
    }

}
