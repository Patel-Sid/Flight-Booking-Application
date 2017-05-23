
package flight;

import city.City;
import database.InvalidInputException;
import flight.FlightFields;
import user.User;

import java.io.Serializable;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.text.SimpleDateFormat;

/**
 * A Flight class to represent a flight.
 *
 * @author jason
 *
 */
public class Flight implements Serializable {
    /**
     * ..
     */
    private static final long serialVersionUID = -6210255868202372115L;
    private String flightNumber;
    private Calendar departure;
    private Calendar arrival;
    private City origin;
    private City destination;
    private String airline;
    private double price;
    private int travelTime;
    private Set<User> fliers;
    private int seatsTaken;
    private int numSeats;


    /**
     * Constructor for Flight. Creates a new flight.
     *
     * @param flightNumber the flight number associated with this flight
     * @param departure the time in which this flight is set to lift off
     * @param arrival the time in which the flight arrives to the destination
     * @param origin the <code>City</code> where the flight initially started
     * @param destination the <code>City</code> where the flight is heading to
     * @param airline the airline company that is responsible for this flight
     */
    public Flight(String flightNumber, Calendar departure, Calendar arrival, City origin, City destination,
                  String airline, double price, int seats) {
        this.flightNumber = flightNumber;
        this.departure = departure;
        this.arrival = arrival;
        this.origin = origin;
        this.destination = destination;
        this.airline = airline;
        this.price = price;
        this.numSeats = seats;
        this.seatsTaken = 0;
        // init fliers
        fliers = new HashSet<User>();
        // get travel time
        this.travelTime = travelTimeMinutes(departure, arrival);

    }

    /**
     * Returns the total travel time of this flight.
     *
     * @param start is when to start counting
     * @param stop is when to stop counting
     * @return the difference in time between start and stop
     */
    private int travelTimeMinutes(Calendar start, Calendar stop) {
        long diff = stop.getTimeInMillis() - start.getTimeInMillis();
        long diffMinutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        return (int) diffMinutes;

    }

    /**
     * Edits the given flightField with the given object.
     *
     * @param flightField the property to modify
     * @param obj the Object that replaces the previous field
     * @throws InvalidInputException If an invalid input is given
     */
    public void editFlight(FlightFields flightField, Object obj) throws InvalidInputException {
        // ENUM: flightNum, departure, arrival, origin, destination, airline
        // depending on flightField given, changes the given flightField with
        // give obj
        switch (flightField) {
            case FLIGHTNUM:
                if (obj instanceof String) {
                    this.flightNumber = (String) obj;
                    break;
                } else {
                    throw new InvalidInputException("Flight number is not a string");
                }
            case DEPARTURE:
                if (obj instanceof Calendar) {
                    this.departure = (Calendar) obj;
                    break;
                } else {
                    throw new InvalidInputException("Departure must be a String");
                }
            case ARRIVAL:
                if (obj instanceof Calendar) {
                    this.arrival = (Calendar) obj;
                    break;
                } else {
                    throw new InvalidInputException("Arrival must be a String");
                }
            case ORIGIN:
                if (obj instanceof City) {
                    this.origin = (City) obj;
                    break;
                } else {
                    throw new InvalidInputException("Origin must be a city");
                }
            case DESTINATION:
                if (obj instanceof City) {
                    this.destination = (City) obj;
                    break;
                } else {
                    throw new InvalidInputException("Destination must be a city");
                }
            case AIRLINE:
                if (obj instanceof String) {
                    this.airline = (String) obj;
                    break;
                } else {
                    throw new InvalidInputException("Airline must be a string");
                }
            default:
                break;
        }
    }

    /**
     * Returns the <code>User</code> on this plane.
     *
     * @return a set of <code>User</code> that are on this flight
     */
    public Set<User> getFliers() {
        return this.fliers;
    }


    /**
     * Adds the given person onto this flight.
     *
     * @param person is the User that is to be added to this flight.
     * @throws FlightFullException if there is no more room on this flight.
     */
    public void addFlyer(User person) throws FlightFullException{
        // adds the Flyer to the Flight
        // check if flier is not in this fight
        if (!(fliers.contains(person))) {
            // check if his bookedItinerary is free
            // Set<FlightItinerary> bookedFlights;

            // check if flight is not full
            if (hasRoom()) {
                fliers.add(person);
            } else {
                throw new FlightFullException("No more room on this flight");
            }
        }

    }

    /**
     * Removes the given person from this flight.
     *
     * @param person the User that needs to be removed.
     * @throws NoSuchFlyerException is raised if no such person exists on this flight.
     */
    public void removeFlyer(User person) throws NoSuchFlyerException {
        // checks if the flier is on this flight
        if (fliers.contains(person)) {
            // remove the person
            fliers.remove(person);
        } else {
            // cannot find person, throw an exception
            throw new NoSuchFlyerException("The User given is not on this flight");
        }
    }



    /**
     * Return the flight number.
     *
     * @return the flight number.
     */
    public String getFlightNumber() {
        return flightNumber;
    }

    /**
     * Set the flight number with the given flight number.
     *
     * @param flightNumber the new flight number
     */
    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    /**
     * Get the departure date.
     *
     * @return the departure date.
     */
    public Calendar getDeparture() {
        return departure;
    }

    /**
     * Set the departure date with the given date.
     *
     * @param departure the new departure date
     */
    public void setDeparture(Calendar departure) {
        this.departure = departure;
    }

    /**
     * Get the arrival date.
     *
     * @return the arrival date
     */
    public Calendar getArrival() {
        return arrival;
    }

    /**
     * Set the arrival date with the given date.
     *
     * @param arrival the new arrival date.
     */
    public void setArrival(Calendar arrival) {
        this.arrival = arrival;
    }

    /**
     * Get the origin city, where the flight is originally at.
     *
     * @return the <code>City</code> the flight takes off at
     */
    public City getOrigin() {
        return origin;
    }

    /**
     * Sets the origin <code>City</code> with the given <code>City</code>.
     *
     * @param origin the <code>City</code> the flight takes off from
     */
    public void setOrigin(City origin) {
        this.origin = origin;
    }

    /**
     * Gets the <code>City</code> the <code>Flight</code> is heading towards.
     *
     * @return the <code>City</code> the Flight lands in
     */
    public City getDestination() {
        return destination;
    }

    /**
     * Set the destination of the <code>Flight</code> to the given <code>City</code>.
     *
     * @param destination the new destination, where the flight lands
     */
    public void setDestination(City destination) {
        this.destination = destination;
    }

    /**
     * Gets the airline company responsible for this <code>Flight</code>.
     *
     * @return a String representing the airline company
     */
    public String getAirline() {
        return airline;
    }

    /**
     * Gets the available number of seats for this <code>Flight</code>.
     * @return the available number of seats for this <code>Flight</code>.
     */
    public int getNumSeats() {
        return numSeats;
    }

    public void setNumSeats(int numSeats) {
        this.numSeats = numSeats;
    }

    /**
     * Gets the number of seats taken for this <code>Flight</code>.
     * @return the number of seats taken for this <code>Flight</code>.
     */
    public int getSeatsTaken() {return seatsTaken;}

    /**
     * Sets the number of seats taken for this <code>Flight</code>.
     * @param numSeats is the number of seats to set as taken.
     */
    public void setSeatsTaken(int numSeats){
        seatsTaken = numSeats;
    }

    /**
     * Return True if there are empty seats.
     * @return True if there are empty seats.
     */
    public boolean hasRoom() {return seatsTaken<numSeats;}

    /**
     * Set the airline company to the given string.
     *
     * @param airline the new airline company
     */
    public void setAirline(String airline) {
        this.airline = airline;
    }

    /**
     * Gets the price of this <code>Flight</code>.
     *
     * @return the price of travelling in this <code>Flight</code>.
     */
    public double getprice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean departsOn(Calendar depart) {
        boolean sameDay = ((depart.get(Calendar.YEAR) == getDeparture().get(Calendar.YEAR)) &&
                (depart.get(Calendar.DAY_OF_YEAR) == getDeparture().get(Calendar.DAY_OF_YEAR)));
        return sameDay;
    }

    /**
     * Get the duration of flying time in minutes on this <code>Flight</code>.
     *
     * @return how long, in minutes, this flight is.
     */
    public int getTravelTime() {
        return travelTime;
    }

    public boolean departsBefore(Calendar cutoff) {
        return (departure.compareTo(cutoff) <= 0);
    }

    public boolean departsAfter(Calendar cutoff) {
        return (departure.compareTo(cutoff) >= 0);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((airline == null) ? 0 : airline.hashCode());
        result = prime * result + ((arrival == null) ? 0 : arrival.hashCode());
        long temp;
        temp = Double.doubleToLongBits(price);
        result = prime * result + (int) (temp ^ (temp >>> 32));
        result = prime * result + ((departure == null) ? 0 : departure.hashCode());
        result = prime * result + ((destination == null) ? 0 : destination.hashCode());
        result = prime * result + ((fliers == null) ? 0 : fliers.hashCode());
        result = prime * result + ((flightNumber == null) ? 0 : flightNumber.hashCode());
        result = prime * result + ((origin == null) ? 0 : origin.hashCode());
        result = prime * result + travelTime;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Flight other = (Flight) obj;
        if (airline == null) {
            if (other.airline != null) {
                return false;
            }
        } else if (!airline.equals(other.airline)) {
            return false;
        }
        if (arrival == null) {
            if (other.arrival != null) {
                return false;
            }
        } else if (!arrival.equals(other.arrival)) {
            return false;
        }
        if (Double.doubleToLongBits(price) != Double.doubleToLongBits(other.price)) {
            return false;
        }
        if (departure == null) {
            if (other.departure != null) {
                return false;
            }
        } else if (!departure.equals(other.departure)) {
            return false;
        }
        if (destination == null) {
            if (other.destination != null) {
                return false;
            }
        } else if (!destination.equals(other.destination)) {
            return false;
        }
        if (fliers == null) {
            if (other.fliers != null) {
                return false;
            }
        } else if (!fliers.equals(other.fliers)) {
            return false;
        }
        if (flightNumber == null) {
            if (other.flightNumber != null) {
                return false;
            }
        } else if (!flightNumber.equals(other.flightNumber)) {
            return false;
        }
        if (origin == null) {
            if (other.origin != null) {
                return false;
            }
        } else if (!origin.equals(other.origin)) {
            return false;
        }
        if (travelTime != other.travelTime) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        String returnString = this.getFlightNumber() + ";" + this.getDeparture() + ";"
                + this.getArrival() + ";" + this.getOrigin().getName() + ";"
                + this.getDestination().getName() + ";" + this.getAirline() + ";" + this.getprice();
        returnString += "\nClients: \n";
        for (User client : fliers) {
            returnString += client.getFirstName() + " " + client.getLastName() + "\n";
        }

        return returnString;

    }

    public String CalendarToDateTime(Calendar calender) {
        /**
         SimpleDateFormat fmt = new SimpleDateFormat("dd-MMM-yyyy");
         fmt.setCalendar(calender);
         String dateFormatted = fmt.format(calender.getTime());
         return dateFormatted;
         */
        SimpleDateFormat fmt = (SimpleDateFormat) database.Constants.dateTime;
        fmt.setCalendar(calender);
        String date = fmt.format(calender.getTime());
        return date;
    }

    public String CalendarToDate(Calendar calendar) {
        String dateTime = CalendarToDateTime(calendar);
        String[] date = dateTime.split(" ");
        return date[0];
    }

    public String getDepartureDateTimeString() {
        Calendar calendar = this.getDeparture();
        return CalendarToDateTime(calendar);
    }
    public String getArrivalDateTimeString() {
        Calendar calendar = this.getArrival();
        return CalendarToDateTime(calendar);
    }

    public void addFlyer(){
        seatsTaken += 1;
    }
}
