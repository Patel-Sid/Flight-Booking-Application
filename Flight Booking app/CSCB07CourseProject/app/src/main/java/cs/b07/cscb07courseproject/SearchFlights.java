package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import database.Constants;
import database.SortOrder;
import flight.Flight;
import user.User;
import validate.vaidateInput.*;

public class SearchFlights extends AppCompatActivity {
    private User client;
    private String sortOrder = "unsorted";
    private Intent intent;
    private boolean viewAsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_flights);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // get intent
        intent = getIntent();
        User client = (User) intent.getSerializableExtra("User");
        viewAsAdmin = (boolean) intent.getSerializableExtra("viewAsAdmin");
        this.client = client;


    }

    public List<Flight> searchFlights(View view) {
        // retrieve text from edittext
        //EditText expiryDateText = (EditText)findViewById(R.id.expiry_date_field);
        //String firstName = firstNameText.getText().toString();
        EditText departureDateText = (EditText)findViewById(R.id.departure_date_field);
        EditText originText = (EditText)findViewById(R.id.origin_field);
        EditText destinationText = (EditText)findViewById(R.id.destination_field);
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
                ArrayList<String> flights = client.getFlightsFormatted(departure, origin,destination, sortOrder);
                for (String s: flights) {
                    System.out.println(s);
                }
                 // place in intent
                 Intent intent = new Intent(this, ListViewPopulating.class);
                 intent.putStringArrayListExtra("Searches", flights);
                 intent.putExtra("viewAsAdmin", viewAsAdmin);
                 intent.putExtra("User", client);
                 // send to DisplaySearchFlights
                // myData = intent.getStringArrayListExtra("Searches");
                Toast.makeText(getApplicationContext(), flights.toString(), Toast.LENGTH_LONG).show();

                startActivity(intent);


            } catch (ParseException ex) {
                ex.printStackTrace();
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



        return null;
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        //  UNSORTED, TRAVELTIME, TOTALCOST;

        switch(view.getId()) {
            case R.id.unsorted:
                if (checked)
                    sortOrder = "UNSORTED";
                    break;
            case R.id.sort_cost:
                if (checked)
                    sortOrder = "TOTALCOST";
                    break;
            case R.id.sort_travel:
                if (checked)
                    sortOrder = "TRAVEL";
                    break;
        }
    }

}
