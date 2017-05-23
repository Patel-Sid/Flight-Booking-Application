package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import database.Constants;
import database.NoSuchClientException;
import database.NoSuchFlightException;
import flight.Flight;
import flight.FlightItinerary;
import flight.InvalidFlightAddition;
import user.User;

import static android.provider.AlarmClock.EXTRA_MESSAGE;
import static cs.b07.cscb07courseproject.LoginScreen.flightDatabase;

public class ListViewPopulating extends AppCompatActivity {
    private ListView lv;
    private ArrayList<String> myData;
    private User client;
    String selected;
    Intent intent;
    private boolean viewAsAdmin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_populating);
        lv = (ListView)findViewById(R.id.listView1);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // retrieve data
        // get intent
        intent = getIntent();
        // get data
        myData = intent.getStringArrayListExtra("Searches");
        User client = (User) intent.getSerializableExtra("User");
        viewAsAdmin = (boolean) intent.getSerializableExtra("viewAsAdmin");
        this.client = client;
        Toast.makeText(getApplicationContext(), myData.toString(), Toast.LENGTH_LONG).show();

        //User client = (User) intent.getSerializableExtra("User");
        //this.client = client;
        // set button unclickable
        findViewById(R.id.book_flight).setEnabled(false);


        // add in header
        // but first check it's already not there
        if (!(myData.get(0).equals(Constants.displayFormat))) {
            myData.add(0, Constants.displayFormat);
        }




        lv.setAdapter(new ArrayAdapter<String>(this,0, myData) {

            private View row;
            private LayoutInflater inflater = getLayoutInflater();
            private TextView tv;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

                tv = (TextView) row.findViewById(android.R.id.text1);

                tv.setText(myData.get(position));
                //Toast.makeText(getApplicationContext(),tv.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"" + position,Toast.LENGTH_SHORT).show();

                return row;
            }
        });
        lv.setOnItemClickListener(new OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                // get the flight string
                selected = (String)lv.getItemAtPosition(position);
                if(position > 0) {
                    findViewById(R.id.book_flight).setEnabled(true);
                } else {
                    findViewById(R.id.book_flight).setEnabled(false);
                }
                //Toast.makeText(getApplicationContext(), selected, Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void book(View view) {
        // get the flight
        String[] split = selected.split(";");
        // create flight itinerary
        FlightItinerary newItin = new FlightItinerary();
        try {


            // get clients itin
            newItin.addFlight(Constants.flightDatabase.getFlight(split[0]));
            Constants.clientDatabase.searchClientEmail(client.getEmail()).bookFlight(newItin);
            //client.bookFlight(newItin);
            Toast.makeText(getApplicationContext(),client.getbookedFlights().toString() , Toast.LENGTH_SHORT).show();

            Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
            Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
            // update client

            Intent intent = new Intent(this, UserActions.class);
            intent.putExtra("User", Constants.clientDatabase.searchClientEmail(client.getEmail()));
            intent.putExtra("viewAsAdmin", viewAsAdmin);
            startActivity(intent);

            Toast.makeText(getApplicationContext(), "Successfully booked flight", Toast.LENGTH_SHORT).show();
        }catch (NoSuchFlightException e) {
            Toast.makeText(getApplicationContext(), "Flight does not exist", Toast.LENGTH_SHORT).show();
        }catch (NoSuchClientException e) {
            e.printStackTrace();
        }
        }






}
