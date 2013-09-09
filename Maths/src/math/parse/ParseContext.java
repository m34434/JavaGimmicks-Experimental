package math.parse;

import java.util.SortedSet;
import java.util.regex.Pattern;

import math.expression.Expression;

public interface ParseContext
{
    Pattern PATTERN_EXPRESSION_MARKER = Pattern.compile(Pattern.quote("$$") + "(\\d+)" + Pattern.quote("$$"));
    
    String getStringExpression();
    
    SortedSet<ParserPlugin> getPlugins();
    
    Expression parseSubexpression(int iStartIndex, int iEndIndex);
    
    int registerExpression(int iStartIndex, int iEndIndex, Expression oExpression);
    
    Expression getExpression(int iExpressionIndex);
    Expression resolveExpression(String sExpressionMarker);
}