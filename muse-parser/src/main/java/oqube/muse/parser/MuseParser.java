package oqube.muse.parser;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import oqube.muse.MuseSink;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import fr.lifl.parsing.Namespace;
import fr.lifl.parsing.Parser;
import fr.lifl.parsing.ParserConfiguration;
import fr.lifl.parsing.ParserException;
import fr.lifl.parsing.ParserListener;
import fr.lifl.parsing.ParserListenerDelegate;
import fr.lifl.parsing.ParserPosition;

/**
 * Muse parser implementation. Current implementation is closed but future
 * evolutions shall allow some customization of parsing process.
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 */
public class MuseParser implements Parser {

  private static final String EOL = System.getProperty("line.separator");

  /* delegate handling of listeners */
  private ParserListenerDelegate delegate = new ParserListenerDelegate();

  /* character stream to use */
  private Reader reader;

  /* buffer storing pre-parsed data */
  private StringBuffer tagContent;

  /*
   * hold current tag name in tag mode. This allows nesting of tags inside tags
   */
  private String currentTag;

  private String[][] currentArgs;

  public String getCurrentTag() {
    return currentTag;
  }

  public void setCurrentTag(String currentTag, String[][] nv) {
    this.currentTag = currentTag;
    this.currentArgs = nv;
  }

  public Reader getReader() {
    return reader;
  }

  /* debug mode */
  private boolean debug = false;

  /* log instance */
  private static Log log = LogFactory.getLog(MuseParser.class);

  public static Log getLog() {
    return log;
  }

  public static void setLog(Log log) {
    MuseParser.log = log;
  }

  /* current state - always equal to top of stack */
  private State state;

  /* stacked states */
  private Stack<State> states = new Stack<State>();

  /* current sink */
  private MuseSink sink;

  public MuseSink getSink() {
    return sink;
  }

  public void setSink(MuseSink sink) {
    this.sink = strong;
    strong.setWrappedSink(sink);
  }

  /*
   * push state/col pair as new state
   */
  private State push(int st, int col) {
    states.push(state);

    if (state != null) {
      StringBuffer sb = new StringBuffer();
      for (int i = 0; i < col; i++)
        sb.append(' ');
      sb.append(state.state).append(" > ").append(st);
      if (log.isDebugEnabled())
        log.debug(sb);
    }
    return state = new State(st, col);
  }

  /*
   * Return old state
   */
  private State pop() {
    State s = state;
    state = (State) states.pop();
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < state.col; i++)
      sb.append(' ');
    sb.append(state.state).append(" < ").append(s.state);
    if (log.isDebugEnabled()) {
      log.debug(sb);
    }
    return s;
  }

  /* current position - for error messages and reporting */
  private ParserPosition pos;

  public ParserPosition getPos() {
    return pos;
  }

  public void setPos(ParserPosition pos) {
    this.pos = pos;
  }

  /**
   * Handles paragraph liens
   */
  private AbstractLexer paralex = new AbstractLexer("^(\\S+.*)$") {

    /*
     * Rules for paragraph: - start paragraph if not in paragraph. First end
     * enclosing env - continue paragraph if in paragraph
     * 
     */
    public void handler() {
      switch (state.state) {
      case para:
        sink.text(matcher.group(1) + "\n");
        break;
      case top:
        /* start new paragraph */
        sink.start("para", null);
        sink.text(matcher.group(1) + "\n");
        push(para, 0);
        break;
      case tab:
        pop();
        handler();
        break;
      case item:
        pop();
        sink.end("item");
        handler();
        break;
      case list:
        pop();
        sink.end("list");
        handler();
        break;
      case enums:
        pop();
        sink.end("enums");
        handler();
        break;
      case quote:
        pop();
        sink.end("quote");
        handler();
        break;
      case table:
        pop();
        sink.end("table");
        handler();
        break;
      case center:
        pop();
        sink.end("center");
        handler();
        break;
      case head:
        pop();
        sink.end("header");
        sink.start("body", null);
        handler();
        break;
      case tag:
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      default:
        throw new ParserException("Invalid paragraph line " + matcher.group(1)
            + " @(" + pos + ")");
      }
    }
  };

  abstract class ItemLexer extends AbstractLexer {

    ItemLexer(String pat) {
      super(pat);
    }

    protected void handle(int type) {
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      int c = matcher.group(1).length();
      if (c > state.col) {
        if (state.state == head && !fragment) {
          pop();
          sink.end("header");
          sink.start("body", null);
        }
        // new sub list
        push(type, c);
        push(item, c);
        if (type == list)
          sink.start("list", null);
        else if (type == enums)
          sink.start("enums", null);
        // ask for item content to be aligned with first line
        c += matcher.group(2).length();
        push(tab, c);
        sink.start("item", null);
        sink.text(matcher.group(3) + "\n");
      } else if (c < state.col) {
        State s = pop(); // old state
        switch (s.state) {
        case para:
          sink.end("para");
          break;
        case head:
          sink.end("header");
          sink.start("body", null);
          break;
        case tab:
          break;
        case item:
          sink.end("item");
          break;
        case list:
          sink.end("list");
          break;
        case enums:
          sink.end("enums");
          break;
        case quote:
          sink.end("quote");
          break;
        case center:
          sink.end("center");
        case table:
          sink.end("table");
          break;
        default:
          throw new ParserException("invalid indentation of list item "
              + matcher.group(0) + " @(" + pos + ")");
        }
        // recurse
        handler();
      } else {
        assert c == state.col;
        while (state.state != item) {
          switch (state.state) {
          case tab:
            pop();
            handler();
            return;
          default:
            throw new ParserException("Invalid indentation of list item "
                + matcher.group(0) + " at " + pos
                + ": should be at least one space further right");
          }
        }
        sink.end("item");
        c += matcher.group(2).length();
        push(tab, c);
        sink.start("item", null);
        sink.text(matcher.group(3) + "\n");
      }
    }
  };

  /* handle unordered lists items */
  private AbstractLexer listlex = new ItemLexer("(\\s+)(-\\s+)(.*)") {
    public void handler() {
      handle(list);
    }
  };

  /* handle ordered lists items */
  private AbstractLexer enumlex = new ItemLexer("(\\s+)(\\d+\\.\\s+)(.*)") {
    public void handler() {
      handle(enums);
    }
  };

  /* handle lines starting with blanks */
  private AbstractLexer blanklex = new AbstractLexer("(\\s+)(\\S.*)") {
    public void handler() {
      // count blanks
      int ws = matcher.group(1).length();
      switch (state.state) {
      case top:
      case item:
      case para:
      case tab:
        if (ws >= 6 + state.col) {// center
          sink.start("center", null);
          push(center, ws);
        } else if (ws > state.col) {
          sink.start("quote", null);
          push(quote, ws);
        }
        if (ws < state.col)
          throw new ParserException("Incorrect indentation line "
              + matcher.group(1) + "in state " + state + " @(" + pos + ")");
        sink.text(matcher.group(2).concat(EOL));
        break;
      case quote:
      case center:
        if (ws < state.col)
          throw new ParserException("Incorrect indentation line "
              + matcher.group(1) + "in state " + state + " @(" + pos + ")");
        sink.text(matcher.group(2).concat(EOL));
        break;
      case head:
        pop();
        sink.end("header");
        sink.start("body", null);
        handler();
        break;
      case tag:
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      default:
        throw new ParserException("Unexpected whitespace starting line "
            + matcher.group(1) + "in state " + state + " @(" + pos + ")");
      }
    }
  };

  /*
   * handle empty lines Empty liens terminate current block(s) and return parser
   * to toplevel state
   */
  private AbstractLexer emptylex = new AbstractLexer("^\\s*$") {
    public void handler() {
      while (state.state != top) {
        assert !states.isEmpty();
        // update state
        switch (state.state) {
        case para:
          sink.end("para");
          break;
        case list:
          sink.end("list");
          break;
        case quote:
          sink.end("quote");
          break;
        case center:
          sink.end("center");
          break;
        case enums:
          sink.end("enums");
          break;
        case item:
          sink.end("item");
          break;
        case head:
          sink.end("header");
          sink.start("body", null);
          break;
        case tasks:
          sink.end("tasks");
          break;
        case tab: // NOP
          break;
        case table:
          sink.end("table");
          break;
        case tag:
          tagContent.append(EOL).append(EOL);
          return;
        default:
          throw new ParserException("Unexpected empty line in state " + state
              + " @" + pos + "");
        }
        pop();
      }
      assert state.state == top;
    }
  };

  /* handle headers */
  private AbstractLexer headerlex = new AbstractLexer("^(\\*+)\\s+(\\S+.*)$") {

    public void handler() {
      switch (state.state) {
      case head:
        pop();
        sink.end("header");
        sink.start("body", null);
        break;
      case tag:
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      if (state.state != top)
        throw new ParserException("Cannot use header inside blocks: "
            + matcher.group(0) + " in state " + state.state + " @" + pos + "");
      /* count levels */
      int lvl = matcher.group(1).length();
      switch (lvl) {
      case 1:
        sink.start("title1", null);
        sink.text(matcher.group(2).trim());
        sink.end("title1");
        break;
      case 2:
        sink.start("title2", null);
        sink.text(matcher.group(2).trim());
        sink.end("title2");
        break;
      case 3:
        sink.start("title3", null);
        sink.text(matcher.group(2).trim());
        sink.end("title3");
        break;
      default: /* only four levels */
        sink.start("title4", null);
        sink.text(matcher.group(2).trim());
        sink.end("title4");
        break;
      }
    }
  };

  /*
   * handle Planner tasks format is: importance prioritiy <space> status space
   * text (link)
   */
  private AbstractLexer planlex = new AbstractLexer(
      "^#(A|B|C)([0-9]*) +(_|C|X) +(.*?) *(\\(\\[\\[(.*)\\]\\]\\))?+$") {

    public void handler() {
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      Map<String, String> params = new HashMap<String, String>() {
        {
          put("priority", matcher.group(1));
          put("level", matcher.group(2));
          put("status", matcher.group(3));
          put("description", matcher.group(4));
          put("link", matcher.group(6));
        }
      };
      switch (state.state) {
      case tasks:
        sink.start("task", params);
        sink.end("task");
        break;
      default:
        // start a new tasks
        sink.start("tasks", null);
        sink.start("task", params);
        sink.end("task");
        push(tasks, 0);
      }
    }
  };

  /* notes anchors in planner */
  private AbstractLexer notelex = new AbstractLexer("^\\.#([0-9]+)\\s+(\\S.*)$") {

    public void handler() {
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      Map<String, String> params = new HashMap<String, String>() {
        {
          put("id", matcher.group(1));
          put("title", matcher.group(2));
        }
      };
      // output note
      sink.start("note", params);
      sink.end("note");
    }
  };

  /* handle headers */
  private AbstractLexer metalex = new AbstractLexer("^#(\\S+)(:?\\s+(.*))?$") {

    public void handler() {
      switch (state.state) {
      case head:
        /* store key/value pair in sink */
        sink.addMetadata(matcher.group(1), matcher.group(3));
        break;
      case tag:
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      default:
        // group 3 should be empty and group 1 denotes an anchor
        if (matcher.group(3) == null || "".equals(matcher.group(3)))
          sink.anchor(matcher.group(1));
        else
          throw new ParserException("Cannot use meta data inside blocks: "
              + matcher.group(0) + " @(" + pos + ")");
      }
    }
  };

  /* handle separator - at least four dashes */
  private AbstractLexer sepalex = new AbstractLexer("^----+") {

    public void handler() {
      switch (state.state) {
      case top:
        sink.separator();
        break;
      case tag:
        tagContent.append(matcher.group(0));
        return;
      default:
        throw new ParserException("Cannot use separator inside blocks: "
            + matcher.group(0) + " @" + pos + "");
      }
    }
  };

  /* handle block tags - can use tags inside tags */
  private AbstractLexer sttaglex = new AbstractLexer(
      "^\\s*<(\\w+)((?:\\s+\\w+=\"[^\"]+\")*)\\s*>\\s*$") {

        @Override
    public void handler() {
      // check nested tags
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      String tg = matcher.group(1);
      // extract tag attributes
      String at = matcher.group(2);
      String[][] nv = new String[0][]; // attributes array
      if (at != null && !"".equals(at)) {
        String[] attrs = at.trim().split("\\s+");
        nv = new String[attrs.length][];
        for (int i = 0; i < attrs.length; i++) {
          nv[i] = attrs[i].split("=");
          // remove quotes
          nv[i][1] = nv[i][1].substring(1, nv[i][1].length() - 1);
        }
      }
      setCurrentTag(tg, nv);
      push(tag, 0);
      tagContent = new StringBuffer();
    }
  };

  private AbstractLexer endtaglex = new AbstractLexer("^\\s*</(\\w+)\\s*>\\s*$") {

    public void handler() {
      String tg = matcher.group(1);
      // check nested tags
      if(getCurrentTag() == null)
          throw new ParserException("Found end tag "+ tg + " without matching start tag");
      if (!getCurrentTag().equals(tg)) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      if (state.state != tag)
        throw new ParserException("Found ending tag " + tg + " in state "
            + state.state + " @" + pos);
      pop();
      /* store key/value pair in sink */
      sink.block(tg, currentArgs, tagContent.toString());
    }
  };

  private AbstractLexer tableHdrLex = new AbstractLexer("^[^|]+(\\|\\|[^|]+)+$") {

    public void handler() {
      String th = matcher.group(0);
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      if (state.state == table)
        throw new ParserException("Cannot nest headers inside table");
      push(table, state.col);
      sink.start("table", null);
      /* parse headers text */
      sink.start("tableRow", null);
      String[] hdrs = th.split("\\|\\|");
      for (int i = 0; i < hdrs.length; i++) {
        sink.start("tableHeader", null);
        sink.text(hdrs[i]);
        sink.end("tableHeader");
      }
      sink.end("tableRow");
    }

  };

  private AbstractLexer tableDataLex = new AbstractLexer("^[^|]+(\\|[^|]*)+$") {

    public void handler() {
      String th = matcher.group(0);
      if (state.state == tag) {
        tagContent.append(matcher.group(0)).append(EOL);
        return;
      }
      if (state.state != table) {
        // need not have headers
        push(table, state.col);
        sink.start("table", null);
      }
      /* parse headers text */
      sink.start("tableRow", null);
      String[] hdrs = th.split("\\|");
      for (int i = 0; i < hdrs.length; i++) {
        sink.start("tableData", null);
        sink.text(hdrs[i]);
        sink.end("tableData");
      }
      sink.end("tableRow");
    }

  };

  /* states */
  private static final int top = 0;

  private static final int para = 1;

  private static final int list = 2;

  private static final int item = 3;

  private static final int quote = 4;

  private static final int center = 5;

  private static final int enums = 6;

  private static final int tab = 7; // for aligning text inside lists

  private static final int head = 8;

  private static final int foot = 9;

  private static final int tag = 10;

  private static final int table = 11;

  private static final int tasks = 12;

  // private static final int enums = blocks.add("(\\s+)\\d+\\.\\s+(.*)");

  /* patterns for formatting */
  private FlowLexer emph = new FlowLexer("\\*([^*]+)\\*") {
    public void handler() {
      start("emph", null);
      /* pass it over to next lexer */
      assert next != null;
      ((FlowLexer) next).format(matcher.group(1));
      end("emph");
    }
  };

  private FlowLexer strong = new FlowLexer("\\*\\*([^*]+)\\*\\*") {
    public void handler() {
      start("strong", null);
      /* pass it over to next lexer */
      assert next != null;
      ((FlowLexer) next).format(matcher.group(1));
      end("strong");
    }
  };

  private FlowLexer verb = new FlowLexer("\\=([^=]+)\\=") {
    public void handler() {
      start("verb", null);
      /* pass it over to next lexer */
      assert next != null;
      ((FlowLexer) next).format(matcher.group(1));
      end("verb");
    }
  };

  private FlowLexer math = new FlowLexer("\\$([^$]+)\\$") {
    public void handler() {
      start("math", null);
      getWrappedSink().rawText(matcher.group(1));
      end("math");
    }
  };

  private FlowLexer uline = new FlowLexer("_([^_]+)_") {
    public void handler() {
      start("uline", null);
      /* pass it over to next lexer */
      assert next != null;
      ((FlowLexer) next).format(matcher.group(1));
      end("uline");
    }
  };

  private FlowLexer link = new FlowLexer(
      "\\[\\[([^]]*)\\](?:\\[([^]]*)\\])?\\]") {
    public void handler() {
      link(matcher.group(1), matcher.group(2));
    }
  };

  private FlowLexer tagflow = new FlowLexer("<([^</>]+)>([^</>]+)</[^</>]+>") {
    public void handler() {
      flow(matcher.group(1), null, matcher.group(2));
    }
  };

  private boolean fragment;

  /**
   * Sets the configuration object to be used by this parser
   * 
   * @param config
   *          F/ public void setParserConfiguration(ParserConfiguration config) { }
   * 
   * /** Sets the reader object providing the stream of characters to parse. The
   * reader is not closed by this method
   * 
   * @param reader
   *          a valid Reader object
   */
  public void setReader(Reader reader) {
    this.reader = reader;
  }

  /**
   * Set an input stream to be parsed by this parser. This input stream will be
   * wrapped in a reader using default locale settings.
   * 
   * @param is
   *          the stream. may not be null.
   */
  public void setStream(InputStream is) {
    this.reader = new InputStreamReader(is);
  }

  /**
   * Adds a listener for parse events to this Parser. Parsing events are
   * generated by the Parser to notify listeners of warnings and recoverable
   * errors. Unrecoverable errors are notified through
   * {@see fr.lifl.util.ParserException}.
   * 
   * @param listener
   *          the listener to add to this parser
   */
  public void addParserListener(ParserListener listener) {
    delegate.addParserListener(listener);
  }

  /**
   * Gives information to this parser that parsing starts at given position in
   * the enclosing context. If this method is not called prior to a call to
   * {@see #start()} method, start position is assumed to be line 1, column 1.
   * 
   * @param pos
   *          the start position - may not be null
   */
  public void setStartPosition(ParserPosition pos) {
    this.pos = pos;
  }

  /**
   * Gives the Parser information of the enclosing Namespace this parsing is
   * part of.
   * 
   */
  public void setStartScope(Namespace scope) {
  }

  /**
   * Default Constructor.
   */
  public MuseParser() {
    // link block lexers
    headerlex.setNext(tableHdrLex).setNext(tableDataLex).setNext(sttaglex)
        .setNext(endtaglex).setNext(emptylex).setNext(sepalex).setNext(listlex)
        .setNext(enumlex).setNext(blanklex).setNext(planlex).setNext(notelex)
        .setNext(metalex).setNext(paralex);
    // link flow lexers
    strong.setNext(link).setNext(math).setNext(emph).setNext(verb).setNext(
        uline).setNext(tagflow).setNext(IdentityLexer.instance);
  }

  class MuseParserException extends ParserException {
    MuseParserException(String line, Exception cause) {
      super("In " + line + ": " + cause.getMessage(), cause);
    }
  }

  /**
   * Asks this Parser to start parsing. This method is normally blocking and
   * Parser should return when finished. This method must be called after a call
   * to {@see #setReader(java.io.Reader)} or else it will throw immediatly a
   * ParserException.
   * <p>
   * Recoverable parse events are notified through registered ParserListener
   * interface, while non recoverable errors throw a PArserException.
   * 
   * @throws ParserException
   */
  public void start() throws ParserException {
    String line = null;
    BufferedReader br = new BufferedReader(reader);
    if (pos == null)
      pos = new ParserPosition(1, 1);
    else {
      pos.setLine(1);
      pos.setColumn(1);
    }
    push(top, 0);
    if (!fragment) {
      push(head, 0);
      sink.start("header", null);
    }
    /*
     * parse lines to create blocks then parse blocks to add formatting and
     * resolve link
     */
    try {
      while ((line = br.readLine()) != null) {
        parse(line);
        pos.setLine(pos.getLine() + 1);
      }
      // call empty lex at end
      if (state.state != top)
        emptylex.handler();
      assert state.state == top;
      if (!fragment) {
        sink.start("footer", null);
        sink.end("footer");
        sink.end("body");
      }
    } catch (Exception e) {
      throw new MuseParserException(line, e);
    }
  }

  /**
   * @param line
   */
  public void parse(String line) {
    if (log.isDebugEnabled())
      log.debug("Parsing line " + line + " @" + pos);
    headerlex.parse(line);
    if (log.isDebugEnabled())
      log.debug("Done");
  }

  /**
   * @return Returns the currentArgs.
   */
  public String[][] getCurrentArgs() {
    return currentArgs;
  }

  public void setFragment(boolean b) {
    this.fragment = b;
  }

  public void setParserConfiguration(ParserConfiguration arg0) {
    // TODO Auto-generated method stub

  }

}
