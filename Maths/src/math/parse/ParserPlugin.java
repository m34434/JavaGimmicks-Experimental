package math.parse;



public interface ParserPlugin
{
    int getPriority();

    boolean parse(ParseContext oContext);
}
