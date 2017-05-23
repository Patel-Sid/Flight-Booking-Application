package database;

/**
 * Indicates an attempt to set an value of incorrect type.
 * 
 * @author bri
 *
 */
public class InvalidInputException extends Exception {


  private static final long serialVersionUID = -5682039279081139488L;

  /**
   * Constructs a new <code>InvalidInputException</code> with an optional message.
   */
  public InvalidInputException(String message) {
    super(message);
  }

}
