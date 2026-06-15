package highlighting;

import highlighting.antlr.*;
import highlighting.core.SyntaxHighlighter;
import highlighting.presets.Texts;
import highlighting.regex.*;
import highlighting.ui.EditorUI;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public class Main {

  public static void main(String... args) {
    // Phase I: RegexHighlighter
    SyntaxHighlighter regex = new RegexHighlighter();

    // Phase II: ScanningHighlighter
    SyntaxHighlighter scanning = new ScanningHighlighter();

    // Phase III: AntlrTokenCollector (tokenbasiert)
    SyntaxHighlighter antlrToken = new AntlrTokenCollector();

    // and go ...
    EditorUI.show(Texts.START_TEXT, regex);
    EditorUI.show(Texts.START_TEXT, scanning);
    EditorUI.show(Texts.START_TEXT, antlrToken);


    //var testText1 = CharStreams.fromString("public class Foo { private int x; private int y;}");

    // Task 1.2 - Step 3
    System.out.print("Einrückung (Leerzeichen pro Stufe): ");
    int indentWidth = new java.util.Scanner(System.in).nextInt();
    var printer = new PrettyPrinterVisitor(indentWidth);

    // Task 1.2 - Step 1
    var input = CharStreams.fromString("public class Foo { private int x; private int y; public Foo(){this.y = null; this.x = null;} public int bar() { if(x) {{ y=x;} return null;} else { while(x <= y) { x = x+y; } return y; } } } ");

    var lexer = new MiniJavaLexer(input);
    var tokens = new CommonTokenStream(lexer);
    var parser = new MiniJavaParser(tokens);
    var tree = parser.compilationUnit(); // Wurzelknoten des Baums (Startregel der Grammatik)
    IO.println(tree.toStringTree(parser));
    printer.visit(tree);

    // Task 1.2 - Step 2
    String result = printer.result();
    System.out.println("\n--- Pretty Printed ---\n" + result);

      var printer2 = new PrettyPrinterVisitor(indentWidth);
      var testput = CharStreams.fromString("public class Bar{ public int foo() { while(x==y) x=x+y; return null; } } ");
      var lexer2 = new MiniJavaLexer(testput);
      var tokens2 = new CommonTokenStream(lexer2);
      var parser2 = new MiniJavaParser(tokens2);
      var tree2 = parser2.compilationUnit(); // Wurzelknoten des Baums (Startregel der Grammatik)
      printer2.visit(tree2);

      String result2 = printer2.result();
      System.out.println("\n--- Pretty Printed ---\n" + result2);


  }
}
