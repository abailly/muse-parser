/**
 *  Copyright (C) 2008 - OQube / Arnaud Bailly
    Licensed under MIT open-source license: 
    see http://www.opensource.org/licenses/mit-license.php
    Created 14 nov. 08
 */
package oqube.muse.events;

/**
 * An interface for receiving events.
 * 
 * @author nono
 * 
 */
public interface EventSink<T> {

  /**
   * A sink handles an event, eventually processes it, transforms it and yields
   * some resulting value.
   * 
   * @param event
   *          to be processed by the sink.
   * @return some result that is processed by the sink.
   */
  T handle(SinkEvent event);

}
