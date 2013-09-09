package math.parse.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Matcher;

import math.expression.Expression;
import math.parse.ParseContext;
import math.parse.Parser;
import math.parse.ParserPlugin;
import math.parse.plugin.AddSubtractParserPlugin;
import math.parse.plugin.BracketParserPlugin;
import math.parse.plugin.Helper;
import math.parse.plugin.LiteralParserPlugin;
import math.parse.plugin.MultiplyDivideModParserPlugin;

public class DefaultParser implements Parser
{
    private final SortedSet<ParserPlugin> m_oPlugins = new TreeSet<>(new ParserPluginComparator());
    
    public DefaultParser(boolean bUseDefaultPlugins)
    {
        if(bUseDefaultPlugins)
        {
            addDefaultPlugins();
        }
    }
    
    public DefaultParser()
    {
        this(true);
    }
    
    public void addDefaultPlugins()
    {
        addPlugin(new AddSubtractParserPlugin());
        addPlugin(new MultiplyDivideModParserPlugin());
        addPlugin(new BracketParserPlugin());
        addPlugin(new LiteralParserPlugin());
    }

    @Override
    public void addPlugin(ParserPlugin oPlugin)
    {
        m_oPlugins.add(oPlugin);
    }

    @Override
    public Expression parse(String sExpressionString)
    {
        sExpressionString = sExpressionString.replaceAll("\\s", "");
        
        final DefaultContext oContext = new DefaultContext(sExpressionString);
        
        return oContext.parseSubexpression(0, sExpressionString.length());
    }

    private class DefaultContext implements ParseContext
    {
        private String m_sExpression;
        
        private final Map<Integer, Expression> m_oExpressions = new HashMap<>();
        
        private Counter m_oCurrentExpressionIndex;
        
        private DefaultContext m_oParent;
        
        public DefaultContext(String sExpression, DefaultContext oParent)
        {
            m_sExpression = sExpression;
            m_oCurrentExpressionIndex = oParent == null ? new Counter() : oParent.m_oCurrentExpressionIndex;
            m_oParent = oParent;
        }

        public DefaultContext(String sExpression)
        {
            this(sExpression, null);
        }

        @Override
        public String getStringExpression()
        {
            return m_sExpression;
        }

        @Override
        public SortedSet<ParserPlugin> getPlugins()
        {
            return Collections.unmodifiableSortedSet(m_oPlugins);
        }

        @Override
        public Expression parseSubexpression(int iStartIndex, int iEndIndex)
        {
            final String sSubstring = m_sExpression.substring(iStartIndex, iEndIndex);
            
            final DefaultContext oSubContext = new DefaultContext(sSubstring, this);
            
            boolean bAppliedChanges = true;
            while(bAppliedChanges)
            {
                bAppliedChanges = false;
                
                for(ParserPlugin oPlugin : m_oPlugins)
                {
                    if(oPlugin.parse(oSubContext))
                    {
                        bAppliedChanges = true;
                        break;
                    }
                }
            }

            final String sSubContextExpression = oSubContext.getStringExpression();
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
        public Expression resolveExpression(String sExpressionMarker)
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
    }
    
    private static class ParserPluginComparator implements Comparator<ParserPlugin>
    {
        @Override
        public int compare(ParserPlugin o1, ParserPlugin o2)
        {
            return o2.getPriority() - o1.getPriority();
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
