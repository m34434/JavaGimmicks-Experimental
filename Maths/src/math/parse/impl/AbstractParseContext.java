package math.parse.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import math.expression.Expression;
import math.parse.ParseContext;
import math.parse.ParserPlugin;
import math.parse.PatternBuilder;
import math.parse.plugin.Helper;

public abstract class AbstractParseContext implements ParseContext
{
    private String m_sExpression;
    
    private final Map<Integer, Expression> m_oExpressions = new HashMap<>();
    
    private Counter m_oCurrentExpressionIndex;
    
    private AbstractParseContext m_oParent;
    
    protected AbstractParseContext(String sExpression, AbstractParseContext oParent)
    {
        m_sExpression = sExpression;
        m_oCurrentExpressionIndex = oParent == null ? new Counter() : oParent.m_oCurrentExpressionIndex;
        m_oParent = oParent;
    }

    protected AbstractParseContext(String sExpression)
    {
        this(sExpression, null);
    }
    
    abstract protected AbstractParseContext createChildContext(String sChildExpression);

    @Override
    public String getParseExpression()
    {
        return m_sExpression;
    }
    
    @Override
    public Matcher match(Pattern oPattern)
    {
        return oPattern.matcher(m_sExpression);
    }

    @Override
    public Matcher match(PatternBuilder oPatternBuilder)
    {
        return match(oPatternBuilder.build());
    }

    @Override
    public Expression parseSubexpression(int iStartIndex, int iEndIndex)
    {
        final String sSubstring = m_sExpression.substring(iStartIndex, iEndIndex);
        
        final AbstractParseContext oSubContext = createChildContext(sSubstring);
        
        boolean bAppliedChanges = true;
        while(bAppliedChanges)
        {
            bAppliedChanges = false;
            
            for(ParserPlugin oPlugin : getPlugins())
            {
                if(oPlugin.parse(oSubContext))
                {
                    bAppliedChanges = true;
                    break;
                }
            }
        }

        final String sSubContextExpression = oSubContext.getParseExpression();
        final Matcher oMatcher = ParseContext.PATTERN_EXPRESSION_MARKER.matcher(sSubContextExpression);
        
        if(!oMatcher.matches())
        {
            throw new IllegalStateException("Could not parse sub expression '" + oSubContext + "'!");
        }
        
        return oSubContext.getExpression(Integer.parseInt(oMatcher.group(1)));
    }

    @Override
    public int registerExpression(int iStartIndex, int iEndIndex, Expression oExpression)
    {
        final String sReplacedExpression = m_sExpression.substring(iStartIndex, iEndIndex);
        
        final int iExpressionIndex = m_oCurrentExpressionIndex.incrementAndGet();
        m_oExpressions.put(iExpressionIndex, oExpression);
        m_sExpression = m_sExpression.substring(0, iStartIndex) + "$$" + iExpressionIndex + "$$" + m_sExpression.substring(iEndIndex, m_sExpression.length());
        
        final Matcher oReplacedMarkersMatcher = PATTERN_EXPRESSION_MARKER.matcher(sReplacedExpression);
        while(oReplacedMarkersMatcher.find())
        {
            int iMarkerIndex = Integer.parseInt(oReplacedMarkersMatcher.group(1));
            
            unregisterExpressionMarker(iMarkerIndex);
        }
        
        return iExpressionIndex;
    }

    @Override
    public Expression getExpression(int iExpressionIndex)
    {
        if(m_oExpressions.containsKey(iExpressionIndex))
        {
            return m_oExpressions.get(iExpressionIndex);
        }
        
        if(m_oParent == null)
        {
            throw new IllegalStateException("Cannot find expression with marker index '" + iExpressionIndex + "'!");
        }
        
        return m_oParent.getExpression(iExpressionIndex);
    }
    
    @Override
    public Expression getExpression(String sExpressionIndex)
    {
        return getExpression(Integer.parseInt(sExpressionIndex));
    }

    @Override
    public Expression getExpression(Matcher oMatcher, int iGroup)
    {
        return getExpression(oMatcher.group(iGroup));
    }

    @Override
    public Expression getExpressionForMarker(String sExpressionMarker)
    {
        return getExpression(Helper.getMarkerNumber(sExpressionMarker));
    }
    
    public String toString()
    {
        final Matcher oMatcher = ParseContext.PATTERN_EXPRESSION_MARKER.matcher(m_sExpression);
        
        final StringBuffer oResult = new StringBuffer();
        
        while(oMatcher.find())
        {
            oMatcher.appendReplacement(oResult, getExpression(Integer.parseInt(oMatcher.group(1))).toString());
        }
        
        oMatcher.appendTail(oResult);
        
        return oResult.toString();
    }
    
    private void unregisterExpressionMarker(int iMarkerIndex)
    {
        m_oExpressions.remove(iMarkerIndex);
        
        if(m_oParent != null)
        {
            m_oParent.unregisterExpressionMarker(iMarkerIndex);
        }
    }
    
    @SuppressWarnings("unused")
    private static class Counter
    {
        private int m_iValue;
        
        public int get()
        {
            return m_iValue;
        }
        
        public int getAndIncrement()
        {
            return m_iValue++;
        }
        
        public int incrementAndGet()
        {
            return ++m_iValue;
        }
    }
}
