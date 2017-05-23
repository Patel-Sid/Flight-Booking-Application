package database;

/**
 * Indicates an attempt to access a <code>Flight</code> that does not exist.
 * 
 * @author bri
 *
 */
public class NoSuchFlightException extends Exception {


  private static final long serialVersionUID = 3423948947918060722L;

  /**
   * Constructs a new <code>NoSuchFlightException</code> with an optional message.
   */
  public NoSuchFlightException(String message) {
    super(message);
  }
}
