package oqube.muse.trac;

import java.io.ByteArrayOutputStream;

import junit.framework.TestCase;
import oqube.muse.parser.MuseParser;

public class MuseTracSinkTest extends TestCase {

  private TracSink sink;
  private MuseParser parser;
  private ByteArrayOutputStream bos;

  protected void setUp() throws Exception {
    super.setUp();
    sink = new TracSink();
    sink.setTagHandler(new TracTagHandler());
    sink.setOut(bos = new ByteArrayOutputStream());
    sink.setLinker(new TracLinker());
    parser = new MuseParser();
    parser.setSink(sink);
  }

  public void testSplitLinesWithOneLine() {
    sink.setLineWidth(80);
    final String string = "a line with les than 80 chars";
    String[] lines = sink.splitLines(string);
    assertEquals(1, lines.length);
    assertEquals(lines[0], string);
  }

  public void testSplitLinesWithTwoLines() {
    sink.setLineWidth(80);
    final String string = "a line with more than 80 characters that is long" + " enough not to fit into the expected linewidth";
    String[] lines = sink.splitLines(string);
    assertEquals(2, lines.length);
    assertEquals("wrong line 1" + lines[0],
            "a line with more than 80 characters that is long" + " enough not to fit into the ", lines[0]);
    assertEquals("wrong line 2" + lines[1], "expected linewidth", lines[1]);
  }

  public void testSplitLinesWithVeryLongWord() {
    sink.setLineWidth(80);
    final String string = "alinewithmorethan0charactersthatslongenoughnottofitintotheexpectedlinewidth";
    String[] lines = sink.splitLines(string);
    assertEquals(1, lines.length);
    assertEquals(
            "alinewithmorethan0charactersthatslongenoughnottofitintotheexpectedlinewidth",
            lines[0]);

  }

  public void testSplitLinesWithLongTextAndLineSeparators() {
    sink.setLineWidth(80);
    final String string = "Le but de tout processus d'assurance qualité est de faire en sorte que\n" + "le nombre d'anomalies contenues dans chaque livraison du système soit\r" + "minimal. Ce nombre est par essence inconnaissable. Par contre, on peut connaitre" + "un certain nombre d'autres mesures qui fournissent un " + "indicateur indirect de ce nombre: ";
    String[] lines = sink.splitLines(string);
    assertEquals(5, lines.length);
    assertEquals(
            "minimal. Ce nombre est par essence inconnaissable. Par contre, on peut ",
            lines[2]);
  }

  public void testSplitLinesWithIndentation() {
    sink.setLineWidth(70);
    // starting a list increase indentation by one
    sink.start("list", null);
    sink.start("list", null);
    sink.start("list", null);
    sink.start("list", null);
    sink.start("list", null);
    // indentation should be 5
    // so linewidth = 65
    final String string = "a line with more than 80 characters that is long enough not to fit into the expected linewidth";
    String[] lines = sink.splitLines(string);
    assertEquals(2, lines.length);
    assertEquals("wrong line 1" + lines[0],
            "a line with more than 80 characters that is long enough not to ",
            lines[0]);
    assertEquals("wrong line 2" + lines[1], "fit into the expected linewidth",
            lines[1]);
  }

  public void testBasicMarkup() {
    parser.setStream(getClass().getResourceAsStream("/index.muse"));
    sink.start("document", null);
    parser.start();
    sink.end("document");
    sink.getOut().flush();
    // check content of output stream
    String out = new String(bos.toByteArray());
    System.err.println(out);

    assertTrue(out.contains("#author abailly@oqube.com"));
    assertTrue(out.contains("'''technologies ouvertes'''"));
    assertTrue(out.contains("''whitepapers'"));
    assertTrue(out.contains("=== Titre niveau 3 ==="));
    assertTrue(out.contains("[wiki:Journaljournal notes]"));
    assertTrue(out.contains("[[Image(test.png, alt=\"image\")]]"));
  }
}
