/*
 * Copyright (c) 2007 - OQube / Arnaud Bailly This library is free software; you
 * can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * Created 22 juil. 07
 */
package oqube.muse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import oqube.muse.template.Template;

import org.jmock.Mock;
import org.jmock.MockObjectTestCase;
import org.jmock.core.Constraint;

/**
 * @author nono
 * 
 */
public class TemplateSinkTest extends MockObjectTestCase {

  public void testHeaderTemplateLifecycleWithDefaultSink() {
    Mock mock = mock(Template.class);
    Map<String, String> env = new HashMap<String, String>() {
      {
        put("toto", "tata");
        // computed properties added by publisher
        put("date", "tata");
        put("year", "tata");
        put("encoding", "tata");
        // standard property
        put("file.encoding", "");
      }
    };
    mock.expects(once()).method("content").with(checkmap(env)).will(
        returnValue(""));
    DefaultSink sink = makeSink();
    sink.setOut(new PrintWriter(new StringWriter()));
    sink.setHeader((Template) mock.proxy());
    sink.start("document",null);
    sink.start("header",null);
    sink.addMetadata("toto", "tata");
    sink.end("header");
  }

  private Constraint checkmap(final Map<String, String> env) {
    return new Constraint() {

      public StringBuffer describeTo(StringBuffer arg0) {
        return arg0.append("passed map contains all elements in " + env);
      }

      public boolean eval(Object arg0) {
        Map m = (Map) arg0;
        return m.keySet().containsAll(env.keySet());
      }

    };
  }

  public void testFooterTemplateLifecycleWithDefaultSink() {
    Mock mock = mock(Template.class);
    Map<String, String> env = new HashMap<String, String>() {
      {
        put("toto", "tata");
      }
    };
    mock.expects(once()).method("content").with(checkmap(env)).will(
        returnValue(""));
    DefaultSink sink = makeSink();
    sink.setOut(new PrintWriter(new StringWriter()));
    sink.setFooter((Template) mock.proxy());
    sink.start("document",null);
    sink.start("footer",null);
    sink.addMetadata("toto", "tata");
    sink.end("footer");
  }

  protected DefaultSink makeSink() {
    return new DefaultSink();
  }
}
