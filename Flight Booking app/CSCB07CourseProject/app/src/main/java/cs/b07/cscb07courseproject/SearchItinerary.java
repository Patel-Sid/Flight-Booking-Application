package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Constants;
import database.NoSuchCityException;
import database.SortOrder;
import flight.Flight;
import flight.FlightItinerary;
import user.User;
import java.util.Date;

public class SearchItinerary extends AppCompatActivity {
    private User client;
    private SortOrder order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_itinerary);
        // get intent
        Intent intent = getIntent();
        User client = (User) intent.getSerializableExtra("User");
        this.client = client;
    }
    public void searchItineraries(View view) {
        // retrieve text from edittext
        //EditText expiryDateText = (EditText)findViewById(R.id.expiry_date_field);
        //String firstName = firstNameText.getText().toString();
        EditText departureDateText = (EditText)findViewById(R.id.departure_date_field_itin);
        EditText originText = (EditText)findViewById(R.id.origin_field_itin);
        EditText destinationText = (EditText)findViewById(R.id.destination_field_itin);
        // convert to string
        String departure = departureDateText.getText().toString();
        String origin = originText.getText().toString();
        String destination = destinationText.getText().toString();
        // CHECK FOR VALID INPUT
        // CHECK FOR VALID INPUT
        // CHECK FOR VALID INPUT
        if (validate.vaidateInput.validateDate(departure) && Constants.flightDatabase.hasCity(origin) && Constants.flightDatabase.hasCity(destination)) {
            // call our function User.getFlights
            try {
                // get list of itineraries
                Intent intent = new Intent(this, ItinerariesPopulate.class);
                Date depDate = Constants.date.parse(departure);
                List<String> itinerariesFormatted = new ArrayList<String>();
                if (order == SortOrder.TRAVELTIME) {
                    itinerariesFormatted = database.FlightDatabase.getItinerariesSortedByTime(departure, origin, destination);
                } else if (order == SortOrder.TOTALCOST) {
                    itinerariesFormatted = database.FlightDatabase.getItinerariesSortedByCost(departure,origin,destination);
                }else if (order == SortOrder.UNSORTED) {
                    itinerariesFormatted = database.FlightDatabase.getItineraries(departure,origin,destination);
                } else {
                    itinerariesFormatted = database.FlightDatabase.getItineraries(departure,origin,destination);
                }
                // send to a populater
                // place in intent
                // pass in date
                // String origin, String destination, Date departureDate, SortOrder sortOrder)

                intent.putStringArrayListExtra("Itineraries", (ArrayList<String>)itinerariesFormatted);
                // create list of departure,origin,destination
                ArrayList<String> searchParams = new ArrayList<String>();
                searchParams.add(departure);
                searchParams.add(origin);
                searchParams.add(destination);
                intent.putStringArrayListExtra("searchParams", searchParams);
                intent.putExtra("User", client);
                // send to ItinerariesPopulate
                startActivity(intent);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            String invalidInput = "";
            if (!(validate.vaidateInput.validateDate(departure))) {
                invalidInput += "Invalid Date\n";

            }
            if (!(Constants.flightDatabase.hasCity(origin))) {
                invalidInput += "Origin does not exist\n";
            }
            if (!(Constants.flightDatabase.hasCity(destination))) {
                invalidInput += "Destination does not exist";
            }

            Toast.makeText(this, invalidInput, Toast.LENGTH_SHORT).show();

        }



    }
    public void onRadioButtonClickedItin(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        //  UNSORTED, TRAVELTIME, TOTALCOST;

        switch(view.getId()) {
            case R.id.unsorted_itin:
                if (checked)
                    order = SortOrder.UNSORTED;
                break;
            case R.id.sort_cost_itin:
                if (checked)
                    order = SortOrder.TOTALCOST;
                break;
            case R.id.sort_travel_itin:
                if (checked)
                    order = SortOrder.TRAVELTIME;
                break;
        }
    }
}



