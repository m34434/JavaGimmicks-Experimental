package net.sf.javagimmicks.math.expression.parse;

import java.util.SortedSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javagimmicks.math.expression.Expression;

public interface ParseContext
{
    Pattern PATTERN_EXPRESSION_MARKER = Pattern.compile(Pattern.quote("$$") + "(\\d+)" + Pattern.quote("$$"));
    
    String getParseExpression();
    Matcher match(Pattern pattern);
    Matcher match(PatternBuilder patternBuilder);
    
    SortedSet<ParserPlugin> getPlugins();
    
    Expression parseSubexpression(int startIndex, int endIndex);
    
    int registerExpression(int startIndex, int endIndex, Expression expression);
    
    Expression getExpression(int expressionIndex);
    Expression getExpression(String expressionIndex);
    Expression getExpression(Matcher matcher, int group);
    Expression getExpressionForMarker(String expressionMarker);
}