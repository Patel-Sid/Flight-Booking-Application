package cs.b07.cscb07courseproject;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.app.Activity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Calendar;

import android.content.res.AssetManager;
import android.content.Context;

import city.City;
import database.Constants;
import database.FlightDatabase;
import database.InvalidFileException;
import database.NoSuchCityException;
import database.NoSuchClientException;
import database.FileIO;
import database.ClientDatabase;
import database.PreexistingFlightException;
import database.SortOrder;
import flight.Flight;
import user.*;

public class LoginScreen extends AppCompatActivity {
    public static File dataDir;
    public static File clientDatabase;
    public static File flightDatabase;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        intent = getIntent();

        // Files for saving data
        dataDir = this.getApplicationContext().getDir("savedData", MODE_PRIVATE);
        clientDatabase = new File(dataDir, "Clients.dat");
        flightDatabase = new File(dataDir, "Flights.dat");

        // Read data
        Constants.flightFileManager.readFlightDatabase(flightDatabase);
        Constants.fileManager.readClientDatabase(clientDatabase);
        Constants.fileManager.saveClientDatabase(clientDatabase);
    }


    public void goRegister(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        EditText emailText = (EditText)findViewById(R.id.email_field);
        EditText passwordText = (EditText)findViewById(R.id.password_field);
        intent.putExtra("emailKey", emailText.getText().toString());
        intent.putExtra("passwordKey", passwordText.getText().toString());
        startActivity(intent);
    }

//    public void goRegister(View view){
//        FlightDatabase test = new FlightDatabase();
//        City city1 = new City("city1");
//        City city2 = new City("city2");
//        City city3 = new City("city3");
//        City city4 = new City("city4");
//        Calendar time11 = Calendar.getInstance();
//        time11.set(2016, 01,01,10,30);
//        Calendar time12 = Calendar.getInstance();
//        time12.set(2016, 01,01,10,45);
//        Calendar time21 = Calendar.getInstance();
//        time11.set(2016, 01,01,11,45);
//        Calendar time22 = Calendar.getInstance();
//        time11.set(2016, 01,01,11,50);
//        Calendar time31 = Calendar.getInstance();
//        time11.set(2016, 01,01,11,51);
//        Calendar time32 = Calendar.getInstance();
//        time11.set(2016, 01,01,11,52);
//        Flight flight1 = new Flight("Flight1", time11, time12, city1, city2, "delta",30.00, 1);
//        Flight flight2 = new Flight("Flight2", time21, time22, city2, city3, "delta",30.00, 1);
//        Flight flight3 = new Flight("Flight3", time31, time32, city3, city4, "delta",30.00, 1);
//        try {
//            test.addFlight(flight1);
//            test.addFlight(flight2);
//            test.addFlight(flight3);
//            Calendar cal = Calendar.getInstance();
//            cal.set(2016, 01, 01);
//            test.searchItineraries("city1", "city4", cal.getTime(), SortOrder.TOTALCOST);
//            Toast.makeText(this, "ok", Toast.LENGTH_SHORT).show();
//        } catch (PreexistingFlightException e) {
//            e.printStackTrace();
//        } catch (NoSuchCityException e){
//            e.printStackTrace();
//        }
//    }

    public void login(View view) {
        EditText emailText = (EditText)findViewById(R.id.email_field);
        String email = emailText.getText().toString();
        EditText passwordText = (EditText)findViewById(R.id.password_field);
        String password = passwordText.getText().toString();

        // check if user in database
        if (Constants.clientDatabase.hasClient(email)) {
            // this workks
            // check if password is correct
            // get client
            try {
                User client = Constants.clientDatabase.searchClientEmail(email);
                // check if client exist, but no password
                if (client.getPassword().length() == 0) {
                    // prompt user to make a password
                    Intent intent = new Intent(this, UpdatePassword.class);
                    // pass in the user
                    intent.putExtra("User", client);
                    startActivity(intent);
                }
                // if same password
                else if (client.getPassword().equals(password)){
                    Context context = getApplicationContext();
                    CharSequence text = "Successfully logged in!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                    if (client.isAdmin()){
                        Intent intent = new Intent(this, AdminActions.class);
                        intent.putExtra("Admin", client);
                        startActivity(intent);
                    } else {
                        // switch to UUserActions
                        Intent intent = new Intent(this, UserActions.class);
                        intent.putExtra("User", client);
                        intent.putExtra("viewAsAdmin", false);
                        startActivity(intent);
                    }
                } else {
                    Context context = getApplicationContext();
                    CharSequence text = "Incorrect email or password";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();
                }
            } catch (NoSuchClientException e) {
                Context context = getApplicationContext();
                CharSequence text = "NoSuchClient";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();
            }
            } else {
            //System.out.println("FUC");
            Context context = getApplicationContext();
            CharSequence text = "Incorrect email or password";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


}
