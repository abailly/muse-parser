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

    Created 19 sept. 2006
 */
package oqube.muse;

import java.util.HashMap;
import java.util.Map;

import oqube.muse.html.HTMLPublisher;
import oqube.muse.html.SlidePublisher;
import oqube.muse.muse.MusePublisher;
import oqube.muse.trac.TracPublisher;

/**
 * Centralized class for attaching and constructing instances
 * of muse publishing system with all configured elements.
 * This class offers static method to register and retrieve 
 * {@link Publisher} instances with a tagged name.
 * At present, this is done in an adhoc maneer but provision is made
 * to use a container such as plexus for doing that.
 * 
 * @author nono
 */
public class PublisherFactory {

  private static Map publishers = new HashMap();
  
  static {
    PublisherFactory.register(new HTMLPublisher(),"xhtml");
    PublisherFactory.register(new MusePublisher(),"muse");
    PublisherFactory.register(new TracPublisher(),"trac");
    PublisherFactory.register(new SlidePublisher(),"slide");
  }
  
    
  /**
   * Register a publisher instance with given name.
   * 
   * @param pub a Publisher instance.
   * @param name identifier for this instance.
   */
  public static void register(Publisher pub, String name) {
    publishers.put(name,pub);
  }
  
  /**
   * Return a publisher instance withe given name.
   * 
   * @param name name of instance. 
   * @return a Publisher. May be null if name is non registered.
   */
  public static Publisher instance(String name) {
    return (Publisher)publishers.get(name);
  }
  
}
