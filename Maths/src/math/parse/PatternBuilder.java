package math.parse;

import java.util.regex.Pattern;

public class PatternBuilder
{
    private final StringBuilder m_sRegex = new StringBuilder();
    
    public PatternBuilder appendExpressionMarker()
    {
        m_sRegex.append(ParseContext.PATTERN_EXPRESSION_MARKER.pattern());
        
        return this;
    }
    
    public PatternBuilder appendRegexGroup(String sRegexGroup)
    {
        m_sRegex.append("(").append(sRegexGroup).append(")");
        
        return this;
    }
    
    public PatternBuilder appendRegex(String sRegex)
    {
        m_sRegex.append(sRegex);
        
        return this;
    }
    
    public PatternBuilder appendConstant(String sConstant)
    {
        m_sRegex.append(Pattern.quote(sConstant));
        
        return this;
    }
    
    public Pattern build()
    {
        return Pattern.compile(m_sRegex.toString());
    }
}