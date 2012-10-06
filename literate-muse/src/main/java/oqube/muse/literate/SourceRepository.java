package oqube.muse.literate;

import java.util.Map;

public interface SourceRepository {

  /** 
   * Final collection and resolution of all blocks.
   * This method returns a map from names to data where name 
   * is the expected filename of the source file and data is the resolved content, 
   * that is content of the source file with all references replaced.
   * 
   * @return a Map<String,String> from  toplevel fragments to their content.
   * @throws IllegalArgumentException if not lenient and some fragments cannot be
   * resolved.
   */
  public abstract Map /* < String, String > */collectSources();

  /** 
   * Add a new fragment to this literate sources repository.
   * 
   * @param name the fragment's identifier.
   * @param content the fragment's content. May contains references
   * to other fragments.
   */
  public abstract void addFragment(String name, String content);

  public abstract boolean isLenient();

  public abstract void setLenient(boolean lenient);

  /**
   * Remove all accumulated data in this repository.
   *
   */
  void reset();
}