/* The following code was generated by JFlex 1.4.1 on 27/09/06 22:13 */

/** Python 2.4 Lexical analyzer
 *
 *  Adapted from http://www.python.org/doc/current/ref/grammar.txt
 * 
 */
package oqube.jhighlight.python;

import java.io.Reader;
import java.io.IOException;
import com.uwyn.jhighlight.highlighter.ExplicitStateHighlighter;



/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.1
 * on 27/09/06 22:13 from the specification file
 * <tt>jhighlight-ext/src/main/java/oqube/jhighlight/python/Python</tt>
 */
public class PythonHighlighter implements ExplicitStateHighlighter {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 128;

  /** lexical states */
  public static final int IN_STRING1 = 1;
  public static final int YYINITIAL = 0;
  public static final int IN_STRING2 = 2;

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\10\27\2\27\1\44\1\27\1\27\1\45\22\27\1\10\1\60\1\25"+
    "\1\72\1\27\1\55\1\55\1\7\1\46\1\47\1\24\1\42\1\51"+
    "\1\42\1\40\1\54\1\32\7\35\2\2\1\46\1\46\1\56\1\52"+
    "\1\57\1\27\1\71\4\36\1\41\1\36\3\1\1\43\1\1\1\31"+
    "\5\1\1\5\2\1\1\6\2\1\1\34\2\1\1\46\1\26\1\46"+
    "\1\55\1\1\1\46\1\61\1\64\1\37\1\62\1\21\1\63\1\20"+
    "\1\13\1\16\1\43\1\67\1\30\1\22\1\17\1\14\1\65\1\1"+
    "\1\3\1\12\1\15\1\4\1\1\1\70\1\33\1\66\1\1\1\11"+
    "\1\42\1\23\1\53\41\27\1\50\137\27\uff00\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\3\0\1\1\1\2\1\3\3\2\1\1\1\4\7\2"+
    "\1\5\1\1\1\2\1\3\1\2\2\5\3\1\4\5"+
    "\1\1\7\2\2\1\2\3\1\0\1\3\1\0\1\3"+
    "\1\0\1\2\1\0\1\3\1\0\2\2\1\5\2\2"+
    "\1\6\4\2\1\0\1\2\1\0\1\3\1\0\2\2"+
    "\7\0\13\2\1\7\1\0\2\10\3\0\1\3\1\11"+
    "\1\0\7\2\1\12\1\2\1\3\1\2\2\0\1\6"+
    "\1\0\10\2\1\0\1\13\1\3\1\0\5\2\3\0"+
    "\3\2\1\0\3\2\3\0\1\2\24\0";

  private static int [] zzUnpackAction() {
    int [] result = new int[164];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\73\0\166\0\261\0\354\0\u0127\0\u0162\0\u019d"+
    "\0\u01d8\0\u0213\0\261\0\u024e\0\u0289\0\u02c4\0\u02ff\0\u033a"+
    "\0\u0375\0\u03b0\0\u03eb\0\u0426\0\u0461\0\u049c\0\u04d7\0\u0512"+
    "\0\u054d\0\u0588\0\u05c3\0\u05fe\0\261\0\u0639\0\u0674\0\u06af"+
    "\0\u054d\0\u06ea\0\u0725\0\u0760\0\u079b\0\u07d6\0\u0811\0\u084c"+
    "\0\u0887\0\u08c2\0\261\0\u08fd\0\u0938\0\u0973\0\u09ae\0\u09e9"+
    "\0\u0a24\0\u0a5f\0\u0a9a\0\u0ad5\0\u0b10\0\u0b4b\0\u0b86\0\354"+
    "\0\u0bc1\0\u0bfc\0\354\0\u0c37\0\u0c72\0\u0cad\0\u0ce8\0\u0d23"+
    "\0\u0d5e\0\u0d99\0\u0dd4\0\u0e0f\0\u0e4a\0\u0e85\0\u0ec0\0\u0efb"+
    "\0\u0f36\0\u0f71\0\u0fac\0\u0fe7\0\u054d\0\u1022\0\u105d\0\u1098"+
    "\0\u10d3\0\u110e\0\u1149\0\u1184\0\u11bf\0\u11fa\0\u1235\0\u1270"+
    "\0\u12ab\0\u08c2\0\261\0\u08c2\0\u12e6\0\u1321\0\u135c\0\u1397"+
    "\0\261\0\u13d2\0\u140d\0\u1448\0\u1483\0\u14be\0\u14f9\0\u1534"+
    "\0\u156f\0\261\0\u15aa\0\u15e5\0\u1620\0\u165b\0\u1696\0\261"+
    "\0\u16d1\0\u170c\0\u1747\0\u1782\0\u17bd\0\u17f8\0\u1833\0\u186e"+
    "\0\u18a9\0\u0887\0\261\0\u18e4\0\u191f\0\u195a\0\u1995\0\u19d0"+
    "\0\u1a0b\0\u1a46\0\u1a81\0\u1abc\0\u1af7\0\u1b32\0\u1b6d\0\u1ba8"+
    "\0\u1be3\0\u1c1e\0\u1c59\0\u1c94\0\u1ccf\0\u1d0a\0\u1d45\0\u1d80"+
    "\0\u1dbb\0\u1df6\0\u1e31\0\u1e6c\0\u1ea7\0\u1ee2\0\u1f1d\0\u1f58"+
    "\0\u1f93\0\u1fce\0\u2009\0\u2044\0\u207f\0\u20ba\0\u20f5\0\u2130"+
    "\0\u216b\0\u21a6\0\u21e1\0\u221c";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[164];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\4\1\5\1\6\1\7\1\10\1\11\1\10\1\12"+
    "\1\4\1\13\1\14\1\5\1\15\1\16\1\17\1\20"+
    "\1\21\1\22\1\5\1\13\1\23\1\24\2\4\1\25"+
    "\1\5\1\26\2\5\1\6\1\5\1\27\1\30\1\5"+
    "\1\31\1\5\1\4\1\32\1\13\1\33\1\34\1\4"+
    "\1\31\1\35\1\36\1\31\1\37\1\40\1\41\1\42"+
    "\1\43\1\44\1\45\1\46\1\47\1\5\1\50\1\51"+
    "\1\52\7\53\1\54\16\53\1\55\71\53\1\56\1\55"+
    "\44\53\74\0\6\5\3\0\11\5\5\0\10\5\1\0"+
    "\1\5\1\0\1\5\15\0\10\5\4\0\1\6\16\0"+
    "\1\57\6\0\2\53\1\6\2\0\1\6\2\0\1\60"+
    "\1\57\1\0\1\53\30\0\6\5\1\61\2\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\1\62"+
    "\7\5\3\0\2\5\1\11\1\5\1\11\1\5\1\61"+
    "\2\0\11\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\3\0\6\5\1\61\2\0\11\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\10\5\2\0"+
    "\7\63\1\64\15\63\1\0\1\65\15\63\2\0\25\63"+
    "\1\0\3\5\1\66\2\5\3\0\7\5\1\67\1\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\3\0\2\5\1\70\3\5\3\0\11\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\2\5"+
    "\1\71\3\5\3\0\11\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\1\70"+
    "\4\5\1\70\2\5\1\72\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\2\5\1\73\5\5\3\0\6\5"+
    "\3\0\2\5\1\74\6\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\11\5"+
    "\5\0\1\75\7\5\1\0\1\5\1\0\1\5\15\0"+
    "\10\5\3\0\6\5\3\0\11\5\5\0\1\76\2\5"+
    "\1\77\4\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\26\0\1\31\25\0\1\35\45\0\1\100\46\0\6\5"+
    "\3\0\11\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\1\101\7\5\4\0\1\102\16\0\1\57\6\0"+
    "\2\53\1\103\2\104\1\103\2\0\1\60\1\57\1\0"+
    "\1\53\30\0\6\5\3\0\2\5\1\105\6\5\5\0"+
    "\1\106\7\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\4\0\1\60\27\0\1\60\2\0\1\60\2\0\1\107"+
    "\104\0\1\35\64\0\1\4\76\0\1\13\25\0\1\110"+
    "\15\0\1\111\27\0\1\13\4\0\1\112\2\0\1\113"+
    "\1\0\1\114\61\0\1\35\1\0\1\35\70\0\1\35"+
    "\3\0\1\115\1\35\65\0\1\35\4\0\1\31\14\0"+
    "\6\5\3\0\1\116\4\5\1\117\3\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\7\5\1\120\1\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\2\5\1\121\3\5"+
    "\3\0\2\5\1\122\6\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\2\5\1\123\3\5"+
    "\3\0\11\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\3\0\2\5\1\124\3\5\3\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\1\125"+
    "\7\5\3\0\6\5\3\0\4\5\1\126\4\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\10\5\3\0"+
    "\6\5\3\0\1\5\1\127\2\5\1\130\4\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\10\5\3\0"+
    "\1\131\1\0\4\131\3\0\11\131\5\0\2\131\1\0"+
    "\2\131\1\0\2\131\1\0\1\131\1\0\1\131\15\0"+
    "\10\131\2\0\44\132\1\133\1\134\25\132\7\0\1\135"+
    "\64\0\72\53\25\0\1\136\55\0\1\137\31\0\1\137"+
    "\32\0\1\60\16\0\1\57\10\0\1\60\2\0\1\60"+
    "\3\0\1\57\1\0\1\53\27\0\7\63\1\140\15\63"+
    "\1\0\1\65\15\63\2\0\25\63\1\0\6\5\3\0"+
    "\4\5\1\76\4\5\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\2\0\7\63\1\53\15\63\1\0"+
    "\1\65\15\63\2\0\25\63\7\0\1\141\1\142\63\0"+
    "\72\63\1\0\6\5\3\0\11\5\5\0\10\5\1\0"+
    "\1\5\1\0\1\5\15\0\4\5\1\143\3\5\3\0"+
    "\6\5\3\0\11\5\5\0\1\144\7\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\5\5"+
    "\1\73\2\5\3\0\6\5\3\0\11\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\4\5\1\145\3\5"+
    "\3\0\6\5\3\0\3\5\1\70\5\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\2\5\1\146\6\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\1\147"+
    "\10\5\5\0\10\5\1\0\1\5\1\0\1\5\15\0"+
    "\10\5\3\0\6\5\3\0\7\5\1\150\1\5\5\0"+
    "\7\5\1\151\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\27\0\1\152\46\0\6\5\3\0\10\5\1\153\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\10\5\4\0"+
    "\1\102\16\0\1\57\10\0\1\102\2\0\1\102\2\0"+
    "\1\60\1\57\1\0\1\53\31\0\1\102\16\0\1\57"+
    "\6\0\2\53\1\103\2\0\1\103\2\0\1\60\1\57"+
    "\1\0\1\53\31\0\1\154\16\0\1\154\10\0\1\154"+
    "\2\0\3\154\1\0\1\154\17\0\4\154\7\0\6\5"+
    "\3\0\5\5\1\155\3\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\1\125"+
    "\7\5\42\0\1\35\53\0\1\156\101\0\1\157\120\0"+
    "\1\35\26\0\1\160\76\0\1\161\55\0\6\5\3\0"+
    "\1\162\10\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\3\0\6\5\3\0\11\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\1\5\1\70\6\5"+
    "\3\0\6\5\3\0\11\5\5\0\1\73\7\5\1\0"+
    "\1\5\1\0\1\5\15\0\2\5\1\73\5\5\3\0"+
    "\6\5\3\0\2\5\1\163\6\5\5\0\10\5\1\0"+
    "\1\5\1\0\1\5\15\0\10\5\3\0\2\5\1\73"+
    "\3\5\3\0\11\5\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\3\0\6\5\3\0\7\5\1\164"+
    "\1\5\5\0\10\5\1\0\1\5\1\0\1\5\15\0"+
    "\10\5\3\0\6\5\3\0\4\5\1\165\4\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\10\5\3\0"+
    "\6\5\3\0\1\166\10\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\7\5"+
    "\1\167\1\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\3\0\6\5\3\0\4\5\1\170\4\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\3\0\6\5\3\0\3\5\1\171\5\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\131"+
    "\3\0\11\131\5\0\10\131\1\172\1\131\1\0\1\131"+
    "\15\0\10\131\11\0\1\173\110\0\1\173\47\0\1\174"+
    "\27\0\1\174\2\0\1\174\45\0\1\142\73\0\1\175"+
    "\62\0\6\5\3\0\7\5\1\122\1\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\11\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\2\5\1\73\5\5\3\0\6\5\3\0\2\5"+
    "\1\176\6\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\3\0\6\5\3\0\11\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\3\5\1\177\4\5"+
    "\3\0\6\5\3\0\7\5\1\73\1\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\11\5\5\0\7\5\1\73\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\3\0\6\5\3\0\7\5\1\200"+
    "\1\5\5\0\10\5\1\0\1\5\1\0\1\5\15\0"+
    "\10\5\3\0\6\5\3\0\11\5\5\0\10\5\1\0"+
    "\1\5\1\0\1\5\15\0\3\5\1\201\4\5\4\0"+
    "\1\154\16\0\1\154\6\0\2\53\1\154\2\0\3\154"+
    "\1\0\1\154\17\0\4\154\7\0\6\5\3\0\3\5"+
    "\1\202\5\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\17\0\1\203\73\0\1\204\73\0\1\205"+
    "\54\0\6\5\3\0\7\5\1\176\1\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\10\5\1\73\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\3\0\6\5\3\0\11\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\1\206\7\5"+
    "\3\0\6\5\3\0\5\5\1\207\3\5\5\0\10\5"+
    "\1\0\1\5\1\0\1\5\15\0\10\5\3\0\6\5"+
    "\3\0\1\73\10\5\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\3\0\6\5\3\0\11\5\5\0"+
    "\1\210\7\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\3\0\6\5\3\0\11\5\5\0\1\147\7\5\1\0"+
    "\1\5\1\0\1\5\15\0\10\5\3\0\6\5\3\0"+
    "\1\5\1\73\7\5\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\4\0\1\174\27\0\1\174\2\0"+
    "\1\174\5\0\1\53\41\0\1\211\61\0\2\5\1\207"+
    "\3\5\3\0\11\5\5\0\10\5\1\0\1\5\1\0"+
    "\1\5\15\0\10\5\3\0\6\5\3\0\11\5\5\0"+
    "\10\5\1\0\1\5\1\0\1\5\15\0\1\212\7\5"+
    "\3\0\6\5\3\0\11\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\4\5\1\207\3\5\3\0\6\5"+
    "\3\0\11\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\1\5\1\213\6\5\3\0\6\5\3\0\4\5"+
    "\1\214\4\5\5\0\10\5\1\0\1\5\1\0\1\5"+
    "\15\0\10\5\6\0\1\215\151\0\1\160\70\0\1\216"+
    "\12\0\6\5\3\0\11\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\6\5\1\73\1\5\3\0\6\5"+
    "\3\0\3\5\1\73\5\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\3\0\6\5\3\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\1\5"+
    "\1\73\6\5\15\0\1\217\60\0\6\5\3\0\11\5"+
    "\5\0\1\73\7\5\1\0\1\5\1\0\1\5\15\0"+
    "\10\5\3\0\6\5\3\0\11\5\5\0\10\5\1\0"+
    "\1\5\1\0\1\5\15\0\1\73\7\5\3\0\6\5"+
    "\3\0\5\5\1\220\3\5\5\0\10\5\1\0\1\5"+
    "\1\0\1\5\15\0\10\5\5\0\1\221\117\0\1\222"+
    "\56\0\1\223\57\0\3\5\1\147\2\5\3\0\11\5"+
    "\5\0\10\5\1\0\1\5\1\0\1\5\15\0\10\5"+
    "\21\0\1\160\103\0\1\224\45\0\1\225\155\0\1\160"+
    "\21\0\1\226\67\0\1\227\75\0\1\230\60\0\1\231"+
    "\105\0\1\232\73\0\1\233\73\0\1\234\70\0\1\235"+
    "\71\0\1\236\76\0\1\237\73\0\1\240\73\0\1\241"+
    "\73\0\1\242\56\0\1\243\71\0\1\244\72\0\1\53"+
    "\63\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[8791];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\3\0\1\11\6\1\1\11\21\1\1\11\15\1\1\11"+
    "\1\1\1\0\1\1\1\0\1\1\1\0\1\1\1\0"+
    "\1\1\1\0\12\1\1\0\1\1\1\0\1\1\1\0"+
    "\2\1\7\0\14\1\1\0\1\11\1\1\3\0\1\1"+
    "\1\11\1\0\7\1\1\11\3\1\2\0\1\11\1\0"+
    "\10\1\1\0\1\11\1\1\1\0\5\1\3\0\3\1"+
    "\1\0\3\1\3\0\1\1\24\0";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[164];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the textposition at the last state to be included in yytext */
  private int zzPushbackPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /* user code: */

  public static final int COMMENT = 1;

  public static final int LITERAL = 2;

  public static final int DELIMITER = 3;

  public static final int PLAIN = 4;

  public static final int KEYWORD = 5;

  public static final int OPERATOR = 6;

  public static final int DECORATOR = 7;

  public static final int IDENTIFIER = 8;

  /* get line number */
  public int getLine() {
    return yyline;
  }
  
 /* set start line number */
  public void setLine(int sl) {
	yyline = sl;
  }

  public int getColumn() {
    return yycolumn;
  }

  public void setColumn(int col) {
    yycolumn = col;
  }
    
	/* Highlighter implementation */
	
	public int getStyleCount()
	{
		return 8;
	}
	
	public byte getStartState()
	{
		return YYINITIAL+1;
	}
	
	public byte getCurrentState()
	{
		return (byte) (yystate()+1);
	}
	
	public void setState(byte newState)
	{
		yybegin(newState-1);
	}
	
	public byte getNextToken()
	throws IOException
	{
		return (byte) yylex();
	}
	
	public int getTokenLength()
	{
		return yylength();
	}
	
	public void setReader(Reader r)
	{
		this.zzReader = r;
	}

	public PythonHighlighter()
	{
	}



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public PythonHighlighter(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public PythonHighlighter(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 174) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzPushbackPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead < 0) {
      return true;
    }
    else {
      zzEndRead+= numRead;
      return false;
    }
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = zzPushbackPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          zzR = false;
          break;
        case '\r':
          yyline++;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
          }
          break;
        default:
          zzR = false;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = zzLexicalState;


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 9: 
          { yybegin(IN_STRING1); return LITERAL;
          }
        case 12: break;
        case 7: 
          { return DECORATOR;
          }
        case 13: break;
        case 4: 
          { return  DELIMITER;
          }
        case 14: break;
        case 3: 
          { return LITERAL;
          }
        case 15: break;
        case 11: 
          { yybegin(YYINITIAL); return LITERAL;
          }
        case 16: break;
        case 6: 
          { return KEYWORD;
          }
        case 17: break;
        case 10: 
          { yybegin(IN_STRING2); return LITERAL;
          }
        case 18: break;
        case 5: 
          { return OPERATOR;
          }
        case 19: break;
        case 8: 
          { return COMMENT;
          }
        case 20: break;
        case 1: 
          { return PLAIN;
          }
        case 21: break;
        case 2: 
          { return IDENTIFIER;
          }
        case 22: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
            return YYEOF;
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
