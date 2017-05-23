
package database;

/**
 * Indicates an attempt to access a <code>City</code> that does not exist.
 * 
 * @author bri
 *
 */
public class NoSuchCityException extends Exception {

  /**
   * Serialization.
   */
  private static final long serialVersionUID = 8303645746958998397L;

  /**
   * Constructs a new <code>NoSuchCityException</code> with an optional message.
   */
  public NoSuchCityException(String message) {
    super(message);
  }

}
