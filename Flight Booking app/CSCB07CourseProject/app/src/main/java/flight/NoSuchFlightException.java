package flight;

public class NoSuchFlightException extends Exception {

  private static final long serialVersionUID = -543029436570932093L;

  public NoSuchFlightException() {}

  /**
   * Creates a new <code>NoSuchFlightException</code> with the given message.
   * 
   * @param message the message for this <code>NoSuchFlightException</code>
   */
  public NoSuchFlightException(String message) {
    super(message);
  }
}
