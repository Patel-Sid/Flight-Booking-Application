
package database;

/**
 * An Exception raised if the <code>User</code> does not exist.
 * 
 * @author sid
 *
 */
public class NoSuchClientException extends Exception {


  private static final long serialVersionUID = 319003305680912041L;

  public NoSuchClientException(String message) {
    super(message);
  }

}
