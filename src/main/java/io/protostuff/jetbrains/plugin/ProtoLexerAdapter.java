package io.protostuff.jetbrains.plugin;

import com.intellij.lang.Language;
import io.protostuff.compiler.parser.ProtoLexer;
import org.antlr.jetbrains.adapter.lexer.ANTLRLexerAdaptor;
import org.antlr.v4.runtime.Lexer;

/**
 * @author Kostiantyn Shchepanovskyi
 */
public class ProtoLexerAdapter extends ANTLRLexerAdaptor {

    public ProtoLexerAdapter() {
        super(ProtoLanguage.INSTANCE, new ProtoLexer(null));
    }
}
