package cs.b07.cscb07courseproject;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import city.City;
import database.Constants;
import database.NoSuchCityException;
import database.PreexistingFlightException;
import flight.Flight;
import user.User;

public class CreateFlight extends AppCompatActivity {
    User admin;
    EditText flightNumber, origin, destination, airlineIn, costIn, seats;
    DatePicker departDate, arriveDate;
    TimePicker departTime, arriveTime;
    Calendar departure, arrival;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_flight);
        Intent intent = getIntent();
        // get client
        User admin = (User) intent.getSerializableExtra("Admin");
        this.admin = admin;

        setUp();

    }

    public void setUp(){
        flightNumber = (EditText) findViewById(R.id.flightNumber);
        origin = (EditText) findViewById(R.id.originCity);
        destination = (EditText) findViewById(R.id.destinationCity);
        airlineIn = (EditText) findViewById(R.id.airline);
        costIn = (EditText) findViewById(R.id.priceEntry);
        seats = (EditText) findViewById(R.id.numSeats);
        departDate = (DatePicker) findViewById(R.id.departureDate);
        departTime = (TimePicker) findViewById(R.id.departureTime);
        arriveDate = (DatePicker) findViewById(R.id.arrivalDate);
        arriveTime = (TimePicker) findViewById(R.id.arrivalTime);
        departDate.setMinDate(System.currentTimeMillis());
        arriveDate.setMinDate(System.currentTimeMillis());
        departure = Calendar.getInstance();
        arrival = Calendar.getInstance();
    }

    public void createFlight(View view){
        String flightNum = flightNumber.getText().toString();
        if (Build.VERSION.SDK_INT < 23){
            departure.set(departDate.getYear(), departDate.getMonth(), departDate.getDayOfMonth(),
                    departTime.getCurrentHour(), departTime.getCurrentMinute());
        }else {
            departure.set(departDate.getYear(), departDate.getMonth(), departDate.getDayOfMonth(),
                    departTime.getHour(), departTime.getMinute());
        }
        City originCity, destinationCity;
        try{
            originCity = Constants.flightDatabase.getCity(origin.getText().toString());
        }
        catch (NoSuchCityException e){
            originCity = new City(origin.getText().toString());
        }
        try{
            destinationCity = Constants.flightDatabase.getCity(destination.getText().toString());
        }
        catch (NoSuchCityException e){
            destinationCity = new City(destination.getText().toString());
        }
        String airline = airlineIn.getText().toString();
        Double cost = Double.parseDouble(costIn.getText().toString());
        int numSeats = Integer.parseInt(seats.getText().toString());

        Flight newFlight = new Flight(flightNum, departure, arrival, originCity, destinationCity,
                airline, cost, numSeats);
        try {
            Constants.flightDatabase.addFlight(newFlight);
            Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
            Toast.makeText(this, "Flight Created!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, AdminActions.class);
            intent.putExtra("Admin", admin);
            startActivity(intent);
        } catch (PreexistingFlightException e) {
            Toast.makeText(this, "Flight number in use!", Toast.LENGTH_SHORT).show();
        }

    }


}
