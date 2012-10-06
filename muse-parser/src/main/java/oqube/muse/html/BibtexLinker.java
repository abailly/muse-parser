/**
 *  Copyright (C) 2006 - OQube / Arnaud Bailly
 This library is free software; you can redistribute it and/or
 modify it under the terms of the GNU Lesser General Public
 License as published by the Free Software Foundation; either
 version 2.1 of the License, or (at your option) any later version.

 This library is distributed in the hope that it will be useful,
 but WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 Lesser General Public License for more details.

 You should have received a copy of the GNU Lesser General Public
 License along with this library; if not, write to the Free Software
 Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA

 Created 1 oct. 2006
 */
package oqube.muse.html;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import oqube.muse.AbstractMuseLinker;
import oqube.muse.MuseSink;
import bibtex.dom.BibtexEntry;
import bibtex.dom.BibtexFile;
import bibtex.expansions.CrossReferenceExpander;
import bibtex.expansions.ExpansionException;
import bibtex.expansions.MacroReferenceExpander;
import bibtex.expansions.PersonListExpander;
import bibtex.parser.BibtexParser;
import bibtex.parser.ParseException;

/**
 * A linker class that generates data from a bibtex reference in muse format.
 * This class extracts information from the referenced bibtex entry, parses it
 * and output to sink an anchor named from the entry's id and labelled with a serial
 * number. The formatted content of the entry can latex be retrieved to be 
 * output eg. as footnotes. 
 * 
 * @author nono
 * 
 */
public class BibtexLinker extends AbstractMuseLinker {

  /*
   * the parser
   */
  BibtexParser parser = new BibtexParser(false);

  /*
   * the bibtex reference tree 
   */
  BibtexFile bibtex = new BibtexFile();
  
  /*
   * serial number of entries. for labelling.
   */
  private int serial = 1;
  
  private List entries = new ArrayList();
  
  /**
   * Reinitialize this bibtexer context.
   *
   */
  public void reset() {
    this.serial = 1;
    this.entries = new ArrayList();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see oqube.muse.AbstractMuseLinker#link(oqube.muse.MuseSink,
   *      java.lang.String, java.lang.String)
   */
  public void link(MuseSink sink, String link, String text) {
    if(link == null)
      return;
    // check link is of the right from
    if (!link.startsWith("bibtex")) {
      super.link(sink, link, text);
      return;
    }
    System.err.println("linking to" +link);
    // extract various parts 
    // bibtex:<file>:<tag>
    String[] parts = link.split(":");
    // sanity check
    if(parts.length != 3)
      return;
    // first lookup entry
    BibtexEntry entry = entryLookup(parts[2]);
    if(entry == null)
      loadFile(parts[1]);
    // not found - load file
    entry = entryLookup(parts[2]);
    if(entry == null)
      return;
    // found - output link and store reference to entry
    String k = entry.getEntryKey();
    sink.link("#"+k,"["+serial++ +"]");
    entries.add(entry);
  }

  /**
   * @return Returns the serial.
   */
  public int getSerial() {
    return serial;
  }

  /**
   * @param serial The serial to set.
   */
  public void setSerial(int serial) {
    this.serial = serial;
  }

  /**
   * @return Returns the entries.
   */
  public List getEntries() {
    return entries;
  }

  /*
   * load given filename as a bibtex file.
   */
  private void loadFile(String string) {
    try {
      FileReader fr;
      fr = new FileReader(string);
      parser.parse(bibtex,fr);
      // expand everything 
      new MacroReferenceExpander(true,true,true,true).expand(bibtex);
      new PersonListExpander(true,true).expand(bibtex);
      new CrossReferenceExpander().expand(bibtex);
    } catch (ParseException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ExpansionException e) {
      e.printStackTrace();
    }
  }

  /*
   * lookup an entry by key in a bibtex file
   */
  private BibtexEntry entryLookup(String string) {
    for(Iterator i = bibtex.getEntries().iterator();i.hasNext();) {
      BibtexEntry be = (BibtexEntry)i.next();
      if(string.equals(be.getEntryKey()))
          return be;
    }
    return null;
  }
}
