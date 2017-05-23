package flight;

public class InvalidFlightAddition extends Exception {

  private static final long serialVersionUID = -5900812497696773049L;

  public InvalidFlightAddition() {}

  /**
   * Creates a new <code>InvalidFlightAddition</code> with the given message.
   * 
   * @param message the message for this <code>InvalidFlightAddition</code>
   */
  public InvalidFlightAddition(String message) {
    super(message);
  }
}
