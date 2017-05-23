package database;

import city.City;
import flight.Flight;
import flight.FlightFields;
import flight.FlightItinerary;
import flight.InvalidFlightAddition;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import javax.xml.datatype.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;
import static database.Constants.date;
import static java.lang.Math.toIntExact;




/**
 * A database used to store and manage <code>Flight</code> data.
 * 
 * @author bri
 *
 */
public class FlightDatabase extends Database {
  /**
   * ..
   */
  private static final long serialVersionUID = 819096623013076884L;
  private static Map<String, Flight> flights; // Contains all flights
  private static Map<String, City> cities; // Contains all cities

  private static DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  private static long minLayover_milli = 30 * 60 * 1000; // 30 min in milliseconds
  private static long maxLayover_milli = 6 * 60 * 60 * 1000;// 6 hours in milliseconds


  /**
   * Creates a new empty <code>FlightDatabase</code>.
   */
  public FlightDatabase() {
    flights = new HashMap<String, Flight>();
    cities = new HashMap<String, City>();
  }

  /**
   * Checks if the <code>City</code> is in this <code>FlightDatabase</code>.
   * 
   * @param cityName Name of the city.
   * @return True of the <code>City</code> is found.
   */
  public static boolean hasCity(String cityName) {
    for (String nextCity : cities.keySet()) {
      if (nextCity.equalsIgnoreCase(cityName)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the <code>City</code> is in this <code>FlightDatabase</code>.
   * 
   * @param city The City to search for.
   * @return True of the <code>City</code> is found.
   */
  public static boolean hasCity(City city) {
    for (String nextCity : cities.keySet()) {
      if (city == cities.get(nextCity)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Adds a new <code>City</code> to this <code>FlightDatabase</code>.
   * 
   * @param cityName Name of the City to add.
   * @throws PreexistingCityException if a <code>City</code> with the same name exists.
   */
  public static void addCity(String cityName) throws PreexistingCityException {
    // Make sure city doesn't exist
    if (hasCity(cityName)) {
      throw new PreexistingCityException(String.format("City %s already exists.", cityName));
    }
    // Otherwise, make new city and add to network
    City newCity = new City(cityName);
    cities.put(cityName, newCity);
  }

  /**
   * Adds a new <code>City</code> to this <code>FlightDatabase</code>.
   * 
   * @param newCity New City to add.
   * @throws PreexistingCityException if a <code>City</code> with the same name exists.
   */
  public static void addCity(City newCity) throws PreexistingCityException {
    // Make sure city doesn't already exist
    if (hasCity(newCity.getName())) {
      throw new PreexistingCityException(
          String.format("City %s already exists.", newCity.getName()));
    }
    // Otherwise, add the city to the network
    cities.put(newCity.getName(), newCity);
  }

  /**
   * Gets <code>City</code> from <code>FlightDatabase</code>.
   * 
   * @param city Name of the City to get.
   * @return The <code>City</code> object with name city.
   * @throws NoSuchCityException If the City can not be found.
   */
  public static City getCity(String city) throws NoSuchCityException {
    // Loops through each city in cities and return if the city name matches
    for (String nextCity : cities.keySet()) {
      if (nextCity.equalsIgnoreCase(city)) {
        return cities.get(city);
      }
    }
    // If city not found, raise NoSuchCityException
    throw new NoSuchCityException(String.format("City %s not found.", city));
  }

  /**
   * Checks if a <code>Flight</code> is in this <code>FlightDatabase</code>.
   * 
   * @param flightNum Flight number of the <code>Flight</code>.
   * @return True of the <code>Flight</code> was found.
   */
  private static boolean hasFlight(String flightNum) {
    for (String nextFlight : flights.keySet()) {
      if (nextFlight.equalsIgnoreCase(flightNum)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if a <code>Flight</code> is in this <code>FlightDatabase</code>.
   * 
   * @param flight <code>Flight</code> to look for.
   * @return True of the <code>Flight</code> was found.
   */
  private static boolean hasFlight(Flight flight) {
    for (String nextFlight : flights.keySet()) {
      if (flights.get(nextFlight) == flight) {
        return true;
      }
    }
    return false;
  }

  /**
   * Add a new <code>Flight</code> to this <code>FlightDatabase</code>.
   * 
   * @param newFlight New <code>Flight</code> to add.
   * @throws PreexistingFlightException If a <code>Flight</code> with the same flight number exists.
   */
  public static void addFlight(Flight newFlight) throws PreexistingFlightException {
    // Check if flight already exists
    if (hasFlight(newFlight.getFlightNumber())) {
      throw new PreexistingFlightException(
          String.format("Flight number %s already exists", newFlight.getFlightNumber()));
    }
    // Make sure the Origin and Destination Cities exist. If they don't,
    // create and add them
    if (!(hasCity(newFlight.getOrigin().getName()))) {
      try {
        addCity(newFlight.getOrigin());
      } catch (PreexistingCityException eeX) {
        // Shouldn't Happen
      }
    } else {
      try {
        String originName = newFlight.getOrigin().getName();
        newFlight.setOrigin(getCity(originName));
      } catch (NoSuchCityException eeX) {
        // Shouldn't Happen
      }
    }
    if (!(hasCity(newFlight.getDestination().getName()))) {
      try {
        addCity(newFlight.getDestination());
      } catch (PreexistingCityException eeX) {
        // Shouldn't Happen
      }
    } else {
      try {
        String destName = newFlight.getDestination().getName();
        newFlight.setDestination(getCity(destName));
      } catch (NoSuchCityException eeX) {
        // Shouldn't Happen
      }
    }
    // Get origin and destination cities
    City origin;
    City destination;
    try {
      origin = getCity(newFlight.getOrigin().getName());
      destination = getCity(newFlight.getDestination().getName());
    } catch (NoSuchCityException eeX) {
      // Shouldn't Happen
      origin = null;
      destination = null;
    }
    // Add new Flight to flights
    flights.put(newFlight.getFlightNumber(), newFlight);
    // Link the origin and destination cities with newFlight
    origin.addDeparture(newFlight);
    destination.addArrival(newFlight);
  }

  /**
   * Gets a <code>Flight</code> based on a flight number.
   * 
   * @param flightNum The Flight number of the <code>Flight</code> to look for.
   * @return The <code>Flight</code> object with flight number flightNum.
   * @throws NoSuchFlightException If the <code>Flight</code> is not found.
   */
  public static Flight getFlight(String flightNum) throws NoSuchFlightException {
    // Loops through each flight in flights and return if flight number
    // matches
    for (String nextFlight : flights.keySet()) {
      if (nextFlight.equalsIgnoreCase(flightNum)) {
        return flights.get(flightNum);
      }
    }
    // If no flight found, raise NoSuchFlightException
    throw new NoSuchFlightException(String.format("Flight %s not found.", flightNum));
  }

  /**
   * Returns all <code>Flight</code>s departing from a specific <code>City</code>.
   * 
   * @param city City of origin.
   * @return a <code>List</code> of <code>Flight</code>s.
   * @throws NoSuchCityException If the <code>City</code> does not exist.
   */
  public static List<Flight> searchOrigin(City city) throws NoSuchCityException {
    if (hasCity(city)) {
      return city.getDepartures();
    } else {
      throw new NoSuchCityException(String.format("Could not find %s City.", city.getName()));
    }
  }

  /**
   * Returns all <code>Flight</code>s departing from a specific <code>City</code>.
   * 
   * @param cityName Name of the City of origin.
   * @return a <code>List</code> of <code>Flight</code>s.
   * @throws NoSuchCityException If the <code>City</code> does not exist.
   */
  public static List<Flight> searchOrigin(String cityName) throws NoSuchCityException {
    if (hasCity(cityName)) {
      return cities.get(cityName).getDepartures();
    } else {
      throw new NoSuchCityException(String.format("Could not find %s City.", cityName));
    }
  }

  /**
   * Returns all <code>Flight</code>s arriving at a specific <code>City</code>.
   * 
   * @param city Destination City.
   * @return a <code>List</code> of <code>Flight</code>s.
   * @throws NoSuchCityException If the <code>City</code> does not exist.
   */
  public static List<Flight> searchDestination(City city) throws NoSuchCityException {
    if (hasCity(city)) {
      return city.getArrivals();
    } else {
      throw new NoSuchCityException(String.format("Could not find %s City.", city.getName()));
    }
  }

  /**
   * Returns all <code>Flight</code>s arriving at a specific <code>City</code>.
   * 
   * @param cityName Name of the City.
   * @return a <code>List</code> of <code>Flight</code>s.
   * @throws NoSuchCityException If the <code>City</code> does not exist.
   */
  public static List<Flight> searchDesination(String cityName) throws NoSuchCityException {
    if (hasCity(cityName)) {
      return cities.get(cityName).getArrivals();
    } else {
      throw new NoSuchCityException(String.format("Could not find %s City.", cityName));
    }
  }

  /**
   * Edits a <code>Flight</code>'s information.
   * 
   * @param flight The <code>Flight</code> to modify.
   * @param flightField The enumeration of Flight fields to modify.
   * @param value The value to change in the Flight.
   * @throws NoSuchFlightException If the Flight does not exist.
   * @throws InvalidInputException If the new value is not valid.
   * @throws PreexistingFlightException If the user tries to change the Flight number to a
   *         preexisting flight number.
   */
  public static void editFlight(Flight flight, FlightFields flightField, Object value)
      throws NoSuchFlightException, InvalidInputException, PreexistingFlightException {
    // If the flight doesn't exist in the database then throw an exception
    if (!(hasFlight(flight))) {
      throw new NoSuchFlightException(
          String.format("Flight %s does not exist.", flight.getFlightNumber()));
    }
    // If Flight number needs to be edited then check if it is a valid
    // string then only modify it
    if (flightField == FlightFields.FLIGHTNUM) {
      if (value instanceof String) {
        // Check if the flight already exists in the database and throw
        // an exception if it does
        // Otherwise add it to the database
        if (!(hasFlight((String) value))) {
          flight.setFlightNumber((String) value);
        } else {
          throw new PreexistingFlightException(
              String.format("Flight Number %s already exists", value));
        }
        // Otherwise a valid string wasn't entered
      } else {
        throw new InvalidInputException("Flight Number must be a String.");
      }
      // If origin needs to be edited then check if it is a valid City
      // then only modify it
    } else if (flightField == FlightFields.ORIGIN) {
      if (value instanceof City) {
        flight.setOrigin((City) value);
        // Otherwise a valid City wasn't entered
      } else {
        throw new InvalidInputException("Origin must be a City");
      }
      // If destination needs to be edited then check if it is a valid
      // City then only modify it
    } else if (flightField == FlightFields.DESTINATION) {
      if (value instanceof City) {
        flight.setDestination((City) value);
        // Otherwise a valid City wasn't entered
      } else {
        throw new InvalidInputException("Destination must be a City");
      }
      // If departure needs to be edited then check if it is a valid Date
      // then only modify it
    } else if (flightField == FlightFields.DEPARTURE) {
      if (value instanceof Calendar) {
        flight.setDeparture((Calendar) value);
        // Otherwise a valid Date wasn't entered
      } else {
        throw new InvalidInputException("Departure must be a Date");
      }
      // If arrival needs to be edited then check if it is a valid Date
      // then only modify it
    } else if (flightField == FlightFields.ARRIVAL) {
      if (value instanceof Calendar) {
        flight.setDeparture((Calendar) value);
        // Otherwise a valid Date wasn't entered
      } else {
        throw new InvalidInputException("Arrival must be a Date");
      }
      // If airline needs to be edited then check if it is a valid String
      // then only modify it
    } else if (flightField == FlightFields.AIRLINE) {
      if (value instanceof String) {
        flight.setAirline((String) value);
      } else {
        throw new InvalidInputException("Airline Number must be a String.");
      }
      // Otherwise a valid String wasn't entered
    } else if (flightField == FlightFields.TOTALCOST) {
      if (value instanceof Double) {
        flight.setPrice((Double) value);
      } else {
        throw new InvalidInputException("Airline Number must be a Double.");
      }
      // Otherwise a valid String wasn't entered
    } else if (flightField == FlightFields.NUMSEATS) {
      if (value instanceof Integer) {
        flight.setNumSeats((Integer) value);
      } else {
        throw new InvalidInputException("Airline Number must be an Integer.");
      }
      // Otherwise a valid String wasn't entered
    } else {
      throw new InvalidInputException("Invalid Flight Field.");
    }
  }

  /**
   * Edits a <code>Flight</code>'s information.
   * 
   * @param flightNumber The number of the <code>Flight</code> to modify.
   * @param flightField The enumeration of Flight fields to modify.
   * @param value The value to change in the Flight.
   * @throws NoSuchFlightException If the Flight does not exist.
   * @throws InvalidInputException If the new value is not valid.
   * @throws PreexistingFlightException If the user tries to change the Flight number to a
   *         preexisting flight number.
   */
  public static void editFlight(String flightNumber, FlightFields flightField, Object value)
      throws NoSuchFlightException, InvalidInputException, PreexistingFlightException {

    Flight flight = getFlight(flightNumber);

    if (flightField == FlightFields.FLIGHTNUM) {
      if (value instanceof String) {
        if (!(flightNumber.equalsIgnoreCase((String) value))) {
          flight.setFlightNumber((String) value);
        } else {
          throw new PreexistingFlightException(
              String.format("Flight %s already exists.", flightNumber));
        }
      } else {
        throw new InvalidInputException("Flight Number must be a String.");
      }
    } else if (flightField == FlightFields.ORIGIN) {
      if (value instanceof City) {
        flight.setOrigin((City) value);
      } else {
        throw new InvalidInputException("Origin must be a City");
      }
    } else if (flightField == FlightFields.DESTINATION) {
      if (value instanceof City) {
        flight.setDestination((City) value);
      } else {
        throw new InvalidInputException("Origin must be a City");
      }
    } else if (flightField == FlightFields.DEPARTURE) {
      if (value instanceof Calendar) {
        flight.setDeparture((Calendar) value);
      } else {
        throw new InvalidInputException("Departure must be a Date");
      }
    } else if (flightField == FlightFields.ARRIVAL) {
      if (value instanceof Calendar) {
        flight.setDeparture((Calendar) value);
      } else {
        throw new InvalidInputException("Arrival must be a Date");
      }
    } else if (flightField == FlightFields.AIRLINE) {
      if (value instanceof String) {
        flight.setAirline((String) value);
      } else {
        throw new InvalidInputException("Airline Number must be a String.");
      }
    } else {
      throw new InvalidInputException("Invalid Flight Field.");
    }
  }

  /**
   * Upload all the flight information given the correct path.
   * 
   * @param path the path of the file
   * @throws InvalidFileException If the file is not found
   */
  public static void uploadFlightInfo(String path) throws InvalidFileException {
    // Open the file
    File file = new File(path);
    try {
      Scanner flightfile = new Scanner(file);
      // Loop through the text file and separate information at ;
      while (flightfile.hasNextLine()) {
        String nextLine = flightfile.nextLine();
        String deliminator = ";";
        String[] nextFlight = nextLine.split(deliminator);
        Flight newFlight;
        try {
          Date dep = dateTime.parse(nextFlight[1]);
          Date arr = dateTime.parse(nextFlight[2]);
          Calendar departure = Calendar.getInstance();
          departure.setTime(dep);
          Calendar arrival = Calendar.getInstance();
          arrival.setTime(arr);
          newFlight = new Flight(nextFlight[0], departure,
              arrival, new City(nextFlight[4]), new City(nextFlight[5]),
              nextFlight[3], Double.parseDouble(nextFlight[6]), Integer.parseInt(nextFlight[7]));
        } catch (NumberFormatException eeX) {
          flightfile.close();
          throw new InvalidFileException("Price format in file incorrect.");
        } catch (ParseException eeX) {
          flightfile.close();
          throw new InvalidFileException("Date format in file incorrect.");
        }
        // Add the flight if it doesn't exist otherwise throw the
        // exception
        try {
          addFlight(newFlight);
        } catch (PreexistingFlightException eeX) {
          System.out.println(String.format("Flight %s already exists. Skipped.", nextFlight[0]));
        }
      }
      // Close the file
      flightfile.close();
      // Other file was not found so throw exception
    } catch (FileNotFoundException eeX) {
      eeX.printStackTrace();
    }

  }

  public static void uploadFlightInfoStream(InputStream is) throws IOException, InvalidFileException{
    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
    StringBuilder out = new StringBuilder();
    String line;
    try {
      Flight newFlight;
      while ((line = reader.readLine()) != null) {
        String[] nextFlight = line.split(";");
        Date dep = dateTime.parse(nextFlight[1]);
        Date arr = dateTime.parse(nextFlight[2]);
        Calendar departure = Calendar.getInstance();
        departure.setTime(dep);
        Calendar arrival = Calendar.getInstance();
        arrival.setTime(arr);
        newFlight = new Flight(nextFlight[0], departure,
                arrival, new City(nextFlight[4]), new City(nextFlight[5]),
                nextFlight[3], Double.parseDouble(nextFlight[6]), Integer.parseInt(nextFlight[7]));
        try {
          addFlight(newFlight);
        } catch (PreexistingFlightException eeX) {
          System.out.println(String.format("Flight %s already exists. Skipped.", nextFlight[0]));
        }
      }
    } catch (NumberFormatException eeX) {
      throw new InvalidFileException("Price format in file incorrect.");
    } catch (ParseException eeX) {
      throw new InvalidFileException("Date format in file incorrect.");
    }
    catch (IOException ex) {
      ex.printStackTrace();
    }

  }


  /**
   * Take in a list of unsorted flight itinerary and sort it according to either total cost or
   * travel time.
   * 
   * @param unsorted list of unsorted Flight Itinerary
   * @param sortOrder sorts depending on total cost or travel time
   * @return a list of sorted Flight Itinerary
   */
  private static List<FlightItinerary> sort(List<FlightItinerary> unsorted, SortOrder sortOrder) {
    // If the search results need to be sorted by cost
    if (sortOrder == SortOrder.TOTALCOST) {
      List<FlightItinerary> sorted = new ArrayList<FlightItinerary>();
      // Go through every itinerary and sort them
      for (FlightItinerary nextItinerary : unsorted) {
        for (int i = 0; i <= sorted.size(); i++) {
          if (i == sorted.size()) {
            sorted.add(nextItinerary);
            break;
          }
          if (nextItinerary.totalPrice() < sorted.get(i).totalPrice()) {
            sorted.add(i, nextItinerary);
            break;
          }
        }
      }
      return sorted;
      // If the search results need to be sorted by the time
    } else if (sortOrder == SortOrder.TRAVELTIME) {
      List<FlightItinerary> sorted = new ArrayList<FlightItinerary>();
      // Go through every itinerary and sort them
      for (FlightItinerary nextItinerary : unsorted) {
        for (int i = 0; i <= sorted.size(); i++) {
          if (i == sorted.size()) {
            sorted.add(nextItinerary);
            break;
          }
          if (nextItinerary.travelTime() < sorted.get(i).travelTime()) {
            sorted.add(i, nextItinerary);
            break;
          }
        }
      }
      return sorted;
    } else {
      return unsorted;
    }
  }

  /**
   * Take a given Itinerary and clone it.
   * 
   * @param original the original Itinerary
   * @return the cloned flight
   */
  private static FlightItinerary cloneItinerary(FlightItinerary original) {
    FlightItinerary clone = new FlightItinerary();
    for (Flight nextFlight : original.getFlights()) {
        clone.addFlight(nextFlight);

    }
    return clone;
  }

  /**
   * Takes in the current date and modifies it.
   * 
   * @param start the start time
   * @param delta duration of the time
   * @return the modified Date
   */
  private static Calendar addTime(Calendar start, Duration delta) {
    Calendar newTime = (Calendar) start.clone();
    delta.addTo(newTime);
    return newTime;
  }

  /**
   * Display all the valid Itineraries that the Client can book in a list sorted by cost or travel
   * time.
   * 
   * @param origin the place from which the flight will depart
   * @param destination the place where the flight will land
   * @param departureDate the date at which the flight takes off
   * @param sortOrder how the search result should be sorted
   * @return the search result containing all the valid itineraries according to the sort order
   * @throws NoSuchCityException if the city is not found
   */
  public static List<FlightItinerary> searchItineraries(City origin, City destination, Date departureDate,
      SortOrder sortOrder) throws NoSuchCityException {
    // Check that both Cities exist
    if (!(hasCity(origin) & hasCity(destination))) {
      throw new NoSuchCityException("No Flights connected to Origin and/or destination");
    }
    List<FlightItinerary> paths = new ArrayList<FlightItinerary>();
    LinkedList<FlightItinerary> queue = new LinkedList<FlightItinerary>();
    Calendar departDate = Calendar.getInstance();
    departDate.setTime(departureDate);
//    DatatypeFactory typemaker;
//    Duration maxLayover = null;
//    Duration minLayover = null;
//  try {
//      typemaker = DatatypeFactory.newInstance();
//      maxLayover = typemaker.newDuration(true, 0, 0, 0, 0, 0, maxLayover_Sec);
//      minLayover = typemaker.newDuration(true, 0, 0, 0, 0, 0, minLayover_Sec);
//  } catch (DatatypeConfigurationException e) {
//      e.printStackTrace();
//  }
    for (Flight nextFlight : origin.getDepartures()) {
      if (nextFlight.departsOn(departDate)) {
        FlightItinerary nextItinerary = new FlightItinerary();

          nextItinerary.addFlight(nextFlight);
          if (nextItinerary.getDestination() == destination) {
            paths.add(nextItinerary);
          } else {
            queue.add(nextItinerary);
          }

      }
    }
    while (queue.size() != 0) {
      FlightItinerary nextItinerary = queue.poll();
      City endCity = nextItinerary.getDestination();
      Calendar maxCutoff = Calendar.getInstance();
      Calendar minCutoff = Calendar.getInstance();
      maxCutoff.setTimeInMillis(nextItinerary.getArrival().getTimeInMillis() + maxLayover_milli);
      minCutoff.setTimeInMillis(nextItinerary.getArrival().getTimeInMillis() + minLayover_milli);
      for (Flight nextFlight : endCity.getDepartures()) {
        if (nextFlight.departsAfter(minCutoff) & nextFlight.departsBefore(maxCutoff)
            & !nextItinerary.visitsCity(nextFlight.getDestination())) {

            FlightItinerary newItinerary = cloneItinerary(nextItinerary);;
            newItinerary.addFlight(nextFlight);

            if (nextFlight.getDestination() == destination) {
              paths.add(newItinerary);
            } else {
              queue.add(newItinerary);
            }


        }
      }
    }
    return sort(paths, sortOrder);
  }

  /**
   * Helper method for searchItineraries.
   * 
   * @param origin the place from which the flight will depart
   * @param destination the place where the flight will land
   * @param departureDate the day on which the flight will depart
   * @param sortOrder how the search result should be sorted
   * @return call on searchItineraries
   * @throws NoSuchCityException if no such city exists
   */
  public static List<FlightItinerary> searchItineraries(String origin, String destination,
      Date departureDate, SortOrder sortOrder) throws NoSuchCityException {
    return searchItineraries(getCity(origin), getCity(destination), departureDate, sortOrder);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null || getClass() != obj.getClass()) {
      return false;
    }
    FlightDatabase other = (FlightDatabase) obj;
    return (flights == null ? other.flights == null : flights.equals(other.flights))
        && (cities == null ? other.cities == null : cities.equals(other.cities));
  }

  public void setMaxLayover(long newLayoverSeconds){
    this.maxLayover_milli =  newLayoverSeconds*1000;
  }

  public void setMinLayover(long newLayoverSeconds){
    this.minLayover_milli = newLayoverSeconds*1000;
  }

  /**
   * Returns all itineraries that depart from origin and arrive at destination on the given date. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. To simplify our task, if there are more than MAX_LAYOVER hours or less
   * than MIN_LAYOVER between the arrival of F1 and the departure of F2, then we do not consider
   * this sequence for a possible itinerary.
   *
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries that depart from origin and arrive at destination on the given date with
   *         valid layover. Each itinerary in the output should contain one line per flight, in the
   *         format:
   *         Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination
   *         where departure and arrival date-times are in the format yyyy-MM-dd HH:mm,
   *         followed by total price (on its own line, exactly 2 decimal places),
   *         followed by total duration (on its own line, in the format HH:mm).
   */
  /**
   * Returns all itineraries that depart from origin and arrive at destination on the given date. If
   * an itinerary contains two consecutive flights F1 and F2, then the destination of F1 should
   * match the origin of F2. To simplify our task, if there are more than MAX_LAYOVER hours or less
   * than MIN_LAYOVER between the arrival of F1 and the departure of F2, then we do not consider
   * this sequence for a possible itinerary.
   *
   * @param date1 a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries that depart from origin and arrive at destination on the given date with
   *         valid layover. Each itinerary in the output should contain one line per flight, in the
   *         format: Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination where
   *         departure and arrival date-times are in the format yyyy-MM-dd HH:mm, followed by total
   *         price (on its own line, exactly 2 decimal places), followed by total duration (on its
   *         own line, in the format HH:mm).
   */
  public static List<String> getItineraries(String date1, String origin, String destination) {
    List<String> returnString = new ArrayList<String>();
    // parse date into Date format
    Date formattedDate = null;
    try {
      formattedDate = Constants.date.parse(date1);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }

    try {
      // get the itineraries
      List<FlightItinerary> itineraries = database.FlightDatabase.searchItineraries(origin,
              destination, formattedDate, SortOrder.UNSORTED);
      for (FlightItinerary itin : itineraries) {
        // get itin in string
        returnString.add(itin.toString());
        
      }
      return returnString;

    } catch (NoSuchCityException e) {
      e.printStackTrace();
    }
    return returnString;


  }



  /**
   * Returns the same itineraries as getItineraries produces, but sorted according to total
   * itinerary cost, in non-decreasing order.
   *
   * @param date1 a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total itinerary cost) in the same format
   *         as in getItineraries.
   */
  public static List<String> getItinerariesSortedByCost(String date1, String origin,
                                                        String destination) {
    List<String> returnString = new ArrayList<String>();
    // parse date into Date format
    Date formattedDate = null;
    try {
      formattedDate = Constants.date.parse(date1);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }

    try {
      // get the itineraries
      List<FlightItinerary> itineraries = database.FlightDatabase.searchItineraries(origin,
              destination, formattedDate, SortOrder.TOTALCOST);
      for (FlightItinerary itin : itineraries) {
        // get itin in string
        returnString.add(itin.toString());
        
      }
      return returnString;

    } catch (NoSuchCityException e) {
      e.printStackTrace();
    }
    return returnString;


  }


  /**
   * Returns the same itineraries as getItineraries produces, but sorted according to total
   * itinerary travel time, in non-decreasing order.
   *
   * @param date1 a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total travel time) in the same format as
   *         in getItineraries.
   */
  public static List<String> getItinerariesSortedByTime(String date1, String origin,
                                                        String destination) {
    List<String> returnString = new ArrayList<String>();
    // parse date into Date format
    Date formattedDate = null;
    try {
      formattedDate = Constants.date.parse(date1);
    } catch (ParseException ex) {
      ex.printStackTrace();
    }

    try {
      // get the itineraries
      List<FlightItinerary> itineraries = database.FlightDatabase.searchItineraries(origin,
              destination, formattedDate, SortOrder.TRAVELTIME);
      for (FlightItinerary itin : itineraries) {
        // get itin in string
        returnString.add(itin.toString());
        
      }
      return returnString;

    } catch (NoSuchCityException e) {
      e.printStackTrace();
    }
    return returnString;


  }


  public List<String> toCsvFormat(){
    List<String> retList = new ArrayList<>();
    for (Flight nextFlight : flights.values()){
      String nextFlightString = String.format("%s;", nextFlight.getFlightNumber());
      Calendar cal = nextFlight.getDeparture();
      nextFlightString += String.format("%04d-%02d-%02d %02d:%02d;", cal.get(Calendar.YEAR),
              cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH),
              cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
      cal = nextFlight.getArrival();
      nextFlightString += String.format("%04d-%02d-%02d %02d:%02d;", cal.get(Calendar.YEAR),
              cal.get(Calendar.MONTH)+1, cal.get(Calendar.DAY_OF_MONTH),
              cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE));
      nextFlightString += String.format("%s;%s;%s;", nextFlight.getAirline(),
              nextFlight.getOrigin().getName(), nextFlight.getDestination().getName());
      nextFlightString += String.format("%.2f;%d;%d", nextFlight.getprice(),
              nextFlight.getNumSeats(), nextFlight.getSeatsTaken());
      retList.add(nextFlightString);
    }
      return retList;
  }

  public void fromCsvFormat(List<Object> flights){
    for (Object next: flights){
      String nextLine = (String) next;
      String[] nextFlight = nextLine.split(";");
      String flightNum = nextFlight[0];
      if (!hasFlight(flightNum)) {
        Calendar depart = Calendar.getInstance();
        depart.set(Integer.parseInt(nextFlight[1]), Integer.parseInt(nextFlight[2]),
                Integer.parseInt(nextFlight[3]), Integer.parseInt(nextFlight[4]),
                Integer.parseInt(nextFlight[5]));
        Calendar arrive = Calendar.getInstance();
        arrive.set(Integer.parseInt(nextFlight[6]), Integer.parseInt(nextFlight[7]),
                Integer.parseInt(nextFlight[8]), Integer.parseInt(nextFlight[9]),
                Integer.parseInt(nextFlight[10]));
        String airline = nextFlight[11];
        City origin = new City(nextFlight[12]);
        City destination = new City(nextFlight[13]);
        double price = Double.parseDouble(nextFlight[14]);
        Integer numSeats = Integer.parseInt(nextFlight[15]);
        Integer seatsTaken = Integer.parseInt(nextFlight[16]);
        Flight newFlight = new Flight(flightNum, depart, arrive, origin, destination, airline,
                price, numSeats);
        newFlight.setSeatsTaken(seatsTaken);
        try {
          addFlight(newFlight);
        } catch (PreexistingFlightException e) {
          // Won't happen
        }
      }
    }
  }
}
