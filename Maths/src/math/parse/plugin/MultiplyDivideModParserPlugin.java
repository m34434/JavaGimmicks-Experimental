package math.parse.plugin;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import math.expression.Expression;
import math.expression.impl.DivideOperation;
import math.expression.impl.ModuloOperation;
import math.expression.impl.MultiplyOperation;
import math.parse.ParseContext;
import math.parse.ParserPlugin;

public class MultiplyDivideModParserPlugin implements ParserPlugin
{
    private final Pattern PATTERN = Pattern.compile(ParseContext.PATTERN_EXPRESSION_MARKER.pattern() + "([*/%])" + ParseContext.PATTERN_EXPRESSION_MARKER.pattern());
    
    public static int PRIOPRITY = 50;
    
    @Override
    public int getPriority()
    {
        return PRIOPRITY;
    }

    @Override
    public boolean parse(ParseContext oContext)
    {
       final String sExpressionString = oContext.getStringExpression();
       
       final Matcher oMatcher = PATTERN.matcher(sExpressionString);
       
       if(!oMatcher.find())
       {
           return false;
       }
       
       final Expression oLeftOperand = oContext.getExpression(Integer.parseInt(oMatcher.group(1)));
       final String sOperationString = oMatcher.group(2);
       final Expression oRightOperand = oContext.getExpression(Integer.parseInt(oMatcher.group(3)));
       
       final Expression oNewExpression;
       
       if("*".equals(sOperationString))
       {
           oNewExpression = new MultiplyOperation(oLeftOperand, oRightOperand);
       }
       else if("/".equals(sOperationString))
       {
           oNewExpression = new DivideOperation(oLeftOperand, oRightOperand);
       }
       else
       {
           oNewExpression = new ModuloOperation(oLeftOperand, oRightOperand);
       }
       
       oContext.registerExpression(oMatcher.start(), oMatcher.end(), oNewExpression);
       
       return true;
    }
}