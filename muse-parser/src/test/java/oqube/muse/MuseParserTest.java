/*
 * ______________________________________________________________________________
 * 
 * Copyright 2006 Arnaud Bailly -
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * (1) Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * (2) Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * (3) The name of the author may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created on Tue Jul 11 2006
 * 
 */
package oqube.muse;

import java.io.StringReader;
import java.util.Map;

import oqube.muse.parser.MuseParser;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

/**
 * Test cases for muse parser.
 * 
 * @author abailly@oqube.com
 * @version $Id:
 *          /local/muse-parser/oqube/muse-parser/src/test/java/oqube/muse/MuseParserTest.java
 *          1276 2007-11-01T19:57:16.936009Z nono $
 */
public class MuseParserTest extends MockObjectTestCase {

  private static final String EOL = System.getProperty("line.separator");

  private Mock mock;

  private MuseParser parser;

  protected void setUp() throws Exception {
    super.setUp();
    // create dummy sink
    mock = mock(MuseSink.class);
    parser = new MuseParser();
    parser.setSink((MuseSink) mock.proxy());
    mock.expects(once()).method("start").with(eq("header"), ANYTHING);
    mock.expects(once()).method("end").with(eq("header"));
    mock.expects(once()).method("start").with(eq("footer"), ANYTHING);
    mock.expects(once()).method("end").with(eq("footer"));
    mock.expects(once()).method("start").with(eq("body"), ANYTHING);
    mock.expects(once()).method("end").with(eq("body"));
  }

  public void test01para() {
    String s = "This is a paragraph blokr" + EOL
        + "anethro line in the paragraphh" + EOL + EOL + "another para" + EOL
        + "with two lines";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(2)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("para"));
    parser.start();
  }

  public void test02List() {
    String s = "This is a paragraph blokr" + EOL + "  - a list item" + EOL
        + "  -   a second item  continuing on one line" + EOL
        + "  - another list item" + EOL + "    continuing on one line" + EOL
        + EOL + "a new para";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(exactly(5)).method("text");
    mock.expects(exactly(2)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("para"));
    mock.expects(exactly(3)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("item"));
    mock.expects(exactly(1)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("list"));
    parser.start();
    // sk.getOut().flush();
  }

  public void test03Quote() {
    String s = "   This is a quoted paragraph blokr" + EOL
        + "    the number of blanks afetr firts line is irrelevant" + EOL
        + "    continuing on one lin";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(exactly(1)).method("text");
    mock.expects(exactly(1)).method("start").with(eq("quote"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("quote"));
    parser.start();
  }

  public void test04Centering() {
    String s = "      This is a centered paragraph block" + EOL
        + "        the number of blanks afetr firts line is irrelevant" + EOL
        + "        continuing on one lin";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(exactly(1)).method("text");
    mock.expects(exactly(1)).method("start").with(eq("center"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("center"));
    parser.start();
  }

  public void test05Headers() {
    String s = "* level1" + EOL + "** level2" + EOL + "*** level3" + EOL
        + "**** level4" + EOL + "***** leveln" + EOL;
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(atLeastOnce()).method("text");
    mock.expects(once()).method("start").with(eq("title1"), ANYTHING);
    mock.expects(once()).method("end").with(eq("title1"));
    mock.expects(once()).method("start").with(eq("title2"), ANYTHING);
    mock.expects(once()).method("end").with(eq("title2"));
    mock.expects(once()).method("start").with(eq("title3"), ANYTHING);
    mock.expects(once()).method("end").with(eq("title3"));
    mock.expects(exactly(2)).method("start").with(eq("title4"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("title4"));
    parser.start();
  }

  public void test06Meta() {
    String s = "#title title" + EOL;
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    // MuseHTMLSink sk = new MuseHTMLSink();
    // sk.setOut(new PrintWriter(System.out));
    // parser.setSink(sk);
    mock.expects(once()).method("addMetadata").with(eq("title"), eq("title"));
    parser.start();
  }

  public void test07Emph() {
    String s = " This is a quoted paragraph *blokr" + EOL
        + " the number of blanks* afetr firts line is irrelevant" + EOL
        + "        continuing on one lin";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(exactly(1)).method("start").with(eq("quote"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("quote"));
    mock.expects(exactly(3)).method("text");
    mock.expects(exactly(1)).method("start").with(eq("emph"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("emph"));
    parser.start();
  }

  public void test08Verb() {
    String s = " This is a quoted paragraph blokr" + EOL
        + " the number of blanks =afetr firts line is irrelevant-" + EOL
        + "        continuing on one lin=";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(exactly(1)).method("start").with(eq("quote"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("quote"));
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(1)).method("start").with(eq("verb"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("verb"));
    parser.start();
  }

  public void test09Strong() {
    String s = "This is **a quoted parag**";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(exactly(1)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("para"));
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(1)).method("start").with(eq("strong"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("strong"));
    parser.start();
  }

  public void test10Math() {
    String s = "this is a $paragrpha$ with strong emphaisis in it";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("end").with(eq("para"));
    mock.expects(exactly(2)).method("text");
    mock.expects(exactly(1)).method("rawText").with(eq("paragrpha"));
    mock.expects(once()).method("start").with(eq("math"), ANYTHING);
    mock.expects(once()).method("end").with(eq("math"));
    parser.start();
    mock.verify();
  }

  public void test11Uline() {
    String s = "this is a paragrpha with _strong emphaisis_ in it";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("end").with(eq("para"));
    mock.expects(exactly(3)).method("text");
    mock.expects(once()).method("start").with(eq("uline"), ANYTHING);
    mock.expects(once()).method("end").with(eq("uline"));
    parser.start();
    mock.verify();
  }

  public void test12Emph() {
    String s = "this is a paragrpha with *light " + EOL + "emphaisis* in it";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("end").with(eq("para"));
    mock.expects(exactly(3)).method("text");
    mock.expects(once()).method("start").with(eq("emph"), ANYTHING);
    mock.expects(once()).method("end").with(eq("emph"));
    parser.start();
    mock.verify();
  }

  public void test13ListInList() {
    String s = "this is a paragrpha with:" + EOL + "  - a first item" + EOL
        + "  - a second item" + EOL + "    - a nested item" + EOL
        + "    - a nested item" + EOL + "  - a first level item" + EOL
        + "emphaisis* in it";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("end").with(eq("para"));
    // two lists
    mock.expects(exactly(2)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("list"));
    // five items
    mock.expects(exactly(5)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(5)).method("end").with(eq("item"));
    // lot of text
    mock.expects(exactly(7)).method("text");
    parser.start();
    mock.verify();
  }

  public void test14EnumInList() {
    String s = "this is a paragrpha with:" + EOL + "  - a first item" + EOL
        + "  - a second item" + EOL + "    1. a enum nested item" + EOL
        + "    2. a seoncd enum nested item" + EOL + "  - a first level item"
        + EOL + "emphaisis* in it";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("end").with(eq("para"));
    // one list
    mock.expects(exactly(1)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("list"));
    // one enum
    mock.expects(exactly(1)).method("start").with(eq("enums"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("enums"));
    // five items
    mock.expects(exactly(5)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(5)).method("end").with(eq("item"));
    // lot of text
    mock.expects(exactly(7)).method("text");
    parser.start();
    mock.verify();
  }

  public void test15Tags() {
    String s = "<tag>  " + EOL + "  - a first item" + EOL + "  - a second item"
        + EOL + "<dummy>" + EOL + "1. a enum nested item" + EOL
        + "    2. a seoncd enum nested item" + EOL + "  - a first level item"
        + EOL + "</tag>";
    parser.setReader(new StringReader(s));
    mock.expects(once()).method("block").with(eq("tag"), ANYTHING, ANYTHING);
    parser.start();
    mock.verify();
  }

  /*
   * public void test16paraAfterList() {
   * parser.setStream(getClass().getResourceAsStream(
   * "/test16paraAfterList.muse"));
   * mock.expects(exactly(2)).method("start").with(eq("para",ANYTHING);
   * mock.expects(exactly(2)).method("end").with(eq("para"); // one list
   * mock.expects(exactly(1)).method("start").with(eq("List",ANYTHING);
   * mock.expects(exactly(1)).method("end").with(eq("List");
   * mock.expects(exactly(2)).method("start").with(eq("Item",ANYTHING);
   * mock.expects(exactly(2)).method("end").with(eq("Item");
   * mock.expects(exactly(4)).method("text"); parser.start(); }
   */
  public void test17HRpara() {
    parser.setStream(getClass().getResourceAsStream("/test17HCPara.muse"));
    mock.expects(exactly(2)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("para"));
    mock.expects(exactly(1)).method("separator");
    // one list
    mock.expects(exactly(2)).method("text");
    parser.start();
  }

  public void test18Headerpara() {
    parser.setStream(getClass().getClassLoader().getResourceAsStream(
        "test18HeaderPara.muse"));
    mock.expects(exactly(3)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("para"));
    mock.expects(exactly(1)).method("start").with(eq("title1"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("title1"));
    mock.expects(exactly(1)).method("start").with(eq("title2"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("title2"));
    // one list
    mock.expects(exactly(5)).method("text");
    parser.start();
  }

  public void test19table1() {
    parser.setStream(getClass().getResourceAsStream("/test19Table1.muse"));
    mock.expects(exactly(1)).method("start").with(eq("table"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("table"));
    mock.expects(exactly(4)).method("start").with(eq("tableRow"), ANYTHING);
    mock.expects(exactly(4)).method("end").with(eq("tableRow"));
    mock.expects(exactly(3)).method("start").with(eq("tableHeader"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("tableHeader"));
    mock.expects(exactly(9)).method("start").with(eq("tableData"), ANYTHING);
    mock.expects(exactly(9)).method("end").with(eq("tableData"));
    mock.expects(exactly(12)).method("text");
    parser.start();
  }

  public void test20table2() {
    parser.setStream(getClass().getResourceAsStream("/test19Table2.muse"));
    mock.expects(exactly(1)).method("start").with(eq("table"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("table"));
    mock.expects(exactly(3)).method("start").with(eq("tableRow"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("tableRow"));
    mock.expects(exactly(9)).method("start").with(eq("tableData"), ANYTHING);
    mock.expects(exactly(9)).method("end").with(eq("tableData"));
    mock.expects(exactly(9)).method("text");
    parser.start();
  }

  public void test21table3() {
    parser.setStream(getClass().getResourceAsStream("/test19Table3.muse"));
    mock.expects(exactly(1)).method("start").with(eq("table"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("table"));
    mock.expects(exactly(1)).method("start").with(eq("tableRow"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("tableRow"));
    mock.expects(exactly(4)).method("start").with(eq("tableHeader"), ANYTHING);
    mock.expects(exactly(4)).method("end").with(eq("tableHeader"));
    mock.expects(exactly(4)).method("text");
    parser.start();
  }

  public void test22tableInItem() {
    parser.setStream(getClass().getResourceAsStream("/test19TableInItem.muse"));
    mock.expects(exactly(1)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("list"));
    mock.expects(exactly(3)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("item"));
    // lot of text
    mock.expects(exactly(1)).method("start").with(eq("table"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("table"));
    mock.expects(exactly(4)).method("start").with(eq("tableRow"), ANYTHING);
    mock.expects(exactly(4)).method("end").with(eq("tableRow"));
    mock.expects(exactly(3)).method("start").with(eq("tableHeader"), ANYTHING);
    mock.expects(exactly(3)).method("end").with(eq("tableHeader"));
    mock.expects(exactly(9)).method("start").with(eq("tableData"), ANYTHING);
    mock.expects(exactly(9)).method("end").with(eq("tableData"));
    mock.expects(exactly(15)).method("text");
    parser.start();
  }

  public void test23InlineTag() {
    String s = "This is a <code>quoted</code> paragraph blokr" + EOL
        + "the number of blanks afetr firts line is <code>irrelevant-" + EOL
        + "continuing</code> on one lin";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(exactly(1)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("para"));
    mock.expects(exactly(3)).method("text");
    mock.expects(exactly(2)).method("flow");
    parser.start();
  }

  public void test24InlineTagInList() {
    String s = " - This is a <code>quoted</code> paragraph blokr" + EOL
        + " - the number of blanks afetr firts line is <code>irrelevant-" + EOL
        + "   continuing</code> on one lin";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(exactly(1)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("list"));
    mock.expects(exactly(2)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("item"));
    mock.expects(exactly(4)).method("text");
    mock.expects(exactly(2)).method("flow");
    parser.start();
  }

  public void test25ComplexLink() {
    String s = "[[http://www.amazon.fr/Queueing-Networks-Markov-Chains-Applications/dp/0471565253/sr=8-1/qid=1168611162/ref=sr_1_1/171-9516910-2255453?ie=UTF8&s=english-books][QNMC]]";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock
        .expects(exactly(1))
        .method("link")
        .with(
            eq("http://www.amazon.fr/Queueing-Networks-Markov-Chains-Applications/dp/0471565253/sr=8-1/qid=1168611162/ref=sr_1_1/171-9516910-2255453?ie=UTF8&s=english-books"),
            eq("QNMC"));
    mock.expects(exactly(1)).method("start").with(eq("para"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("para"));
    parser.start();

  }

  public void testParsingTasksInPlannerFile() {
    parser.setStream(getClass().getResourceAsStream("/testPlanner1.muse"));
    String[][] data = {
        {
            "A",
            "0",
            "_",
            "idée de site web d'audit des projets OS [[Creation#16][(16)]] [[Creation#20][(20)]]",
            "2007.11.02" },
        { "A", "0", "C",
            "@14:00 préparer réunion SB + PF [[2006.04.07#1][(1)]]",
            "2006.04.07" },
        { "A", "0", "X", "notification congé création", "2006.04.03" },
        { "B", "0", "C", "renvoyer cv modifié à free-expert", "2006.10.10" },
        { "A", "0", "X", "@11:00 RV Sandrine Faust Paris : [[RNTL]]",
            "2006.03.29" } };
    mock.expects(exactly(5)).method("start").with(eq("task"),
        new Checktask(data));
    mock.expects(exactly(5)).method("end").with(eq("task"));
    mock.expects(exactly(2)).method("start").with(eq("tasks"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("tasks"));
    parser.start();
  }

  public void testParsingTasksWithoutLinks() {
    parser.setStream(getClass().getResourceAsStream("/testPlanner3.muse"));
    String[][] data = {
        {
            "A",
            "",
            "_",
            "idée de site web d'audit des projets OS [[Creation#16][(16)]] [[Creation#20][(20)]]",
            null },
        { "A", "", "C",
            "@14:00 préparer réunion SB + PF [[2006.04.07#1][(1)]]", null } };
    mock.expects(exactly(2)).method("start").with(eq("task"),
        new Checktask(data));
    mock.expects(exactly(2)).method("end").with(eq("task"));
    mock.expects(exactly(1)).method("start").with(eq("tasks"), ANYTHING);
    mock.expects(exactly(1)).method("end").with(eq("tasks"));
    parser.start();
  }

  public void testParsingNotesInPlannerFiles() {
    parser.setStream(getClass().getResourceAsStream("/testPlanner2.muse"));
    String[][] data = { { "21", "RV Miguel Cardoso" },
        { "20", "RV Eddie Kasprszak" } };
    mock.expects(exactly(2)).method("start").with(eq("note"),
        new Checknote(data));
    mock.expects(exactly(2)).method("end").with(eq("note"));
    mock.expects(exactly(2)).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("list"));
    mock.expects(exactly(6)).method("end").with(eq("item"));
    mock.expects(exactly(6)).method("start").with(eq("item"), ANYTHING);
    mock.expects(exactly(6)).method("text");
    parser.start();
  }

  public void testTitleShouldBeTrimmed() {
    String s = "* this is a title" + EOL + EOL + "this is some text";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(once()).method("end").with(eq("title1"));
    mock.expects(once()).method("end").with(eq("para"));
    mock.expects(once()).method("start").with(eq("title1"), ANYTHING);
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("text").with(eq("this is a title"));
    mock.expects(once()).method("text").with(eq("this is some text "));
    parser.start();
  }

  public void testEOLShouldBeReplacedBySpaceWithinFlow() {
    String s = "a text" + EOL + "this is some text";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(once()).method("end").with(eq("para"));
    mock.expects(once()).method("start").with(eq("para"), ANYTHING);
    mock.expects(once()).method("text").with(eq("a text this is some text "));
    parser.start();
  }

  public void testEOLShouldBeReplacedBySpaceWithinList() {
    String s = " - a text" + EOL + "   this is some text";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(once()).method("end").with(eq("list"));
    mock.expects(once()).method("start").with(eq("list"), ANYTHING);
    mock.expects(once()).method("end").with(eq("item"));
    mock.expects(once()).method("start").with(eq("item"), ANYTHING);
    mock.expects(once()).method("text").with(eq("a text this is some text "));
    parser.start();
  }

  public void testEOLShouldBeReplacedBySpaceWithinEnums() {
    String s = " 2. dans cet autre message, Laurent Bossavit fait remarquer que les"
        + EOL
        + "    simulacres ne sont pas nécessaires quand le langage permet de"
        + EOL + "    redéfinir le comportement d'un objet.";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(once()).method("end").with(eq("enums"));
    mock.expects(once()).method("start").with(eq("enums"), ANYTHING);
    mock.expects(once()).method("end").with(eq("item"));
    mock.expects(once()).method("start").with(eq("item"), ANYTHING);
    mock.expects(once()).method("text").with(
        eq("dans cet autre message, Laurent Bossavit fait remarquer "
            + "que les simulacres ne sont pas nécessaires quand le "
            + "langage permet de redéfinir le comportement d'un objet. "));
    parser.start();
  }

  public void testEOLShouldBeReplacedBySpaceWithinNextItemsInList() {
    String s = " - a text" + EOL + "   this is some text" + EOL + " - this is"
        + EOL + "   another item";
    StringReader rd = new StringReader(s);
    parser.setReader(rd);
    mock.expects(once()).method("end").with(eq("list"));
    mock.expects(once()).method("start").with(eq("list"), ANYTHING);
    mock.expects(exactly(2)).method("end").with(eq("item"));
    mock.expects(exactly(2)).method("start").with(eq("item"), ANYTHING);
    mock.expects(once()).method("text").with(eq("a text this is some text "));
    mock.expects(once()).method("text").with(eq("this is another item "));
    parser.start();
  }

  private class Checktask implements Constraint {

    private String[][] data;

    private int idx = 0;

    public Checktask(String[][] data) {
      this.data = data;
    }

    @SuppressWarnings("unchecked")
    public boolean eval(Object arg0) {
      Map<String, String> params = (Map<String, String>) arg0;
      StringBuffer comp = new StringBuffer();
      comp.append("comparing expected " + data[idx][0] + " to actual "
          + params.get("priority"));
      comp.append("comparing expected " + data[idx][1] + " to actual "
          + params.get("level"));
      comp.append("comparing expected " + data[idx][2] + " to actual "
          + params.get("status"));
      comp.append("comparing expected " + data[idx][3] + " to actual "
          + params.get("description"));
      comp.append("comparing expected " + data[idx][4] + " to actual "
          + params.get("link"));
      System.err.println(comp);
      boolean ret = data[idx][0].equals(params.get("priority"));
      ret &= data[idx][1].equals(params.get("level"));
      ret &= data[idx][2].equals(params.get("status"));
      ret &= data[idx][3].equals(params.get("description"));
      ret &= data[idx][4] == null ? params.get("link") == null : data[idx][4]
          .equals(params.get("link"));
      idx++;
      return ret;
    }

    public StringBuffer describeTo(StringBuffer arg0) {
      return arg0.append("check consecutive parameters map");
    }

  }

  private class Checknote implements Constraint {

    private String[][] data;

    private int idx = 0;

    public Checknote(String[][] data) {
      this.data = data;
    }

    @SuppressWarnings("unchecked")
    public boolean eval(Object arg0) {
      Map<String, String> params = (Map<String, String>) arg0;
      boolean ret = data[idx][0].equals(params.get("id"));
      ret &= data[idx][1].equals(params.get("title"));
      idx++;
      return ret;
    }

    public StringBuffer describeTo(StringBuffer arg0) {
      return arg0.append("check consecutive parameters map");
    }

  }

}
