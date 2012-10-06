/*
 * Copyright 2004-2006 Geert Bevin <gbevin[remove] at uwyn dot com> Distributed
 * under the terms of either: - the common development and distribution license
 * (CDDL), v1.0; or - the GNU Lesser General Public License, v2.1 or later $Id$
 */
package oqube.jhighlight.haskell;

import com.uwyn.jhighlight.highlighter.ExplicitStateHighlighter;
import com.uwyn.jhighlight.highlighter.JavaHighlighter;
import com.uwyn.jhighlight.renderer.XhtmlRenderer;
import com.uwyn.jhighlight.renderer.XhtmlRendererFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates highlighted syntax in XHTML from Java source.
 * 
 * @author Arnaud Bailly
 * @version $Revision$
 * @since 1.0
 */
public class HaskellXhtmlRenderer extends XhtmlRenderer {

  static {
    // register itself
    XhtmlRendererFactory.setRenderer("haskell", HaskellXhtmlRenderer.class
        .getName());
  }

  public final static HashMap DEFAULT_CSS = new HashMap() {
    {
      put("h1", "font-family: sans-serif; " + "font-size: 16pt; "
          + "font-weight: bold; " + "color: rgb(0,0,0); "
          + "background: rgb(210,210,210); " + "border: solid 1px black; "
          + "padding: 5px; " + "text-align: center;");

      put("code", "color: rgb(0,0,0); " + "font-family: monospace; "
          + "font-size: 12px; " + "white-space: pre;");

      put(".haskell_plain", "color: rgb(0,0,0);");

      put(".haskell_keyword", "color: rgb(0,0,0); " + "font-weight: bold;");

      put(".haskell_ctor", "color: rgb(0,44,221);");

      put(".haskell_op", "color: rgb(0,124,31);");

      put(".haskell_varop", "color: rgb(0,124,31);");

      put(".haskell_var", "color: rgb(0,124,31);");

      put(".haskell_conop", "color: rgb(0,44,221);");

      put(".haskell_delimiter", "color: rgb(0,33,255);");

      put(".haskell_literal", "color: rgb(188,0,0);");

      put(".haskell_comment", "color: rgb(147,147,147); "
          + "background-color: rgb(247,247,247);");

    }
  };

  protected Map getDefaultCssStyles() {
    return DEFAULT_CSS;
  }

  protected String getCssClass(int style) {
    switch (style) {
    case HaskellHighlighter.HASKELL_PLAIN:
      return "haskell_plain";
    case HaskellHighlighter.HASKELL_KEYWORD:
      return "haskell_keyword";
    case HaskellHighlighter.HASKELL_CTOR:
      return "haskell_ctor";
    case HaskellHighlighter.HASKELL_CONOP:
      return "haskell_conop";
    case HaskellHighlighter.HASKELL_OP:
      return "haskell_op";
    case HaskellHighlighter.HASKELL_DELIMITER:
      return "haskell_delimiter";
    case HaskellHighlighter.HASKELL_LITERAL:
      return "haskell_literal";
    case HaskellHighlighter.HASKELL_COMMENT:
      return "haskell_comment";
    case HaskellHighlighter.HASKELL_VAR:
      return "haskell_var";
    case HaskellHighlighter.HASKELL_VAROP:
      return "haskell_varop";
    }

    return null;
  }

  protected ExplicitStateHighlighter getHighlighter() {
    HaskellHighlighter highlighter = new HaskellHighlighter();
    return highlighter;
  }
}
