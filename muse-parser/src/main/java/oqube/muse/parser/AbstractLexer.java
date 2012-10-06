/*
 * ______________________________________________________________________________
 * 
 * Copyright 2006 Arnaud Bailly -
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * (1) Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * 
 * (2) Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * (3) The name of the author may not be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO
 * EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR
 * OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * Created on Mon Aug 21 2006
 * 
 */
package oqube.muse.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This abstract class is the base class of all parsing elements in the Muse
 * parser.
 * 
 * The behavior of this class is controlled by its {@link parse(String)} method.
 * This basic implementation is based on properties {@link #matcher} and
 * {@link #next}: if the matcher matches the input string, then the method
 * {@link #handler()} is called, else the input string is passed to next
 * matcher in list.
 * 
 * 
 * matches a given string
 * 
 * @author abailly@oqube.muse.com
 * @version $Id$
 */
public abstract class AbstractLexer {

  /* current matcher */
  protected Matcher matcher;

  /* next lexer */
  protected AbstractLexer next;

  public AbstractLexer getNext() {
    return next;
  }

  public AbstractLexer setNext(AbstractLexer next) {
    return this.next = next;
  }

  /**
   * Instantiate the lexer with teh given pattern.
   * 
   * @param pattern
   *          pattern matched by this lexer.
   * @see java.util.regex.Pattern
   */
  public AbstractLexer(String pattern) {
    this.matcher = Pattern.compile(pattern).matcher("");
  }

  /**
   * Parse the given string. If string matches pattern, then the handler method
   * is invoked, else if there is a next lexer in the chain, it gets called.
   */
  public void parse(String s) {
    int i = 0;
    matcher.reset(s);
    if (matcher.matches())
      handler();
    else if (next != null)
      next.parse(s);
  }

  /**
   * Return the matched matcher.
   * 
   * @return first matcher matched in parse method. May be null if no match was
   *         found
   */
  public Matcher getMatcher() {
    return matcher;
  }

  /**
   * This method is invoked by the lexer if this lexer matches the input.
   */
  public abstract void handler();
}
