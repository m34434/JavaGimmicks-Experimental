package net.sf.javagimmicks.math.expression.parse.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.parse.ParseContext;
import net.sf.javagimmicks.math.expression.parse.ParserPlugin;
import net.sf.javagimmicks.math.expression.parse.PatternBuilder;
import net.sf.javagimmicks.math.expression.parse.plugin.Helper;

public abstract class AbstractParseContext implements ParseContext
{
    private String _expression;
    
    private final Map<Integer, Expression> _expressions = new HashMap<>();
    
    private Counter _currentExpressionIndex;
    
    private AbstractParseContext _parentContext;
    
    protected AbstractParseContext(String expression, AbstractParseContext parentContext)
    {
        _expression = expression;
        _currentExpressionIndex = parentContext == null ? new Counter() : parentContext._currentExpressionIndex;
        _parentContext = parentContext;
    }

    protected AbstractParseContext(String expression)
    {
        this(expression, null);
    }
    
    abstract protected AbstractParseContext createChildContext(String childExpression);

    @Override
    public String getParseExpression()
    {
        return _expression;
    }
    
    @Override
    public Matcher match(Pattern pattern)
    {
        return pattern.matcher(_expression);
    }

    @Override
    public Matcher match(PatternBuilder patternBuilder)
    {
        return match(patternBuilder.build());
    }

    @Override
    public Expression parseSubexpression(int startIndex, int endIndex)
    {
        final String substring = _expression.substring(startIndex, endIndex);
        
        final AbstractParseContext subContext = createChildContext(substring);
        
        boolean appliedChanges = true;
        while(appliedChanges)
        {
            appliedChanges = false;
            
            for(ParserPlugin plugin : getPlugins())
            {
                if(plugin.parse(subContext))
                {
                    appliedChanges = true;
                    break;
                }
            }
        }

        final String subContextExpression = subContext.getParseExpression();
        final Matcher expressionIndexMatcher = ParseContext.PATTERN_EXPRESSION_MARKER.matcher(subContextExpression);
        
        if(!expressionIndexMatcher.matches())
        {
            throw new IllegalStateException("Could not parse sub expression '" + subContext + "'!");
        }
        
        return subContext.getExpression(Integer.parseInt(expressionIndexMatcher.group(1)));
    }

    @Override
    public int registerExpression(int startIndex, int endIndex, Expression expression)
    {
        final String replacedExpression = _expression.substring(startIndex, endIndex);
        
        final int expressionIndex = _currentExpressionIndex.incrementAndGet();
        _expressions.put(expressionIndex, expression);
        _expression = _expression.substring(0, startIndex) + "$$" + expressionIndex + "$$" + _expression.substring(endIndex, _expression.length());
        
        final Matcher replacedMarkersMatcher = PATTERN_EXPRESSION_MARKER.matcher(replacedExpression);
        while(replacedMarkersMatcher.find())
        {
            int markerIndex = Integer.parseInt(replacedMarkersMatcher.group(1));
            
            unregisterExpressionMarker(markerIndex);
        }
        
        return expressionIndex;
    }

    @Override
    public Expression getExpression(int expressionIndex)
    {
        if(_expressions.containsKey(expressionIndex))
        {
            return _expressions.get(expressionIndex);
        }
        
        if(_parentContext == null)
        {
            throw new IllegalStateException("Cannot find expression with marker index '" + expressionIndex + "'!");
        }
        
        return _parentContext.getExpression(expressionIndex);
    }
    
//    @Override
    public Expression getExpression(String expressionIndex)
    {
        return getExpression(Integer.parseInt(expressionIndex));
    }

//    @Override
    public Expression getExpression(Matcher matcher, int group)
    {
        return getExpression(matcher.group(group));
    }

//    @Override
    public Expression getExpressionForMarker(String expressionMarker)
    {
        return getExpression(Helper.getMarkerNumber(expressionMarker));
    }
    
    public String toString()
    {
        final Matcher matcher = ParseContext.PATTERN_EXPRESSION_MARKER.matcher(_expression);
        
        final StringBuffer result = new StringBuffer();
        
        while(matcher.find())
        {
            matcher.appendReplacement(result, getExpression(Integer.parseInt(matcher.group(1))).toString());
        }
        
        matcher.appendTail(result);
        
        return result.toString();
    }
    
    private void unregisterExpressionMarker(int markerIndex)
    {
        _expressions.remove(markerIndex);
        
        if(_parentContext != null)
        {
            _parentContext.unregisterExpressionMarker(markerIndex);
        }
    }
    
    @SuppressWarnings("unused")
    private static class Counter
    {
        private int _value;
        
        public int get()
        {
            return _value;
        }
        
        public int getAndIncrement()
        {
            return _value++;
        }
        
        public int incrementAndGet()
        {
            return ++_value;
        }
    }
}
