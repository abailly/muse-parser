/*______________________________________________________________________________
 * 
 * Copyright 2006  Arnaud Bailly - 
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * (1) Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 * (2) Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in
 *     the documentation and/or other materials provided with the
 *     distribution.
 *
 * (3) The name of the author may not be used to endorse or promote
 *     products derived from this software without specific prior
 *     written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 * STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 * Created on Mon Aug 21 2006
 *
 */
package oqube.muse;

/**
 * An interface for publishing links. 
 * This interface is used by {@link MuseSink} instances to process
 * link references and publish them. Instances of this interface are processed 
 * in a *Chain of responsibility* pattern: an instance that knows how to handle
 * a link should return a non-null result. 
 *
 * Implementations of this interface should declare themselves as plexus components
 * with an additional *hint* specifying which kind of rendering format they provide 
 * (ie. latex, xhtml, pdf, whatever). This hint will be used in wiring  linkers
 * to sinks.
 *
 * @author abailly@oqube.muse.com
 * @version $Id$
 */
public interface MuseLinker {

  /*
   * Role string for plexus container.
   */
  String ROLE = MuseLinker.class.getName();

  /**
   * Generate a publication ready link for given link
   * and text couple.
   *
   * @param sink thi sink where data is published to
   * @param link the target of the link
   * @param text the anchor of the link
   * @return a correctly formatted link or null if this instance should 
   * not handle these kind of links.
   */
  void link(MuseSink sink,String link,String text);

  /** 
   * Generates an anchor that may later be used as a target for links.
   * Depending on the sink, this may ar may not produce output.
   *
   * @param sink thi sink where data is published to
   * @param anchor the identifier of the anchor
   */
  void anchor(MuseSink sink,String anchor);

}
