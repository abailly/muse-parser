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

    Created 18 sept. 2006
 */
package oqube.muse;

import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * A class for configuring logging (jdk1.4 style).
 * 
 * @author nono
 *
 */
public class TestLogging {

  public TestLogging() {
    System.err.println("Done configuring logging");
    Logger log = Logger.getLogger("oqube.muse");
    log.setLevel(Level.FINEST);
    Formatter frm = new SimpleFormatter() {
      public synchronized String format(LogRecord record) {
        return record.getMessage() + "\n";
      }
    };
    Handler[] hdls = log.getHandlers();
    for(int i=0;i<hdls.length;i++)
      log.removeHandler(hdls[i]);
    ConsoleHandler hdl = new ConsoleHandler();
    hdl.setLevel(Level.FINEST);
    hdl.setFormatter(frm);
    log.addHandler(hdl);
    System.err.println("Done configuring logging"); 
  }
}
