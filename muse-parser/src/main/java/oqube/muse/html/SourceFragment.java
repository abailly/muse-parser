package oqube.muse.html;

public class SourceFragment {

  private static final String DEFAULT_LANGUAGE = "java";

  private String name = "";

  private String language = DEFAULT_LANGUAGE;

  private boolean hidden;

  private String content = "";

  private String id;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLanguage() {
    return language;
  }

  public void setLanguage(String language) {
    this.language = language;
  }

  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

}
