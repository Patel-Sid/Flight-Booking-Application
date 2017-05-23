package user;

/**
 * A class representing a <code>Aministrator</code>.
 * 
 * @author richelle
 *
 */

public class Administrator extends User {

  /**
   * ..
   */
  private static final long serialVersionUID = -9093111656950118782L;

  /**
   * Constructor for <code>Administrator</code> object.
   *
   * @param email the admin's login
   * @param password the password
   */

  public Administrator(String email, String password) {
    super("Admin", "", email, "", "", "2000-01-01", password);

  }

}
