/**
 *  Copyright (C) 2008 - OQube / Arnaud Bailly
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * Created on Sun Jan 20 2008
 */
package oqube.muse;

import oqube.muse.doxia.MuseDoxiaLinker;
import oqube.muse.doxia.SinkWrapper;

import org.apache.maven.doxia.sink.Sink;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.junit.Test;

/**
 * 
 * @author abailly@oqube.com
 * @version $Id$
 */
public class SinkWrapperTest {

  Mockery context = new Mockery();

  @Test
  public void LinkToPngProducesImgTag() {
    final String link = "toto.png";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    context.checking(new Expectations() {
      {
        one(sk).figure();
        one(sk).figureGraphics(link);
        one(sk).figureCaption();
        one(sk).text(text);
        one(sk).figureCaption_();
        one(sk).figure_();
      }
    });
    SinkWrapper ss = new SinkWrapper();
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToRelativePngProducesImgTag() {
    final String link = "../../toto.png";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).figure();
        one(sk).figureGraphics(link);
        one(sk).figureCaption();
        one(sk).text(text);
        one(sk).figureCaption_();
        one(sk).figure_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkPngURIProducesImgTag() {
    final String link = "http://somehost/toto.png";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).figure();
        one(sk).figureGraphics(link);
        one(sk).figureCaption();
        one(sk).text(text);
        one(sk).figureCaption_();
        one(sk).figure_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToMuseFileProducesAnchorTagToHtmlFile() {
    final String link = "toto.muse";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    context.checking(new Expectations() {
      {
        one(sk).link("toto.html");
        one(sk).text(text);
        one(sk).link_();
      }
    });
    SinkWrapper ss = new SinkWrapper();
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToRelativeMuseFileProducesAnchorTagToHtmlFile() {
    final String link = "./toto.muse";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).link("./toto.html");
        one(sk).text(text);
        one(sk).link_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToMuseURIProducesAnchorTagToURI() {
    final String link = "http://localhost/toto.muse";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).link(link);
        one(sk).text(text);
        one(sk).link_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToFragmentInMuseFile() {
    final String link = "toto.muse#titi";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).link("toto.html#titi");
        one(sk).text(text);
        one(sk).link_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }

  @Test
  public void LinkToLonelyFragmentInHTMLFile() {
    final String link = "#titi";
    final String text = "toto";
    MuseDoxiaLinker linker = new MuseDoxiaLinker();
    final Sink sk = context.mock(Sink.class);
    SinkWrapper ss = new SinkWrapper();
    context.checking(new Expectations() {
      {
        one(sk).link(link);
        one(sk).text(text);
        one(sk).link_();
      }
    });
    ss.setSink(sk);
    linker.link(ss, link, text);
    context.assertIsSatisfied();
  }
}
