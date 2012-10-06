package oqube.muse.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.math.BigInteger;
import java.util.Collections;

import org.junit.Assert;
import org.junit.Test;


public class AnimatorTest {

  private final String EOL = System.getProperty("line.separator");


  public static class Dummy {

    private int i;

    private String s;

    private boolean b;

    private double d;

    public void setInt(int i) {
      this.i = i;
    }

    public void callWith(String s, boolean b, double d) {
      this.s = s;
      this.b = b;
      this.d = d;
    }
  }

  public static class Dummy2 {

    private Dummy d;

    private float f;

    public Dummy create(String s) {
      this.d = new Dummy();
      d.s = s;
      return d;
    }

    public void doSomething(float f) {
      this.f = f;
    }
  }

  @Test
  public void animatorUseParserToCallMethod() throws Throwable {
    Animator anim = new Animator();
    Dummy d = new Dummy();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "call with \"some string\" 'true '12.3")));
    Object o = anim.animateWith(d, p);
    Assert.assertSame(d, o);
    Assert.assertEquals(true, d.b);
    Assert.assertEquals(12.3, d.d, 0.0);
    Assert.assertEquals("some string", d.s);
  }

  @Test
  public void animatorInvokeSequenceOnSeveralObjects() throws Throwable {
    Animator anim = new Animator();
    Dummy2 d2 = new Dummy2();
    Parser p = new Parser(new BufferedReader(new StringReader("create 'data"
        + EOL + "call with \"some string\" 'true '12.3")));
    Assert.assertNull(p.more.more);
    Object o = anim.animateWith(d2, p);
    Dummy d = d2.d;
    Assert.assertSame(d, o);
    Assert.assertEquals(true, d.b);
    Assert.assertEquals(12.3, d.d, 0.0);
    Assert.assertEquals("some string", d.s);

  }

  @Test
  public void animatorAcceptsNullTarget() throws Throwable {
    Animator anim = new Animator();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "var 'toto is 'titi")));
    Object o = anim.animateWith(null, p);
    Assert.assertSame(anim, o);
  }

  @Test
  public void animatorCreateObjectsFromClassNames() throws Throwable {
    Animator anim = new Animator();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "make 'java.math.BigInteger '1 ")));
    Object o = anim.animateWith(null, p);
    Assert.assertEquals(new BigInteger("1"), o);
  }

  @Test
  public void animatorCreateObjectsFromClassNamesWithImplicitPackage()
      throws Throwable {
    Animator anim = new Animator();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "make 'ArrayList ")));
    Object o = anim.animateWith(null, p);
    Assert.assertEquals(Collections.EMPTY_LIST, o);
  }

  @Test
  public void animatorResolveReferenceNumbers() throws Throwable {
    Animator anim = new Animator();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "make 'java.math.BigInteger '1 " + EOL
            + "make 'java.math.BigInteger '2 " + EOL + "add '$1 ")));
    Object o = anim.animateWith(null, p);
    Assert.assertEquals(new BigInteger("3"), o);
  }

  @Test
  public void animatorInvokeSequenceOnSameObjectIfVoidMethod() throws Throwable {
    Animator anim = new Animator();
    Dummy2 d2 = new Dummy2();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "do something '45.6 " + EOL + "create 'data" + EOL
            + "call with \"some string\" 'true '12.3")));
    Object o = anim.animateWith(d2, p);
    Dummy d = d2.d;
    Assert.assertSame(d, o);
    Assert.assertEquals(true, d.b);
    Assert.assertEquals(12.3, d.d, 0.0);
    Assert.assertEquals(45.6, d2.f, 0.1);
    Assert.assertEquals("some string", d.s);
  }

  @Test
  public void animatorInvocationsAreSeparatedByEOL() throws Throwable {
    Animator anim = new Animator();
    Dummy2 d2 = new Dummy2();
    Parser p = new Parser(new BufferedReader(new StringReader(
        "do something '45.6 " + EOL + "create 'data" + EOL
            + "call with \"some string\" 'true '12.3" + EOL + EOL
            + "do something '46.5 ")));
    Object o = anim.animateWith(d2, p);
    Dummy d = d2.d;
    Assert.assertSame(d2, o);
    Assert.assertEquals(true, d.b);
    Assert.assertEquals(12.3, d.d, 0.0);
    Assert.assertEquals(46.5, d2.f, 0.1);
    Assert.assertEquals("some string", d.s);
  }

  public static class Dummy3 {
    private int i;

    private String[] s;

    public void withArgs(int i, String... s) {
      this.i = i;
      this.s = s;
    }
  }

  @Test
  public void animatorCanHandleMethodsWithVarArgs() throws Throwable {
    Animator anim = new Animator();
    String input = "with args '1 'toto 'tutu";
    Dummy3 d = new Dummy3();
    Object o = anim.animateWith(d, new Parser(new BufferedReader(
        new StringReader(input))));
    Assert.assertEquals("tutu", d.s[1]);
  }

  @Test
  public void animatorCanResolveVariableReferences() throws Throwable {
    String input = "  var 'var is  'toto " + EOL
        + " var 'var2 is \"${var} tutu\"" + EOL
        + " call with \"titi ${var2}\" 'true '1";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Animator a = new Animator();
    Dummy d = new Dummy();
    a.animateWith(d, p);
    Assert.assertEquals("varIs", p.content());
    Assert.assertEquals("titi toto tutu", d.s);
  }

  @Test
  public void animatorThrowErrorWithLineNumberOnUnknownCall()
      throws IOException {
    String input = "  var 'var is  'toto " + EOL
        + " var 'var2 is \"${var} tutu\"" + EOL
        + " call with \"titi ${var2}\" 'true '1" + EOL
        + "var 'var3 is \"${unknown}\"";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Animator a = new Animator();
    Dummy d = new Dummy();
    try {
      a.animateWith(d, p);
    } catch (Throwable e) {
      Assert.assertTrue(e instanceof AnimatorError);
      Assert.assertEquals(4, ((AnimatorError) e).getLine());
    }
  }

  @Test
  public void animatorThrowErrorWithLineNumberOnUnknownVariableReference()
      throws IOException {
    String input = "  var 'var is  'toto " + EOL
        + " var 'var2 is \"${var} tutu\"" + EOL + " call with '1";
    Parser p = new Parser(new BufferedReader(new StringReader(input)));
    Animator a = new Animator();
    Dummy d = new Dummy();
    try {
      a.animateWith(d, p);
    } catch (Throwable e) {
      Assert.assertTrue(e instanceof AnimatorError);
      Assert.assertEquals(3, ((AnimatorError) e).getLine());
    }
  }

}
