package oqube.muse.html;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.MuseSink;

/**
 * A class for managing HTML links between source fragments.
 * A linker receives successive source fragments {@link #add(SourceFragment)} and assign 
 * them a unique identifier (unique within the context of this linker). Fragments are 
 * instances of the {@link SourceFragment} class with at least a defined name. 
 * It can then be used to:
 * <ul>
 * <li>retrieve the link associated with some name {@link #outputLinkTo(String)},</li>
 * <li>substitute links to other fragments appearing within the fragment's content 
 * as (HTML) hyperlinks ({@link #substituteContent(SourceFragment)})</li>
 * <li>output an anchor for a fragment together with a back link to previous fragment ({@link #outputLinksFor(SourceFragment)})</li>  
 * </ul>
 * @author nono
 * 
 */
public class HTMLFragmentLinker {

  private int counter = 0;

  private MuseSink sink;

  private Map<String, String> namesToIds = new HashMap<String, String>();

  private Set<String> forwards = new HashSet<String>();

  private SourceFragment lastFragment;

  private SourceFragment previousFragment;

  public MuseSink getSink() {
    return sink;
  }

  public void setSink(MuseSink sink) {
    this.sink = sink;
  }

  public String add(SourceFragment fragment) {
    String knownId = namesToIds.get(fragment.getName());
    if (!handleForwardReferenceTo(fragment, knownId)) {
      storeIdFor(fragment, knownId);
    }
    previousFragment = lastFragment;
    lastFragment = fragment;
    return fragment.getId();
  }

  private void storeIdFor(SourceFragment fragment, String knownId) {
    fragment.setId(genId());
    if (knownId == null)
      namesToIds.put(fragment.getName(), fragment.getId());
  }

  private boolean handleForwardReferenceTo(SourceFragment fragment,
      String knownId) {
    boolean forward = knownId != null && forwards.contains(fragment.getName());
    if (forward) {
      fragment.setId(knownId);
      forwards.remove(fragment.getName());
    }
    return forward;
  }

  private String genId() {
    return "id" + (counter++);
  }

  public void outputLinkTo(String name) {
    final String id = namesToIds.get(name);
    sink.link(id, name);
  }

  public void substituteContent(SourceFragment target) {
    StringBuffer sb = new StringBuffer();
    Pattern pat = Pattern.compile("<<([^<]+)>>");
    Matcher m = pat.matcher(target.getContent());
    while (m.find()) {
      final String reference = m.group(1);
      String id = maybeIsForwardReference(reference, namesToIds.get(reference));
      m.appendReplacement(sb, "<a href=\"#" + id + "\">&lt;&lt;$1&gt;&gt;</a>");
    }
    m.appendTail(sb);
    target.setContent(sb.toString());
  }

  private String maybeIsForwardReference(final String reference, String id) {
    if (id == null) {
      id = genId();
      namesToIds.put(reference, id);
      forwards.add(reference);
    }
    return id;
  }

  public void outputLinksFor(SourceFragment thisFragment) {
    sink.anchor(thisFragment.getId());
    if (previousFragment != null) {
      sink.link(previousFragment.getId(), previousFragment.getName());
    }
  }
}
