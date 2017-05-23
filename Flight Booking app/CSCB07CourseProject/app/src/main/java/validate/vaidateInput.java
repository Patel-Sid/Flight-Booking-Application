package validate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class vaidateInput {
  
  public static final long MIN_LAYOVER = 30 * 60; // 30 min in seconds
  public static final long MAX_LAYOVER = 6 * 60 * 60; // 6 hours in seconds

  private static final Locale locale = Locale.getDefault();
  private static final DateFormat date = new SimpleDateFormat("yyyy-MM-dd", locale);
  private static final DateFormat dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm", locale);
  private static final DateFormat time = new SimpleDateFormat("HH:mm", locale);
  
  public static boolean validateDate (String date) {
    // check length
    if (date.length() == 10) {
      // separate into array
      String[] split = date.split("-");
      // confirm format
      try {
        int year = Integer.parseInt(split[0]);
        int month = Integer.parseInt(split[1]);
        int day = Integer.parseInt(split[2]);
        return true;
      } catch (NumberFormatException ex) {
        return false;
      }
      
    } else {
      return false;
    }

    
     
    
  }
  
  public static boolean validateDateTime (String date) {
    // check length
    if (date.length() == 16) {
      //separate into array
      String[] splitspace = date.split(" ");
      // validate the yyyy-mm-dd
      if (validateDate(splitspace[0])) {
        // validate second part
        // split it
        String[] splitcolon = splitspace[1].split(":");
        try {
          int hour = Integer.parseInt(splitcolon[0]);
          int min = Integer.parseInt(splitcolon[1]);
          return true;
        } catch (NumberFormatException ex) {
          return false;
        }
      } else {
        return false;
      }
      
    } else {
      return false;
    }

  }

}
