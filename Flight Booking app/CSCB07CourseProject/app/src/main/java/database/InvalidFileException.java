package database;

/**
 * An exception raised when an invalid database file is given.
 * 
 * @author bri
 *
 */
public class InvalidFileException extends Exception {

  /**
   * Serialization.
   */
  private static final long serialVersionUID = -8665375647913790768L;

  /**
   * Constructs a new <code>InvalidFileException</code> with an optional message.
   */
  public InvalidFileException(String message) {
    super(message);
  }
}
