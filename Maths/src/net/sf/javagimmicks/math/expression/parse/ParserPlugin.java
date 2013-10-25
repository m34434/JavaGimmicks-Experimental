package net.sf.javagimmicks.math.expression.parse;



public interface ParserPlugin
{
    int getPriority();

    boolean parse(ParseContext oContext);
}
