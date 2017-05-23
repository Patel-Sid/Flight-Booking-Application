package user;

import android.widget.Toast;

import database.ClientDatabase;
import database.Constants;
import database.FlightDatabase;
import database.InvalidFileException;
import database.NoSuchCityException;
import database.NoSuchFlightException;
import flight.Flight;
import flight.FlightItinerary;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cs.b07.cscb07courseproject.R.id.airline;
import static cs.b07.cscb07courseproject.R.id.arrivalTime;
import static cs.b07.cscb07courseproject.R.id.flightNum;
import static database.Constants.dateTime;


/**
 * A class representing a <code>User</code>.
 * 
 * @author richelle
 *
 */

public class User implements Serializable {


  private static final long serialVersionUID = 5471086034356184818L;
  private String firstName;
  private String lastName;
  private String email;
  private String address;
  // private String ccNumber;
  // private String expiryDate;
  // Combined these two into a CreditCard
  private CreditCard creditCard;
  private List<String> bookedFlights;
  private String password;
  private boolean isAdmin;

  // Used for Credit Card Format
  private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");

  /**
   * Constructs a <code>User</code>.
   * 
   * @param firstName the first name of the <code>User</code>
   * @param lastName the last name of the <code>User</code>
   * @param email the email of the <code>User</code>
   * @param address the address of the <code>User</code>
   * @param ccNumber the ccNumber to the <code>CreditCard</code> of the <code>User</code>
   * @param expiryDate the expiry date to the <code>CreditCard</code> of the <code>User</code>
   */
  public User(String firstName, String lastName, String email, String address, String ccNumber,
      String expiryDate, String password) {
    this.setFirstName(firstName);
    this.setLastName(lastName);
    this.setEmail(email);
    this.setAddress(address);
    try {
      this.creditCard = new CreditCard(ccNumber, date.parse(expiryDate));
    } catch (ParseException eeX) {
      eeX.printStackTrace();
    }
    this.bookedFlights = new ArrayList<String>();
    this.password = password;
    this.isAdmin = false;

    try{
      Constants.clientDatabase.addClient(this);
    }
    catch (Exception e){
      e.printStackTrace();
    }
  }



  public static void uploadClientInfo(String path, ClientDatabase database)
      throws InvalidFileException, IOException {

    database.uploadClientInfo(path);
  }

  /**
   * Upload the <code>Flight</code>s to the <code>FlightDatabase</code>.
   * 
   * @param path the location of the text file where the <code>Flight</code> information is stored
   * @param database the <code>FlightDatabase</code> to upload the <code>Flight</code>s
   * @throws InvalidFileException if the file is not a proper text file
   */
  public void uploadFlightInfo(String path, FlightDatabase database) throws InvalidFileException {

    database.uploadFlightInfo(path);

  }

  public void sort() {

  }

  public void makeAdmin(){
    this.isAdmin = true;
  }

  public boolean isAdmin(){
    return this.isAdmin;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAddress() {
    return address;
  }

  public void setAddress(String address) {
    this.address = address;
  }

  public String getCcNumber() {
    return creditCard.getNumber();
  }


  public void setCcNumber(String ccNumber) {
    creditCard.setNumber(ccNumber);;
  }

  public Date getExpiryDate() {
    return creditCard.getExpiry();
  }

  public void setExpiryDate(Date expiryDate) {
    creditCard.setExpiry(expiryDate);;
  }

  public List<String> getbookedFlights() {
    return this.bookedFlights;
  }

  /**
   * Returns all flights that depart from origin and arrive at destination on the given date.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight origin
   * @param destination a flight destination
   * @return the flights that depart from origin and arrive at destination on the given date
   *         formatted in exactly this format:
   *         Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price The dates are
   *         in the format yyyy-MM-dd HH:mm; the price has exactly two decimal places.
   * @throws ParseException if date cannot be parsed
   */
  public static List<Flight> getFlights(String date, String origin, String destination, String sort)
      throws ParseException {
    List<Flight> flights = new ArrayList<Flight>();

    try {
      // 1) get origin city
      List<Flight> originDeparture =
          database.Constants.flightDatabase.getCity(origin).getDepartures();
      List<Flight> destinationArrival =
          database.Constants.flightDatabase.getCity(destination).getArrivals();
      // find common elements
      // now originDeparture only contains common elements
      originDeparture.retainAll(destinationArrival);

      // loop through common flights to find matching date
      for (Flight flight : originDeparture) {
        // get date and convert back to string
        //String departureDate = database.Constants.dateTime.format(flight.getDeparture());
        String departureDate = flight.getDepartureDateTimeString();
        // split it
        String[] dates = departureDate.split(" ");
        // compares
        if (date.equals(dates[0])) {
          // this means matching arrival date
          // add to list
          flights.add(flight);
        }
      }
    } catch (NoSuchCityException eeX) {
      // TODO Auto-generated catch block
      eeX.printStackTrace();
    }

    if (sort.equals("TOTALCOST")) {
      sortCost(flights);
      return flights;

    } else if (sort.equals("TRAVEL")) {
      sortTravel(flights);
      return flights;

    }
    return flights;

  }
  public static void sortTravel(List<Flight> array) {
    boolean swapped = true;
    int j = 0;
    Flight tmp;
    while (swapped) {
      swapped = false;
      j++;
      for (int i = 0; i < array.size() - j; i++) {

        if (array.get(i).getTravelTime() > array.get(i + 1).getTravelTime()) {
          tmp = array.get(i);
          array.set(i, array.get(i + 1));
          array.set(i + 1, tmp);
          swapped = true;
        }
      }
    }
  }
  public static void sortCost(List<Flight> array) {
    boolean swapped = true;
    int j = 0;
    Flight tmp;
    while (swapped) {
      swapped = false;
      j++;
      for (int i = 0; i < array.size() - j; i++) {

        if (array.get(i).getprice() > array.get(i + 1).getprice()) {
          tmp = array.get(i);
          array.set(i, array.get(i + 1));
          array.set(i + 1, tmp);
          swapped = true;
        }
      }
    }
  }

  /**
   * Returns all flights that depart from origin and arrive at destination on the given date.
   *
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight origin
   * @param destination a flight destination
   * @return the flights that depart from origin and arrive at destination on the given date
   *         formatted in exactly this format:
   *         Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price The dates are
   *         in the format yyyy-MM-dd HH:mm; the price has exactly two decimal places.
   * @throws ParseException if date cannot be parsed
   */
  public static ArrayList<String> getFlightsFormatted(String date, String origin, String destination, String sort)
          throws ParseException {
    // call helper function to get list of flights
    List<Flight> validFlights = User.getFlights(date, origin, destination, sort);
    ArrayList<String> returnString = new ArrayList<String>();
    // format all flights
    for (Flight flight : validFlights) {
      String formatted = new String();
      // get hours and mins
      int hours = flight.getTravelTime() / 60;
      int mins = flight.getTravelTime() % 60;
      // Convert dates back
      //String departureDate = database.Constants.dateTime.format(flight.getDeparture());
      String departureDate = flight.getDepartureDateTimeString();
      //String arrivalDate = database.Constants.dateTime.format(flight.getArrival());
      String arrivalDate = flight.getArrivalDateTimeString();
      formatted = String.format("%s;%s;%s;%s;%s;%s;%.2f,%s", flight.getFlightNumber(), departureDate,
              arrivalDate, flight.getAirline(), flight.getOrigin().getName(),
              flight.getDestination().getName(), flight.getprice(), String.format("%02d:%02d", hours, mins));
      // add it to list
      returnString.add(formatted);

      //String oneFlightFormatted = String.format("%s;%s;%s;%s;%s;%s;%.2f;%s",
      //        flightNum, dateTime.format(departure), dateTime.format(arrival),
      //        airline, origin, destination, price, String.format("%02d:%02d", hours, mins));

    }
    return returnString;
  }
  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password){
    this.password = password;
  }

  public void bookFlight(FlightItinerary flight) {
    // convert itineray to string
    bookedFlights.add(flight.toString());
    for (Flight nextFlight: flight.getFlights()){
      try {
        Constants.flightDatabase.getFlight(nextFlight.getFlightNumber()).addFlyer();
      } catch (NoSuchFlightException e) {
        e.printStackTrace();
      }
    }
  }

}
