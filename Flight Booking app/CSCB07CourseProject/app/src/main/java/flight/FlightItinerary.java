
package flight;

import city.City;
import database.Constants;
import database.NoSuchCityException;
import database.SortOrder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The class representing a Flight Itinerary, which is a list of Flights.
 * 
 * @author jason
 *
 */
public class FlightItinerary implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1521596522537376459L;
  private List<Flight> flights;
  // private ItineraryDatabase itinDatabase = new ItineraryDatabase();

  /**
   * Constructor for FlightItinerary, creates a new Itinerary.
   */
  public FlightItinerary() {
    // create an array list of all flights in this itinerary
    flights = new ArrayList<Flight>();
    // add it into fight itineraryDatabase
    // will need to initialize itinDatabase at start of program
    // itinDatabase.add(this);
  }

  /**
   * Adds the given <code>Flight</code> into the itinerary.
   * 
   * @param flight the <code>Flight</code> to add
   */
  public void addFlight(Flight flight) {
    flights.add(flight);
  }

  /**
   * List all of the flights in this itinerary by flight number
   * 
   * @return a list of flight numbers that are in this itinerary.
   */
  public List<String> listFlights() {
    // create a list of the flight numbers
    List<String> flightNums = new ArrayList<String>();
    // loop through all flights in this itinerary
    for (Flight flight : flights) {
      flightNums.add(flight.getFlightNumber());
    }
    return flightNums;
  }

  /**
   * Returns the flight associated with the given flightNumber.
   * 
   * @param flightNumber the flight we are looking for.
   * @return a Flight with flightNumber given.
   * @throws NoSuchFlightException if no such flight exists in this itinerary with flightNumber
   */
  public Flight getFlight(String flightNumber) throws NoSuchFlightException {
    Flight desiredFlight = null;
    for (Flight flight : flights) {
      if (flight.getFlightNumber() == flightNumber) {
        desiredFlight = flight;
        break;
      }
    }
    if (desiredFlight == null) {
      throw new NoSuchFlightException("Flight " + flightNumber + " is not in this itinerary.");
    } else {
      return desiredFlight;
    }
  }

  /**
   * Removes the flight from the itinerary.
   * 
   * @param flight to be removed
   * @throws NoSuchFlightException if the flight does not exist
   */
  protected void remove(Flight flight) throws NoSuchFlightException {
    if (!(flights.remove(flight))) {
      throw new NoSuchFlightException("Flight does not exist");
    }

  }

  /**
   * Get the departure date of first <code>Flight</code>.
   * 
   * @return the date in which this <code>Flight</code> first departs
   */
  public Calendar getDeparture() {
    return flights.get(0).getDeparture();
  }

  /**
   * Get the arrival date, which is the last <code>Flight</code>'s arrival date.
   * 
   * @return the date in which the last <code>Flight</code> arrives
   */
  public Calendar getArrival() {
    return flights.get(flights.size() - 1).getArrival();
  }

  /**
   * Get the first <code>City</code> the <code>Flight</code> departs from.
   * 
   * @return the <code>City</code> departs from.
   */
  public City getOrigin() {
    return flights.get(0).getOrigin();
  }

  /**
   * Get the last <code>City</code> the <code>Flight</code> is set to land.
   * 
   * @return the last <code>City</code> the <code>Flight</code> is set to land.
   */
  public City getDestination() {
    // return flights.get(flights.size() - 1).getDestination();
    return this.getLastFlight().getDestination();
  }

  /**
   * Returns the last <code>Flight</code> in this <code>FlightItinerary</code>.
   * 
   * @return the last <code>Flight</code> in this <code>FlightItinerary</code>.
   */
  public Flight getLastFlight() {
    return flights.get(flights.size() - 1);
  }

  public Flight getFirstFlight() {
    return flights.get(0);
  }

  /**
   * Return the total cost of this <code>FlightItinerary</code>, which is the summation of the cost
   * of each flights.
   * 
   * @return a double representing the total cost of all the flights.
   */
  public double totalPrice() {
    double totalCost = 0;
    for (Flight flight : flights) {
      totalCost += flight.getprice();
    }
    return totalCost;
  }

  /**
   * Return the total flying time in this <code>FlightItinerary</code>, which is the summation of
   * all travel time of all flights.
   * 
   * @return an int representing the total travel time of all the flights.
   */
  public int travelTime() {
    /**
     * int totalMinutes = 0; for (Flight flight : flights) { totalMinutes += flight.getTravelTime();
     * } // going to leave this in minutes, until we learn more about what this // is used in.
     * return totalMinutes;
     */
    Flight lastFlight = flights.get(flights.size() - 1);
    Flight firstFlight = flights.get(0);
    // get time
    Calendar last = lastFlight.getArrival();
    Calendar first = firstFlight.getDeparture();
    // get duration
    long start = first.getTimeInMillis();
    long stop = last.getTimeInMillis();
    long diff = stop - start;
    long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
    return (int) minutes;

  }

  /**
   * Check if any <code>Flight</code>s in this <code>FlightItinerary</code> visits the given
   * <code>City</code>.
   * 
   * @param city the <code>City</code> we want to check for
   * @return True iff one <code>Flight</code> in this <code>FlightItinerary</code> visits the city
   */
  public boolean visitsCity(City city) {
    if (getOrigin() == city) {
      return true;
    }
    for (Flight nextFlight : flights) {
      if (nextFlight.getDestination() == city) {
        return true;
      }
    }
    return false;
  }

  public List<Flight> getFlights() {
    return flights;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((flights == null) ? 0 : flights.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    FlightItinerary other = (FlightItinerary) obj;
    if (flights == null) {
      if (other.flights != null)
        return false;
    } else if (!flights.equals(other.flights))
      return false;
    return true;
  }

  @Override
  public String toString() {
    String returnString = "";
    // format
    long duration = this.getLastFlight().getArrival().getTimeInMillis()
        - this.getFirstFlight().getDeparture().getTimeInMillis();
    long hours = TimeUnit.MILLISECONDS.toHours(duration);
    long mins = TimeUnit.MILLISECONDS.toMinutes(duration)
        - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(duration));
    // format each flight
    String formatReturn = "";
    // count number of runs
    int runs = 0;
    for (Flight flight : this.getFlights()) {
      if (runs > 0) {
        formatReturn += "\n";
      }
      String add = formatFlight(flight);
      formatReturn += add;
      runs += 1;
    }
    returnString += formatReturn;
    // add the itinerary
    String itineraryFormatted =
        String.format("%.2f\n%s", this.totalPrice(), String.format("%02d:%02d", hours, mins));
    returnString +="\n" + itineraryFormatted;
    return returnString;
  }


  private static String formatFlight(Flight flight) {
    /**
     * String flight1Formatted = String.format("%s;%s;%s;%s;%s;%s", flightNum1,
     * dateTime.format(departure1), dateTime.format(arrival1), airline1, origin, stopover);
     */
    String formatted = String.format("%s;%s;%s;%s;%s;%s", flight.getFlightNumber(),
        flight.getDepartureDateTimeString(), flight.getArrivalDateTimeString(), flight.getAirline(),
        flight.getOrigin().getName(), flight.getDestination().getName());
    return formatted;
  }

}
