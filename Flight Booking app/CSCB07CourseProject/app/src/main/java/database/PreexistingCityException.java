
package database;

/**
 * Indicates an attempt to create a <code>City</code> that already exists.
 * 
 * @author bri
 *
 */
public class PreexistingCityException extends Exception {


  private static final long serialVersionUID = -476419404914384335L;

  /**
   * Constructs a new <code>PreexistingCityException</code> with an optional message.
   */
  public PreexistingCityException(String message) {
    super(message);
  }
}
