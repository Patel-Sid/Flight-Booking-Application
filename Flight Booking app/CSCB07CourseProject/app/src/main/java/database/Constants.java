package database;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import static android.content.Context.MODE_PRIVATE;

public class Constants {
  public static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
  public static DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm");
  public static FlightDatabase flightDatabase = new FlightDatabase();
  public static ClientDatabase clientDatabase = new ClientDatabase();
  public static FileIO fileManager = new FileIO();
  public static FileIO flightFileManager = new FileIO();
  public static int maxLayover_Mins = 0;
  public static int maxLayover_Hours = 6;
  public static int minLayover_Mins = 30;
  public static int minLayover_Hours = 0;
  public static String displayFormat = "Number;DepartureDateTime;ArrivalDateTime;Airline;Origin;Destination;Price;Travel Time";
  public static String displayFormatItin = "All Flights in Itin\nTotal Cost\nTravel Time in HH:mm";
  //public static final Duration MIN_LAYOVER = Duration.ofMinutes(30);
  // public static final Duration MAX_LAYOVER = Duration.ofHours(6);
}
