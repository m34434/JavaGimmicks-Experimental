package net.sf.javagimmicks.math.expression.parse;

import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javagimmicks.math.expression.Expression;

public interface ParseContext
{
    Pattern PATTERN_EXPRESSION_MARKER = Pattern.compile(Pattern.quote("$$") + "(\\d+)" + Pattern.quote("$$"));
    
    String getParseExpression();
    Matcher match(Pattern oPattern);
    Matcher match(PatternBuilder oPatternBuilder);
    
    SortedSet<ParserPlugin> getPlugins();
    
    Expression parseSubexpression(int iStartIndex, int iEndIndex);
    
    int registerExpression(int iStartIndex, int iEndIndex, Expression oExpression);
    
    Expression getExpression(int iExpressionIndex);
    Expression getExpression(String sExpressionIndex);
    Expression getExpression(Matcher oMatcher, int iGroup);
    Expression getExpressionForMarker(String sExpressionMarker);
}