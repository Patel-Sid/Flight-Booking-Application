package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import city.City;
import database.Constants;
import database.InvalidInputException;
import database.NoSuchCityException;
import database.NoSuchFlightException;
import database.PreexistingCityException;
import database.PreexistingFlightException;
import flight.Flight;
import flight.FlightFields;
import user.ClientField;
import user.User;

public class EditFlightInfo extends AppCompatActivity {
    private TableLayout table, flightModify;
    private Spinner selectSpinner;
    ArrayAdapter<CharSequence> adapter;
    DatePicker flightDate;
    TimePicker flightTime;
    EditText flightNumEntry, valueEntry;
    Button submitButton;
    private Flight flight;
    private FlightFields editField;
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_flight_info);
        Intent intent = getIntent();

        // Set up activity
        setUp();

        // Listen for user action
        userAction();


    }

    public void displaySearchFlights(View view){
        // Retrieve flightNum from TextView
        flightNumEntry = (EditText) findViewById(R.id.flight_num_entry);
        String flightNum = flightNumEntry.getText().toString();
        // Search for flightNum in the flightDatabase
        try {
            flight = Constants.flightDatabase.getFlight(flightNum);
            populateFlightTable();
            table.setVisibility(TableLayout.VISIBLE);
            selectSpinner.setVisibility(Spinner.VISIBLE);
            flightModify.setVisibility(TableLayout.VISIBLE);

        } catch (NoSuchFlightException e){
            Toast.makeText(this, "Flight does not exist!", Toast.LENGTH_SHORT).show();
            table.setVisibility(TableLayout.INVISIBLE);
            selectSpinner.setVisibility(Spinner.INVISIBLE);
            flightModify.setVisibility(TableLayout.INVISIBLE);
        }
    }

    private void setUp(){
        table = (TableLayout) findViewById(R.id.flightInfoTable);
        flightModify = (TableLayout) findViewById(R.id.flightModify);
        table.setVisibility(TableLayout.INVISIBLE);
        flightModify.setVisibility(TableLayout.INVISIBLE);

        selectSpinner = (Spinner) findViewById(R.id.field_select);
        adapter = ArrayAdapter.createFromResource(this, R.array.flightField_select,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpinner.setAdapter(adapter);
        valueEntry = (EditText) findViewById(R.id.new_value);
        flightDate = (DatePicker) findViewById(R.id.date_picker);
        flightTime = (TimePicker) findViewById(R.id.time_picker);
        flightDate.setMinDate(System.currentTimeMillis());
        submitButton = (Button) findViewById(R.id.submit_button);
        valueEntry.setVisibility(EditText.GONE);
        flightDate.setVisibility(DatePicker.GONE);
        flightTime.setVisibility(TimePicker.GONE);
        submitButton.setEnabled(false);
    }

    private void userAction(){
        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                if (pos == 0){
                    valueEntry.setVisibility(EditText.GONE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(false);
                }
                else if(pos == 1){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Airline");
                    editField = FlightFields.AIRLINE;
                }
                else if(pos == 2){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new City of Origin");
                    editField = FlightFields.ORIGIN;
                }
                else if(pos == 3){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Destination City");
                    editField = FlightFields.DESTINATION;
                }
                else if(pos == 4){
                    valueEntry.setVisibility(EditText.GONE);
                    flightDate.setVisibility(DatePicker.VISIBLE);
                    flightTime.setVisibility(TimePicker.VISIBLE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Departure Time");
                    editField = FlightFields.DEPARTURE;
                }
                else if(pos == 5){
                    valueEntry.setVisibility(EditText.GONE);
                    flightDate.setVisibility(DatePicker.VISIBLE);
                    flightTime.setVisibility(TimePicker.VISIBLE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Arrival Time");
                    editField = FlightFields.ARRIVAL;
                }
                else if(pos == 6){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Cost");
                    editField = FlightFields.TOTALCOST;
                }
                else if(pos == 7){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    flightDate.setVisibility(DatePicker.GONE);
                    flightTime.setVisibility(TimePicker.GONE);
                    submitButton.setEnabled(true);
                    valueEntry.setHint("Enter new Seat Capacity");
                    editField = FlightFields.NUMSEATS;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void populateFlightTable(){
        TextView flightNum = (TextView) findViewById(R.id.flightNum);
        TextView airline = (TextView) findViewById(R.id.airline);
        TextView originCity = (TextView) findViewById(R.id.originCity);
        TextView destinationCity = (TextView) findViewById(R.id.destinationCity);
        TextView departureTime = (TextView) findViewById(R.id.departureTime);
        TextView arrivalTime = (TextView) findViewById(R.id.arrivalTime);
        TextView flightCost = (TextView) findViewById(R.id.flightCost);
        TextView seatAvailability = (TextView) findViewById(R.id.seatAvailability);
        String flightNumString = flight.getFlightNumber();
        String airlineString = flight.getAirline();
        String originString = flight.getOrigin().getName();
        String destinationString = flight.getDestination().getName();
        String departureString = dateTimeFormat.format(flight.getDeparture().getTime());
        String arrivalString = dateTimeFormat.format(flight.getArrival().getTime());
        String costString = Double.toString(flight.getprice());
        String seatString = String.format("%d/%d seats taken.",
                flight.getSeatsTaken(), flight.getNumSeats());
        flightNum.setText(flightNumString);
        airline.setText(airlineString);
        originCity.setText(originString);
        destinationCity.setText(destinationString);
        departureTime.setText(departureString);
        arrivalTime.setText(arrivalString);
        flightCost.setText(costString);
        seatAvailability.setText(seatString);
    }

    private int editAirline(){
        String airline = valueEntry.getText().toString();
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.AIRLINE, airline);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editOrigin(){
        String origin = valueEntry.getText().toString();
        City originCity;
        try{
            originCity = Constants.flightDatabase.getCity(origin);
        } catch (NoSuchCityException e) {
            originCity = new City(origin);
            try {
                Constants.flightDatabase.addCity(originCity);
            } catch (PreexistingCityException ex) {
                // Won't happen
                return -1;
            }
        }
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.ORIGIN, originCity);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editDestination(){
        String dest = valueEntry.getText().toString();
        City destCity;
        try{
            destCity = Constants.flightDatabase.getCity(dest);
        } catch (NoSuchCityException e) {
            destCity = new City(dest);
            try {
                Constants.flightDatabase.addCity(destCity);
            } catch (PreexistingCityException ex) {
                // Won't happen
                return -1;
            }
        }
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.DESTINATION, destCity);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editDeparture(){
        Calendar depart = Calendar.getInstance();
        if (Build.VERSION.SDK_INT < 23){
            depart.set(flightDate.getYear(), flightDate.getMonth(), flightDate.getDayOfMonth(),
                    flightTime.getCurrentHour(), flightTime.getCurrentMinute());
        }else {
            depart.set(flightDate.getYear(), flightDate.getMonth(), flightDate.getDayOfMonth(),
                    flightTime.getHour(), flightTime.getMinute());
        }
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(),
                    FlightFields.DEPARTURE, depart);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editArrival(){
        Calendar arrival = Calendar.getInstance();
        if (Build.VERSION.SDK_INT < 23){
            arrival.set(flightDate.getYear(), flightDate.getMonth(), flightDate.getDayOfMonth(),
                    flightTime.getCurrentHour(), flightTime.getCurrentMinute());
        }else {
            arrival.set(flightDate.getYear(), flightDate.getMonth(), flightDate.getDayOfMonth(),
                    flightTime.getHour(), flightTime.getMinute());
        }
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.ARRIVAL, arrival);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editCost(){
        Double newCost = Double.parseDouble(valueEntry.getText().toString());
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.TOTALCOST , newCost);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    private int editSeats(){
        int numSeats = Integer.parseInt(valueEntry.getText().toString());
        try {
            Constants.flightDatabase.editFlight(flight.getFlightNumber(), FlightFields.NUMSEATS, numSeats);
            return 0;
        } catch (Exception e) {
            return -1;
        }
    }

    public void editInfo(View view){
        int result = -1;
        if (editField == FlightFields.AIRLINE){
            result = editAirline();
        } else if (editField == FlightFields.ORIGIN) {
            result = editOrigin();
        } else if (editField == FlightFields.DESTINATION) {
            result = editDestination();
        } else if (editField == FlightFields.DEPARTURE) {
            result = editDeparture();
        } else if (editField == FlightFields.ARRIVAL) {
            result = editArrival();
        } else if (editField == FlightFields.TOTALCOST) {
            result = editCost();
        } else if (editField == FlightFields.NUMSEATS) {
            result = editSeats();
        }
        // Save changes and Toast confirmation if successful
        if (result==0) {
            Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
            populateFlightTable();
            Toast.makeText(this, "Change successful", Toast.LENGTH_SHORT).show();
        }
    }
}
