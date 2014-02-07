package net.sf.javagimmicks.math.expression.parse.impl;

import java.util.Collections;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import net.sf.javagimmicks.math.expression.Expression;
import net.sf.javagimmicks.math.expression.parse.Parser;
import net.sf.javagimmicks.math.expression.parse.ParserPlugin;
import net.sf.javagimmicks.math.expression.parse.plugin.AddSubtractParserPlugin;
import net.sf.javagimmicks.math.expression.parse.plugin.BracketParserPlugin;
import net.sf.javagimmicks.math.expression.parse.plugin.LiteralParserPlugin;
import net.sf.javagimmicks.math.expression.parse.plugin.MultiplyDivideModParserPlugin;

public class DefaultParser implements Parser
{
    private final SortedSet<ParserPlugin> _plugins = new TreeSet<>(new ParserPluginComparator());
    
    public DefaultParser(boolean useDefaultPlugins)
    {
        if(useDefaultPlugins)
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
    public void addPlugin(ParserPlugin plugin)
    {
        _plugins.add(plugin);
    }

    @Override
    public Expression parse(String expressionString)
    {
        expressionString = expressionString.replaceAll("\\s", "");
        
        final DefaultContext context = new DefaultContext(expressionString);
        
        return context.parseSubexpression(0, expressionString.length());
    }

    private class DefaultContext extends AbstractParseContext
    {
        public DefaultContext(String expression)
        {
            super(expression);
        }

        protected DefaultContext(String expression, DefaultContext parentContext)
        {
            super(expression, parentContext);
        }

        @Override
        public SortedSet<ParserPlugin> getPlugins()
        {
            return Collections.unmodifiableSortedSet(_plugins);
        }

        @Override
        protected AbstractParseContext createChildContext(String childExpression)
        {
            return new DefaultContext(childExpression, this);
        }
    }
    
    private static class ParserPluginComparator implements Comparator<ParserPlugin>
    {
        @Override
        public int compare(ParserPlugin p1, ParserPlugin p2)
        {
            return p2.getPriority() - p1.getPriority();
        }
    }
}
