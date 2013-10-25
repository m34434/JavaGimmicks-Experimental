package net.sf.javagimmicks.math.expression.parse.plugin;

import java.util.regex.Pattern;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.impl.AddOperation;
import net.sf.javagimmicks.math.expression.impl.SubtractOperation;
import net.sf.javagimmicks.math.expression.parse.PatternBuilder;

public class AddSubtractParserPlugin extends AbstractRegexParserPlugin
{
    private final Pattern PATTERN = new PatternBuilder().appendExpressionMarker().appendRegexGroup("[+-]").appendExpressionMarker().build();
    
    public static int PRIOPRITY = 40;
    
    @Override
    public int getPriority()
    {
        return PRIOPRITY;
    }
    
    @Override
    protected Pattern getPattern()
    {
        return PATTERN;
    }

    @Override
    protected boolean parse(RegexParseContext oContext)
    {
       final Expression oLeftOperand = oContext.getExpressionForMatchedMarker(1);
       final String sOperationString = oContext.group(2);
       final Expression oRightOperand = oContext.getExpressionForMatchedMarker(3);
       
       final Expression oNewExpression;
       
       if("+".equals(sOperationString))
       {
           oNewExpression = new AddOperation(oLeftOperand, oRightOperand);
       }
       else
       {
           oNewExpression = new SubtractOperation(oLeftOperand, oRightOperand);
       }
       
       oContext.registerExpressionForMatch(oNewExpression);
       
       return true;
    }
}