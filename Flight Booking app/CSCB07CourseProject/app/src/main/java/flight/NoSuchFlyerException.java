package flight;

public class NoSuchFlyerException extends Exception {

  private static final long serialVersionUID = -1986594014467501368L;

  public NoSuchFlyerException() {}

  /**
   * Creates a new <code>NoSuchFlyerException</code> with the given message.
   * 
   * @param message the message for this <code>NoSuchFlyerException</code>
   */
  public NoSuchFlyerException(String message) {
    super(message);
  }

}
