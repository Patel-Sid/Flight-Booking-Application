
package flight;

/**
 * Enumeration for flightFields. To easily modify various fields within one method.
 * 
 * @author jason
 *
 */
public enum FlightFields {
  FLIGHTNUM, DEPARTURE, ARRIVAL, ORIGIN, DESTINATION, AIRLINE, TOTALCOST, NUMSEATS;

  /**
   * Returns the same itineraries as getItineraries produces, but sorted according to total
   * itinerary travel time, in non-decreasing order.
   * 
   * @param date a departure date (in the format yyyy-MM-dd)
   * @param origin a flight original
   * @param destination a flight destination
   * @return itineraries (sorted in non-decreasing order of total travel time) in the same format as
   *         in getItineraries.
   */
  public static String getItinerariesSortedByTime(String date, String origin, String destination) {
    // TODO: complete this method body
    return null;
  }
}
