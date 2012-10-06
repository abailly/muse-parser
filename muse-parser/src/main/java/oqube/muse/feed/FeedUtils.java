package oqube.muse.feed;

public abstract class FeedUtils {

  public static String escapeTitle(String group) {
    StringBuffer buffer = new StringBuffer();
    for (char c : group.toCharArray()) {
      if (Character.isLetterOrDigit(c) && c < 128)
        buffer.append(c);
      else
        buffer.append('-');
    }
    return buffer.toString();
  }

}
