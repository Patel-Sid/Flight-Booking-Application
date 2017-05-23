
package database;

/**
 * Indicates an attempt to create a <code>Flight</code> that already exists.
 * 
 * @author bri
 *
 */
public class PreexistingFlightException extends Exception {


  private static final long serialVersionUID = 368716355273969116L;

  /**
   * Constructs a new <code>PreexistingFlightException</code> with an optional message.
   */
  public PreexistingFlightException(String message) {
    super(message);
  }
}
