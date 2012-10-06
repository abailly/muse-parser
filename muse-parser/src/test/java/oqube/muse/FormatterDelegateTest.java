package oqube.muse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

public class FormatterDelegateTest extends TestCase {

  public void testDoesNothingOnUnknownTag() {
    FormatterDelegate del = new FormatterDelegate();
    del.start("toto", null);
  }

  private boolean iscalled;

  public void testDelegateCallsHandlerOnKnownTagStarting() {
    FormatterDelegate del = new FormatterDelegate();
    del.append("toto", new ElementHandler("toto") {

      @Override
      public void start(Map<String, String> parameters) {
        iscalled = true;
      }

    });
    del.start("toto", null);
    assertTrue("handler not called", iscalled);
  }

  public void testDelegateCallsHandlerOnKnownTagEnding() {
    FormatterDelegate del = new FormatterDelegate();
    del.append("toto", new ElementHandler("toto") {

      @Override
      public void end() {
        iscalled = true;
      }

    });
    del.end("toto");
    assertTrue("handler not called", iscalled);
  }

  public void testDelegateCallsHandlerInRegistrationOrder() {
    final List<String> called = new ArrayList<String>();
    FormatterDelegate del = new FormatterDelegate();
    del.append("toto", new ElementHandler("toto") {

      @Override
      public void start(Map<String, String> parameters) {
        called.add("1");
      }

    });
    del.append("toto", new ElementHandler("toto") {

      @Override
      public void start(Map<String, String> parameters) {
        called.add("2");
      }

    });
    del.append("toto", new ElementHandler("toto") {

      @Override
      public void start(Map<String, String> parameters) {
        called.add("3");
      }

    });
    del.start("toto", null);
    assertEquals("handler not called in order", Arrays.asList(new String[] {
        "1", "2", "3" }), called);
  }

}
