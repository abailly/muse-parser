package oqube.muse.html;

import junit.framework.TestCase;
import oqube.muse.MuseSink;

import org.jmock.Expectations;
import org.jmock.Mockery;

public class SourceFragmentTest extends TestCase {

  private Mockery context = new Mockery();

  private SourceFragment firstFragment;

  private HTMLFragmentLinker fragmentLinker;

  private SourceFragment previousFragment;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    fragmentLinker = new HTMLFragmentLinker();
    firstFragment = new SourceFragment();
    firstFragment.setName("first");
    previousFragment = new SourceFragment();
    previousFragment.setName("previous");
  }

  public void testFragmentLinkerSetDifferentIdsForDifferentFragments()
      throws Exception {
    HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();
    SourceFragment f1 = new SourceFragment();
    SourceFragment f2 = new SourceFragment();
    assertTrue("fragments should not be equal", !f1.equals(f2));
    String id1 = fragmentLinker.add(f1);
    String id2 = fragmentLinker.add(f2);
    assertTrue("fragment ids should be different", !id1.equals(id2));
  }

  public void testFragmentLinkerLinkNamesToIds() throws Exception {
    final MuseSink mock = context.mock(MuseSink.class);
    final String fragmentId = fragmentLinker.add(firstFragment);
    context.checking(new Expectations() {
      {
        one(mock).link(fragmentId, firstFragment.getName());
      }
    });
    fragmentLinker.setSink(mock);
    fragmentLinker.outputLinkTo(firstFragment.getName());
    context.assertIsSatisfied();
  }

  public void testFragmentLinkerLinkSuccessiveFragments() throws Exception {
    final MuseSink mock = context.mock(MuseSink.class);
    final String previousFragmentId = fragmentLinker.add(previousFragment);
    final String firstFragmentId = fragmentLinker.add(firstFragment);
    context.checking(new Expectations() {
      {
        one(mock).anchor(firstFragmentId);
        one(mock).link(previousFragmentId, previousFragment.getName());
      }
    });
    fragmentLinker.setSink(mock);
    fragmentLinker.outputLinksFor(firstFragment);
    context.assertIsSatisfied();
  }

  public void testFragmentLinkerOmitLinkOnFirstFragment() throws Exception {
    final MuseSink mock = context.mock(MuseSink.class);
    final String thisFragmentId = fragmentLinker.add(firstFragment);
    context.checking(new Expectations() {
      {
        one(mock).anchor(thisFragmentId);
      }
    });
    fragmentLinker.setSink(mock);
    fragmentLinker.outputLinksFor(firstFragment);
    context.assertIsSatisfied();
  }

  public void testFragmentLinkerSubstitutesReferencesToLinksInFragmentBody()
      throws Exception {
    HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();
    final String fragmentContent = "some content with link ot <<previous>> fragment";
    firstFragment.setContent(fragmentContent);
    fragmentLinker.add(previousFragment);
    fragmentLinker.add(firstFragment);
    fragmentLinker.substituteContent(firstFragment);
    assertTrue("content should have substituted link: "
        + firstFragment.getContent(), firstFragment.getContent().contains(
        "<a href=\"#id0\">&lt;&lt;previous"));
  }

  public void testFragmentLinkerIncludesReferencesToUnknownFragments()
      throws Exception {
    HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();
    final String fragmentContent = "some content with link ot <<unknown>> fragment";
    firstFragment.setContent(fragmentContent);
    fragmentLinker.add(firstFragment);
    fragmentLinker.substituteContent(firstFragment);
    assertTrue("content should have substituted link: "
        + firstFragment.getContent(), firstFragment.getContent().contains(
        "<a href=\"#id1\">&lt;&lt;unknown"));
  }

  public void testFragmentLinkerUseForwardSubstitutedReferencesAsIds()
      throws Exception {
    HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();
    final String fragmentContent = "some content with link ot <<previous>> fragment";
    firstFragment.setContent(fragmentContent);
    fragmentLinker.add(firstFragment);
    fragmentLinker.substituteContent(firstFragment);
    SourceFragment other = new SourceFragment();
    other.setName("other");
    fragmentLinker.add(other);
    assertEquals("Id of added fragment incorrect", "id1", fragmentLinker
        .add(previousFragment));
  }

  public void testFragmentLinkerSetDifferentIdsForFragmentsWithSameName()
      throws Exception {
    final MuseSink mock = context.mock(MuseSink.class);
    HTMLFragmentLinker fragmentLinker = new HTMLFragmentLinker();
    previousFragment.setName(firstFragment.getName());
    final String id1 = fragmentLinker.add(firstFragment);
    final String id2 = fragmentLinker.add(previousFragment);
    context.checking(new Expectations() {
      {
        one(mock).anchor(id2);
        one(mock).link(id1, firstFragment.getName());
      }
    });
    fragmentLinker.setSink(mock);
    fragmentLinker.outputLinksFor(previousFragment);
    assertTrue("fragment ids should be different", !id1.equals(id2));
    context.assertIsSatisfied();
  }

}
