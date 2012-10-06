package oqube.muse.events;

import junit.framework.TestCase;
import oqube.muse.filter.MatcherFilter;
import oqube.muse.filter.OrFilter;
import oqube.muse.filter.TagFilter;

public class CompositeFilterTest extends TestCase {

  public void testFilterStartAndEndOfTagExcludingSubtree() {
    TagFilter filter = new TagFilter("emph");
    assertIsNullEvent(filter.filter(new TextEvent("toto")));
    assertIsNonNullEvent(filter.filter(new StartEvent("emph", null, 10)));
    assertIsNonNullEvent(filter.filter(new EndEvent("emph")));
    assertIsNullEvent(filter.filter(new StartEvent("titi", null, 10)));
    assertIsNullEvent(filter.filter(new EndEvent("titi")));
  }

  public void testOrFilterIncludesAllSubfilters() {
    OrFilter filter = new OrFilter(new TagFilter("emph"), new MatcherFilter(
        EventSequenceMatcher.textEquals("titititi")));
    assertIsNullEvent(filter.filter(new TextEvent("toto")));
    assertIsNonNullEvent(filter.filter(new TextEvent("titititi")));
    assertIsNonNullEvent(filter.filter(new StartEvent("emph", null, 10)));
    assertIsNonNullEvent(filter.filter(new EndEvent("emph")));
    assertIsNullEvent(filter.filter(new StartEvent("titi", null, 10)));
    assertIsNullEvent(filter.filter(new EndEvent("titi")));
  }

  private void assertIsNonNullEvent(SinkEvent filter) {
    assertTrue(filter != SinkEvent.NULL_EVENT);
  }

  private void assertIsNullEvent(SinkEvent filter) {
    assertTrue(filter == SinkEvent.NULL_EVENT);
  }
}
