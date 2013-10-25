package net.sf.javagimmicks.math.expression.parse.plugin;

import java.util.SortedSet;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.parse.ParseContext;
import net.sf.javagimmicks.math.expression.parse.ParserPlugin;
import net.sf.javagimmicks.math.expression.parse.PatternBuilder;

public abstract class AbstractRegexParserPlugin implements ParserPlugin
{
    abstract protected Pattern getPattern();
    abstract protected boolean parse(RegexParseContext oContext);

    @Override
    public final boolean parse(ParseContext oContext)
    {
        final Matcher oMatcher = oContext.match(getPattern());
        
        if(!oMatcher.find())
        {
            return false;
        }
        
        return parse(new RegexParseContext(oContext, oMatcher));
    }
    
    public static class RegexParseContext implements ParseContext, MatchResult
    {
        private final ParseContext m_oDelegate;
        private final Matcher m_oMatcher;

        public RegexParseContext(ParseContext oDelegate, Matcher oMatcher)
        {
            m_oDelegate = oDelegate;
            m_oMatcher = oMatcher;
        }
        
        public Matcher getMatcher()
        {
            return m_oMatcher;
        }
        
        public Expression getExpressionForMatchedMarker(int iGroup)
        {
            return getExpression(m_oMatcher, iGroup);
        }

        public int registerExpressionForMatch(Expression oExpression)
        {
            return registerExpression(m_oMatcher.start(), m_oMatcher.end(), oExpression);
        }

        public MatchResult toMatchResult()
        {
            return m_oMatcher.toMatchResult();
        }

        public int start()
        {
            return m_oMatcher.start();
        }

        public int start(int group)
        {
            return m_oMatcher.start(group);
        }

        public int end()
        {
            return m_oMatcher.end();
        }

        public int end(int group)
        {
            return m_oMatcher.end(group);
        }

        public String group()
        {
            return m_oMatcher.group();
        }

        public String group(int group)
        {
            return m_oMatcher.group(group);
        }

        public String group(String name)
        {
            return m_oMatcher.group(name);
        }

        public int groupCount()
        {
            return m_oMatcher.groupCount();
        }

        public Matcher appendReplacement(StringBuffer sb, String replacement)
        {
            return m_oMatcher.appendReplacement(sb, replacement);
        }

        public StringBuffer appendTail(StringBuffer sb)
        {
            return m_oMatcher.appendTail(sb);
        }

        public Matcher region(int start, int end)
        {
            return m_oMatcher.region(start, end);
        }

        public int regionStart()
        {
            return m_oMatcher.regionStart();
        }

        public int regionEnd()
        {
            return m_oMatcher.regionEnd();
        }

        public boolean hasTransparentBounds()
        {
            return m_oMatcher.hasTransparentBounds();
        }

        public boolean hasAnchoringBounds()
        {
            return m_oMatcher.hasAnchoringBounds();
        }

        public boolean hitEnd()
        {
            return m_oMatcher.hitEnd();
        }

        public boolean requireEnd()
        {
            return m_oMatcher.requireEnd();
        }

        public String getParseExpression()
        {
            return m_oDelegate.getParseExpression();
        }

        public Matcher match(Pattern oPattern)
        {
            return m_oDelegate.match(oPattern);
        }

        public Matcher match(PatternBuilder oPatternBuilder)
        {
            return m_oDelegate.match(oPatternBuilder);
        }

        public SortedSet<ParserPlugin> getPlugins()
        {
            return m_oDelegate.getPlugins();
        }

        public Expression parseSubexpression(int iStartIndex, int iEndIndex)
        {
            return m_oDelegate.parseSubexpression(iStartIndex, iEndIndex);
        }

        public int registerExpression(int iStartIndex, int iEndIndex, Expression oExpression)
        {
            return m_oDelegate.registerExpression(iStartIndex, iEndIndex, oExpression);
        }
        
        public Expression getExpression(int iExpressionIndex)
        {
            return m_oDelegate.getExpression(iExpressionIndex);
        }

        public Expression getExpression(String sExpressionIndex)
        {
            return m_oDelegate.getExpression(sExpressionIndex);
        }

        public Expression getExpression(Matcher oMatcher, int iGroup)
        {
            return m_oDelegate.getExpression(oMatcher, iGroup);
        }

        public Expression getExpressionForMarker(String sExpressionMarker)
        {
            return m_oDelegate.getExpressionForMarker(sExpressionMarker);
        }
    }
}
