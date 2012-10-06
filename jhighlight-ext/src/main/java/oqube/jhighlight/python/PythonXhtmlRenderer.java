/*
 * Copyright 2004-2006 Geert Bevin <gbevin[remove] at uwyn dot com>
 * Distributed under the terms of either:
 * - the common development and distribution license (CDDL), v1.0; or
 * - the GNU Lesser General Public License, v2.1 or later
 * $Id: /local/muse-parser/oqube/jhighlight-ext/src/main/java/oqube/jhighlight/haskell/HaskellXhtmlRenderer.java 1059 2006-09-27T07:19:37.659037Z nono  $
 */
package oqube.jhighlight.python;

import com.uwyn.jhighlight.highlighter.ExplicitStateHighlighter;
import com.uwyn.jhighlight.renderer.XhtmlRenderer;
import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates highlighted syntax in XHTML from Python (2.4) source.
 *
 * @author abailly@oqube.com
 * @version $Revision: 1059 $
 * @since 1.0
 */
public class PythonXhtmlRenderer extends XhtmlRenderer
{
  
  static {
    // register itself
    XhtmlRendererFactory.setRenderer("python",PythonXhtmlRenderer.class.getName());
  }
  
	public final static HashMap DEFAULT_CSS = new HashMap() {{
			put("h1",
				"font-family: sans-serif; " +
				"font-size: 16pt; " +
				"font-weight: bold; " +
				"color: rgb(0,0,0); " +
				"background: rgb(210,210,210); " +
				"border: solid 1px black; " +
				"padding: 5px; " +
				"text-align: center;");
			
			put("code",
				"color: rgb(0,0,0); " +
				"font-family: monospace; " +
				"font-size: 12px; " +
				"white-space: pre;");
			
			put(".python_plain",
				"color: rgb(0,0,0);");
			
			put(".python_keyword",
				"color: rgb(0,0,0); " +
				"font-weight: bold;");
			
			put(".python_decorator",
				"color: rgb(0,44,221);");
			
      put(".python_operator",
      "color: rgb(0,124,31);");

			put(".python_delimiter",
				"color: rgb(0,33,255);");
			
			put(".python_literal",
				"color: rgb(188,0,0);");
			
			put(".python_identifier",
				"color: rgb(0,188,0);");
			
			put(".python_comment",
				"color: rgb(147,147,147); " +
				"background-color: rgb(247,247,247);");
			
		}};
	
	protected Map getDefaultCssStyles()
	{
		return DEFAULT_CSS;
	}
		
  protected String getCssClass(int style)
  {
    switch (style)
      {
      case PythonHighlighter.PLAIN:
	return "python_plain";
      case PythonHighlighter.KEYWORD:
	return "python_keyword";
      case PythonHighlighter.DECORATOR:
        return "python_decorator";
      case PythonHighlighter.OPERATOR:
	return "python_op";
      case PythonHighlighter.DELIMITER:
	return "python_delimiter";
      case PythonHighlighter.LITERAL:
	return "python_literal";
      case PythonHighlighter.COMMENT:
	return "python_comment";
      case PythonHighlighter.IDENTIFIER:
	return "python_identifier";
      }
    
    return null;
  }
  
  protected ExplicitStateHighlighter getHighlighter()
  {
    PythonHighlighter highlighter = new PythonHighlighter();
    return highlighter;
  }
}

