package oqube.muse.events;

import static oqube.muse.events.EventSequenceMatcher.textEquals;
import junit.framework.TestCase;
import oqube.muse.filter.SinkFilter;
import oqube.muse.filter.StartFilter;
import oqube.muse.filter.StopFilter;

public class StartStopFilterTest extends TestCase {

  public void testFilterStartOnOneEvent() {
    EventSequenceMatcher m = new EventSequenceMatcher(textEquals("toto"));
    StartFilter filter = new StartFilter(m);
    assertFilterGeneratesNullEvent(filter, "tutu");
    assertFilterGeneratesSameEvent(filter, "toto");
    assertFilterGeneratesSameEvent(filter, "titi");
  }

  private void assertFilterGeneratesSameEvent(SinkFilter filter, String text) {
    assertEquals("event filtered", new TextEvent(text), filter
        .filter(new TextEvent(text)));
  }

  private void assertFilterGeneratesNullEvent(SinkFilter filter,
      final String text) {
    assertEquals("event not filtered", SinkEvent.NULL_EVENT, filter
        .filter(new TextEvent(text)));
  }

  public void testFilterStartOnTwoEvents() {
    EventSequenceMatcher m = new EventSequenceMatcher(textEquals("toto"),
        textEquals("titi"));
    StartFilter filter = new StartFilter(m);
    assertFilterGeneratesNullEvent(filter, "tutu");
    assertFilterGeneratesNullEvent(filter, "toto");
    assertFilterGeneratesCompoundEvent("titi", filter, new CompoundEvent(
        new TextEvent("toto"), new TextEvent("titi")));
    assertFilterGeneratesSameEvent(filter, "tata");
  }

  private void assertFilterGeneratesCompoundEvent(final String string,
      StartFilter filter, final CompoundEvent compoundEvent) {
    assertEquals("event filtered", compoundEvent, filter.filter(new TextEvent(
        string)));
  }

  public void testFilterStartOnZeroEventsFilterEverything() {
    StartFilter filter = new StartFilter(new EventSequenceMatcher());
    assertFilterGeneratesSameEvent(filter, "toto");
    assertFilterGeneratesSameEvent(filter, "tata");
  }

  public void testFilterStopOnZeroEventsFilterNothing() {
    StopFilter filter = new StopFilter(new EventSequenceMatcher());
    assertFilterGeneratesNullEvent(filter, "tutu");
    assertFilterGeneratesNullEvent(filter, "tata");
  }

  public void testFilterStopOnOneEvent() {
    EventSequenceMatcher m = new EventSequenceMatcher(textEquals("toto"));
    StopFilter filter = new StopFilter(m);
    assertFilterGeneratesSameEvent(filter, "tutu");
    assertFilterGeneratesSameEvent(filter, "toto");
    assertFilterGeneratesNullEvent(filter, "titi");
  }

  public void testFilterStopOnTwoEvents() {
    EventSequenceMatcher m = new EventSequenceMatcher(textEquals("toto"),
        textEquals("titi"));
    StopFilter filter = new StopFilter(m);
    assertFilterGeneratesSameEvent(filter, "tutu");
    assertFilterGeneratesSameEvent(filter, "toto");
    assertFilterGeneratesSameEvent(filter, "titi");
    assertFilterGeneratesNullEvent(filter, "titi");
  }
}
