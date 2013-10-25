package net.sf.javagimmicks.math.expression.parse.plugin;

import java.util.regex.Matcher;

import net.sf.javagimmicks.math.expression.parse.ParseContext;

public class Helper
{
    public static int getMarkerNumber(String sExpressionMarker)
    {
        final Matcher oMatcher = ParseContext.PATTERN_EXPRESSION_MARKER.matcher(sExpressionMarker);
        
        if(!oMatcher.matches())
        {
            throw new IllegalArgumentException("String '" + sExpressionMarker + "' is not an expression marker!");
        }
        
        return Integer.parseInt(oMatcher.group(1));
    }
}
