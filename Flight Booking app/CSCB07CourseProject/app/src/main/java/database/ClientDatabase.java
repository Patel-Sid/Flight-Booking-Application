
package database;

import user.ClientField;
import user.User;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;



/**
 * A class to store all the <code>User</code>'s information.
 *
 * @author sid
 *
 */
public class ClientDatabase extends Database {

  /**
   * ..
   */
  private static final long serialVersionUID = 7122969442779987780L;

  // Contains all clients
  private Map<String, User> clients;

  // Used for Credit Card Format
  private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Creates a new empty <code>ClientDatabase</code>.
   */
  // constructor
  public ClientDatabase() {
    clients = new HashMap<String, User>();
  }

  /**
   * Checks if there is a <code>User</code> in <code>ClientDatabase</code> associated with the given
   * email.
   *
   * @param email the email of the given <code>User</code>
   * @return return True if <code>User</code> already exists
   */
  public boolean hasClient(String email) {
    // Loop through each Client
    for (String nextClient : clients.keySet()) {
      // If the Client's email matches with the given client then return
      // true
      if (nextClient.equalsIgnoreCase(email)) {
        return true;
      }
    }
    // Else we didn't find the Client
    return false;
  }


  /**
   * Checks if there is a <code>User</code> in <code>ClientDatabase</code> that matches the given
   * <code>User</code>.
   *
   * @param client the Client
   * @return return True if Client already exists
   */
  public boolean hasClient(User client) {
    for (String nextClient : clients.keySet()) {
      if (clients.get(nextClient) == client) {
        return true;
      }
    }
    return false;
  }

  /**
   * Add a new <code>User</code> to the <code>ClientDatabase</code>.
   *
   * @param firstName the first name of the <code>User</code>
   * @param lastName the last name of the <code>User</code>
   * @param email the email of the <code>User</code>
   * @param address the address of the <code>User</code>
   * @param ccNumber the ccNumber of the <code>User</code>'s <code>CreditCard</code>
   * @param expiryDate the expiry date of the <code>User</code>'s <code>CreditCard</code>
   * @throws ClientExistsException if the <code>User</code> is already in the
   *         <code>ClientDatabase</code>
   */
  public void addClient(String firstName, String lastName, String email, String address,
                        String ccNumber, String expiryDate, String password) throws ClientExistsException {
    // Checks if client already exists in the database
    if (hasClient(email)) {
      throw new ClientExistsException(String.format("Client %s already exists.", email));
    }
    // Else we need to create a new Client and add it to our database
    User newClient = new User(firstName, lastName, email, address, ccNumber, expiryDate, password);
    clients.put(email, newClient);
  }

  public void addClient(User newUser) throws ClientExistsException{
    if (hasClient(newUser.getEmail())){
      throw new ClientExistsException("Client already exists.");
    }
    clients.put(newUser.getEmail(), newUser);
  }

  /**
   * Search for a given <code>User</code> in the <code>ClientDatabase</code>.
   *
   * @param email the email of the client
   * @return the Client associated with the given email
   */

  public User searchClientEmail(String email) throws NoSuchClientException {
    // Loop through all the clients in the database
    for (String nextClient : clients.keySet()) {
      // If the client's email match with the given email then we have our
      // client
      if (nextClient.equalsIgnoreCase(email)) {
        return clients.get(nextClient);
      }
    }
    // If no such Client exists in the database then raise
    // NoSuchClientException
    throw new NoSuchClientException(String.format("Client %s not found.", email));

  }

  /**
   * Edit the clientField of the given <code>User</code> with the value provided.
   *
   * @param client the <code>User</code>
   * @param clientField the field that needs to changed
   * @param value the value that the field will be changed with
   * @throws NoSuchClientException if the <code>User</code> given does not exist
   * @throws InvalidInputException if the value is not the same type as the clientField
   * @throws ParseException if a date String is given and it cannot be parsed due to wrong format
   */
  public void editClient(User client, ClientField clientField, Object value)
          throws NoSuchClientException, InvalidInputException, ParseException {
    // Check to see if the Client doesn't exist in the database and throw an
    // exception
    if (!(hasClient(client))) {
      throw new NoSuchClientException(String.format("Client %s does not exist.", client));
    }
    // If the given field is address, then check if a valid string is
    // entered and make changes accordingly
    if (clientField == ClientField.ADDRESS) {
      if (value instanceof String) {
        client.setAddress((String) value);
      } else {
        throw new InvalidInputException("Address must be a String.");
      }
      // If the given field is firstname, then check if a valid string is
      // entered and make changes accordingly
    } else if (clientField == ClientField.FIRSTNAME) {
      if (value instanceof String) {
        client.setFirstName((String) value);
      } else {
        throw new InvalidInputException("First Name must be a String");
      }
      // If the given field is lastname, then check if a valid string is
      // entered and make changes accordingly
    } else if (clientField == ClientField.LASTNAME) {
      if (value instanceof String) {
        client.setLastName((String) value);
      } else {
        throw new InvalidInputException("Last Name must be a String");
      }
      // If the given field is creditcard, then check if a valid string is
      // entered and make changes accordingly
    } else if (clientField == ClientField.CREDITCARD) {
      if (value instanceof String) {
        client.setCcNumber((String) value);
      } else {
        throw new InvalidInputException("ccNumber must be a String");
      }
      // If the given field is expirydate, then check if a valid string is
      // entered and make changes accordingly
    } else if (clientField == ClientField.EXPIRYDATE) {
      if (value instanceof String) {
        client.setExpiryDate(date.parse((String) value));
      } else {
        throw new InvalidInputException("Expiry Date must be a String");
      }
    } else if (clientField == ClientField.PASSWORD) {
      if (value instanceof String) {
        client.setPassword((String) value);
      } else {
        throw new InvalidInputException("Expiry Date must be a String");
      }
    }
  }

  /**
   * Uploads all the <code>User</code>s in a text file.
   *
   * @param path the location of the text file
   * @throws InvalidFileException if the file is not a text file
   * @throws IOException if the file does not exist
   */
  public void uploadClientInfo(String path) throws InvalidFileException, IOException {
    // Open the file
    File file = new File(path);
    try {
      Scanner clientfile = new Scanner(file);
      // Loop through the entire file and separate information at ;
      while (clientfile.hasNextLine()) {
        String nextLine = clientfile.nextLine();
        String deliminator = ";";
        String[] nextClient = nextLine.split(deliminator);
        User newUser = new User(nextClient[1], nextClient[0], nextClient[2], nextClient[3],
                nextClient[4], nextClient[5], "");
        // Add the client if it doesn't exist otherwise throw the
        // exception
        clients.put(nextClient[2], newUser);
        File clientFile = new File(nextClient[2] + ".txt");
        if (!clientFile.exists()) {
          clientFile.createNewFile();
        }
      }
      clientfile.close();
    } catch (FileNotFoundException eeX) {
      eeX.printStackTrace();
    }
  }

  /**
   * Uploads all the <code>User</code>s in a text file.
   *
   * @param is the input stream
   * @throws InvalidFileException if the file is not a text file
   * @throws IOException if the file does not exist
   */
  public void uploadClientInfoStream(InputStream is) throws IOException {
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder out = new StringBuilder();
    String line;
    // create user
    try {
      while ((line = reader.readLine()) != null) {
        String[] nextClient = line.split(";");
        User newUser = new User(nextClient[1], nextClient[0], nextClient[2], nextClient[3],
                nextClient[4], nextClient[5], "");
        // Add the client if it doesn't exist otherwise throw the
        // exception
        clients.put(nextClient[2], newUser);

      }
    } catch (IOException ex) {
      ex.printStackTrace();
    }
    reader.close();
  }

  /**
   * Checks if any two clients are same.
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    ClientDatabase other = (ClientDatabase) obj;
    return (clients == null ? other.clients == null : clients.equals(other.clients));
  }
}
