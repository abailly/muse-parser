package oqube.muse.filter;

import oqube.muse.events.RegexpMatcher;

public class RegexFilter extends MatcherFilter{

  public RegexFilter(String regexp) {
    super(new RegexpMatcher(regexp));
  }

}
