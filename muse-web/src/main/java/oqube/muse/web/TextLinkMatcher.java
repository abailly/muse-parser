package oqube.muse.web;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import oqube.muse.events.TextEvent;
import oqube.muse.feed.FeedUtils;

import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

public class TextLinkMatcher extends TypeSafeMatcher<TextEvent> {

  private String titleToMatch;

  public TextLinkMatcher(String date, String title) {
    this.titleToMatch = date + ": " + title;
  }

  @Override
  public boolean matchesSafely(TextEvent event) {
    String text = event.getText();
    Pattern pat = Pattern.compile("([0-9]{8}: )(.*)");
    Matcher match = pat.matcher(text);
    if (match.matches())
      return titleToMatch.equals(match.group(1)
          + FeedUtils.escapeTitle(match.group(2)));
    else
      return false;
  }

  public void describeTo(Description arg0) {
    arg0.appendText("matches section title '" + titleToMatch + "'");
  }

}
