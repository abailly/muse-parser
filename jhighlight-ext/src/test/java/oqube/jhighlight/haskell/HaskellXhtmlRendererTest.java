package oqube.jhighlight.haskell;

import java.io.IOException;

import com.uwyn.jhighlight.renderer.Renderer;
import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import junit.framework.TestCase;

public class HaskellXhtmlRendererTest extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();
    XhtmlRendererFactory.setRenderer("haskell",
        "oqube.jhighlight.haskell.HaskellXhtmlRenderer");
  }

  public void test01() throws IOException {
    String hsk = "-- Increase salary by percentage\n"
        + "increase :: Float -> Company -> Company\n"
        + "increase k = everywhere (mkT (incS k))\n" +
        "{- \"interesting\" code for increase\n" +
        "just for testing multiline \n"+
        "comments -}\n"
        + "incS :: Float -> Salary -> Salary\n"
        + "incS k (S s) = S (s * (1+k))";
    Renderer r = XhtmlRendererFactory.getRenderer("haskell");
    String rend = r.highlight("toto",hsk,"UTF-8",true);
    assertTrue("content not formated",rend.contains("<span class=\"haskell_var\">incS</span><span class=\"haskell_plain\">&nbsp;</span><span class=\"haskell_delimiter\">::"));
  }
}
