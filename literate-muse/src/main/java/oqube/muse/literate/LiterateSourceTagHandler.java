/**
 * 
 */
package oqube.muse.literate;

import oqube.muse.AbstractTagHandler;
import oqube.muse.MuseSink;

/**
 * A tag handler for collecting <source> tags and transfering them to a
 * {@link oqube.muse.literate.DefaultSourceRepository} instance.
 * 
 * This tag handler does not format tags but just collects data from
 * <em>named</em> source tags and hand them over to a SourceRepository for
 * further processing.
 * 
 * @author nono
 * 
 */
public class LiterateSourceTagHandler extends AbstractTagHandler {

  /*
   * the repository instance.
   */
  private SourceRepository repository = new DefaultSourceRepository();

  
  /**
   * @return Returns the repository.
   */
  public SourceRepository getRepository() {
    return repository;
  }

  /**
   * @param repository
   *          The repository to set.
   */
  public void setRepository(SourceRepository repository) {
    this.repository = repository;
  }

  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.AbstractTagHandler#block(oqube.muse.MuseSink,
   *      java.lang.String, java.lang.String[][], java.lang.String)
   */
  public boolean block(MuseSink sink, String tag, String[][] at, String content) {
    // Check tag
    if ("source".equals(tag) || "src".equals(tag)) {
      String name = null; 
      for (int i = 0; i < at.length; i++) {
        if ("name".equals(at[i][0]))
          name = at[i][1];
      }
      // pass fragment to source handler
      if (repository != null && name != null)
        repository.addFragment(name, content);
    }
    // always give a chance to other handlers
    if (getNext() != null)
      return getNext().block(sink, tag, at, content);
    else
      return false;
  }
}
