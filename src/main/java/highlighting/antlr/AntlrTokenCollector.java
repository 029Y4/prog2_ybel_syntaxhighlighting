package highlighting.antlr;

import highlighting.core.HighlightRegion;
import highlighting.core.SyntaxHighlighter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import highlighting.presets.MiniJavaColours;
import org.antlr.v4.runtime.*;

    // TODO Phase III — AntlrTokenCollector (token-based syntax highlighting).

    // This highlighter uses the ANTLR-generated MiniJavaLexer to turn the input text into a token
    // stream. {@code collectMatches(String)} is the only method you need to implement:

    // - extract tokens of interest and
    // - map them to {@code HighlightRegions} using the colours from {@code // MiniJavaColours}.
    // - Sorting, filtering of invalid regions, and conflict handling are performed  by the
    //      base class {@code SyntaxHighlighter} via the
    //      template method {@code computeRegions(...)}.
public class AntlrTokenCollector extends SyntaxHighlighter {

  // TODO (Phase III — implement this method): Use the token stream produced by the ANTLR-generated
  // {@code MiniJavaLexer} to collect highlight regions.
  //
  // Requirements / hints:
  // - Iterate over the lexer tokens (typically via {@code CommonTokenStream}); ignore the EOF
  // token.

  // - For each token type that should be coloured (e.g., keywords, string/char literals, comments),
  // create a {@code HighlightRegion} with the corresponding colour from {@code MiniJavaColours}.

  // - Use {@code Token#getStartIndex()} and {@code Token#getStopIndex()} (inclusive) to compute
  // {@code [start, end)} ranges: {@code start = startIndex, end = stopIndex + 1}.

  // - Do not sort, merge, or resolve overlaps here; return all candidates as you find them.
  // Normalisation and conflict resolution are handled later by the template method.

  // - Annotation highlighting: colour '@' and the immediately following IDENTIFIER token (if
  // present).

  @Override
  public List<HighlightRegion> collectMatches(String text) {

      boolean previousWasAnnot = false;

      List<HighlightRegion> regionList = new ArrayList<>();

      var input = CharStreams.fromString(text);
      var lexer = new MiniJavaLexer(input);
      var tokens = new CommonTokenStream(lexer);

      tokens.fill();

      for (var t : tokens.getTokens()) {
          int start = t.getStartIndex();
          int end = t.getStopIndex()+1;

          switch(t.getType()){
              case MiniJavaLexer.PACKAGE:
              case MiniJavaLexer.IMPORT:
              case MiniJavaLexer.CLASS:
              case MiniJavaLexer.PUBLIC:
              case MiniJavaLexer.PRIVATE:
              case MiniJavaLexer.FINAL:
              case MiniJavaLexer.RETURN:
              case MiniJavaLexer.NULL:
              case MiniJavaLexer.NEW:
              case MiniJavaLexer.IF:
              case MiniJavaLexer.ELSE:
              case MiniJavaLexer.WHILE:
              case MiniJavaLexer.EXTENDS:
              case MiniJavaLexer.IMPLEMENTS:
                  HighlightRegion keywordRegion = new HighlightRegion(start, end, MiniJavaColours.KEYWORD_COLOUR);
                  regionList.add(keywordRegion);
                  break;
              case MiniJavaLexer.AT:
                  /*HighlightRegion annotRegion = new HighlightRegion(start, end, MiniJavaColours.ANNOTATION_COLOUR);
                  regionList.add(annotRegion);*/
                  previousWasAnnot = true;
                  break;
              case MiniJavaLexer.IDENTIFIER:
                  if(previousWasAnnot){
                      HighlightRegion overRegion = new HighlightRegion(start-1, end, MiniJavaColours.ANNOTATION_COLOUR);
                      regionList.add(overRegion);
                      previousWasAnnot = false;
                  }
                  break;
              case MiniJavaLexer.STRING_LITERAL:
                  HighlightRegion stringRegion = new HighlightRegion(start, end, MiniJavaColours.STRING_LITERAL_COLOUR);
                  regionList.add(stringRegion);
                  break;
              case MiniJavaLexer.CHAR_LITERAL:
                  HighlightRegion charRegion = new HighlightRegion(start, end, MiniJavaColours.CHAR_LITERAL_COLOUR);
                  regionList.add(charRegion);
                  break;
              case MiniJavaLexer.LINE_COMMENT:
                  HighlightRegion lineComRegion = new HighlightRegion(start,end, MiniJavaColours.LINE_COMMENT_COLOUR);
                  regionList.add(lineComRegion);
                  break;
              case MiniJavaLexer.BLOCK_COMMENT:
                  HighlightRegion blockComRegion = new HighlightRegion(start, end, MiniJavaColours.BLOCK_COMMENT_COLOUR);
                  regionList.add(blockComRegion);
                  break;
              case MiniJavaLexer.JAVADOC_COMMENT:
                  HighlightRegion docComRegion = new HighlightRegion(start, end, MiniJavaColours.JAVADOC_COMMENT_COLOUR);
                  regionList.add(docComRegion);
                  break;
          }
          /*var tokenName = MiniJavaLexer.VOCABULARY.getSymbolicName(t.getType());
          System.out.printf(
              "%-10s line=%d col=%d text='%s'%n",
              tokenName, t.getLine(), t.getCharPositionInLine(), t.getText());*/
      }



      return regionList;

    //throw new UnsupportedOperationException(tokens.getText());
  }
}
