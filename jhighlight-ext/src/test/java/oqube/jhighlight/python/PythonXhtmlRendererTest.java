package oqube.jhighlight.python;

import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;

import com.uwyn.jhighlight.renderer.Renderer;
import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import junit.framework.TestCase;

public class PythonXhtmlRendererTest extends TestCase {

  protected void setUp() throws Exception {
    super.setUp();
    XhtmlRendererFactory.setRenderer("python",
        "oqube.jhighlight.python.PythonXhtmlRenderer");
  }

  public void test01() throws IOException {
    InputStream is = getClass().getResourceAsStream("/webmail.py");
    Renderer r = XhtmlRendererFactory.getRenderer("python");
    ByteArrayOutputStream bos  = new ByteArrayOutputStream();
    r.highlight("toto",is,bos,"UTF-8",true);
    assertTrue("content not formated",bos.toString().contains("</span><span class=\"python_keyword\">def</span><span class=\"python_plain\">&nbsp;</span><span class=\"python_identifier\">__init___</span><span class=\"python_delimiter\">(</span><span class=\"python_keyword\">self</span><span class=\"python_plain\">,</span><span class=\"python_identifier\">login</span><span class=\"python_plain\">,</span><span class=\"python_identifier\">pwd</span><span class=\"python_plain\">,"));    
  }
}
