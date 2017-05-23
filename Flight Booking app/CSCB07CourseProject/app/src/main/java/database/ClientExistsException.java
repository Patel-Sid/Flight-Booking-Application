
package database;

/**
 * An Exception that indicates that the <code>User</code> already exists.
 *
 * @author sid
 *
 */
public class ClientExistsException extends Exception {

  static final long serialVersionUID = -7228407291905626565L;

  /**
   * Constructs a new <code>ClientExistsException</code> with an optional message.
   *
   */
  public ClientExistsException(String message) {
    super(message);
  }

}
