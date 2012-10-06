package oqube.muse.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import fr.lifl.utils.SubstitutableString;

public class Parser {

  public static final String EOL = System.getProperty("line.separator");

  public Parser next;

  public Parser more;

  private SubstitutableString content = new SubstitutableString("");

  private static final Pattern p = Pattern
      .compile("\\s*(?:(\\w+)|'(\\S+)|\"([^']+)\")\\s*");

  public Parser(String content, Parser next, Parser more) {
    this.content = new SubstitutableString(content);
    this.next = next;
    this.more = more;
  }

  private void initWith(String input) {
    Matcher m = p.matcher(input);
    String com = null;
    Parser tmp = null;
    while (m.find()) {
      if (m.group(1) != null)
        com = com == null ? m.group(1) : com + capitalize(m.group(1));
      else if (m.group(2) != null) {
        tmp = linkNext(m.group(2), com, tmp);
      } else if (m.group(3) != null) {
        tmp = linkNext(m.group(3), com, tmp);
      }
    }
    if (com != null)
      this.content = new SubstitutableString(com);
  }

  public Parser(BufferedReader reader) throws IOException {
    // create string from reader
    String line = reader.readLine();
    if (line != null) {
      initWith(line);
      try {
        this.more = new Parser(reader);
      } catch (IOException e) {
        this.more = null;
      }
    } else
      throw new IOException("No more input");
  }

  private Parser linkNext(String match, String com, Parser tmp) {
    if (com == null)
      this.content = new SubstitutableString(match);
    else if (tmp == null)
      this.next = tmp = new Parser(match, null, null);
    else {
      tmp.next = new Parser(match, null, null);
      tmp = tmp.next;
    }
    return tmp;
  }

  private String capitalize(String group) {
    if (group.length() > 0)
      return Character.toUpperCase(group.charAt(0)) + group.substring(1);
    else
      return group;
  }

  public String content() {
    return content.toString();
  }

  public String content(Map<String, String> variables) {
    return content.instance(variables);
  }

}
