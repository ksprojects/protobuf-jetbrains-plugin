package io.protostuff.jetbrains.plugin;

import io.protostuff.compiler.parser.ProtoLexer;
import org.antlr.jetbrains.adapter.lexer.AntlrLexerAdapter;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoLexerAdapter extends AntlrLexerAdapter {

    public ProtoLexerAdapter() {
        super(ProtoLanguage.INSTANCE, new ProtoLexer(null), ProtoParserDefinition.ELEMENT_FACTORY);
    }
}
