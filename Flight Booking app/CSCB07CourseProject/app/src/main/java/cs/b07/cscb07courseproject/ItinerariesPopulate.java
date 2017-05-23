package cs.b07.cscb07courseproject;

import android.content.Intent;
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
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import database.Constants;
import database.NoSuchCityException;
import database.NoSuchClientException;
import database.NoSuchFlightException;
import database.SortOrder;
import flight.FlightItinerary;
import user.User;

import static android.media.CamcorderProfile.get;
import static cs.b07.cscb07courseproject.R.string.destination;
import static cs.b07.cscb07courseproject.R.string.origin;

public class ItinerariesPopulate extends AppCompatActivity {
    private ListView lv;
    private ArrayList<String> itins;
    private ArrayList<String> searchParams;
    private User client;
    String selected;
    private int positionOn = 0;
    private int sort = 0;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_populating);
        lv = (ListView)findViewById(R.id.listView1);
        lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        // retrieve data
        // get intent
        Intent intent = getIntent();


        // get data
        itins = intent.getStringArrayListExtra("Itineraries");
        searchParams = intent.getStringArrayListExtra("searchParams");
        date = intent.getStringExtra("Date");
        User client = (User) intent.getSerializableExtra("User");
        this.client = client;
        //User client = (User) intent.getSerializableExtra("User");
        //this.client = client;
        // set button unclickable
        findViewById(R.id.book_flight).setEnabled(false);

        // add in header
        // but first check it's already not there
        if (itins.size() == 0) {
            itins.add(0, Constants.displayFormatItin);
        } else if (!(itins.get(0).equals(Constants.displayFormatItin))) {
            itins.add(0, Constants.displayFormatItin);
        }





        lv.setAdapter(new ArrayAdapter<String>(this,0, itins) {

            private View row;
            private LayoutInflater inflater = getLayoutInflater();
            private TextView tv;

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                row = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);

                tv = (TextView) row.findViewById(android.R.id.text1);

                tv.setText(itins.get(position));
                //Toast.makeText(getApplicationContext(),tv.toString(),Toast.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(),"" + position,Toast.LENGTH_SHORT).show();

                return row;
            }
        });
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,int position, long arg3)
            {
                // get the flight string
                selected = (String)lv.getItemAtPosition(position);
                if(position > 0) {
                    findViewById(R.id.book_flight).setEnabled(true);
                    positionOn = position;
                } else {
                    findViewById(R.id.book_flight).setEnabled(false);
                    positionOn = position;

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
            /**
            // positionOn is index of array
            String itineraryString = itins.get(positionOn);
            // get the itinerary's properties
            // origin, destination, totalprice, traveltime
            newItin.addFlight(Constants.flightDatabase.getFlight(split[0]));
            client.bookFlight(newItin);

             // get clients itin
             newItin.addFlight(Constants.flightDatabase.getFlight(split[0]));
             Constants.clientDatabase.searchClientEmail(client.getEmail()).bookFlight(newItin);
             //client.bookFlight(newItin);
             Toast.makeText(getApplicationContext(),client.getbookedFlights().toString() , Toast.LENGTH_SHORT).show();

             Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
             Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
             // update client

             Intent intent = new Intent(this, BookedFlights.class);
             intent.putExtra("User", Constants.clientDatabase.searchClientEmail(client.getEmail()));
             startActivity(intent);
             */
            // have itin
            //      List<FlightItinerary> itineraries = database.FlightDatabase.searchItineraries(origin,
            //destination, formattedDate, SortOrder.UNSORTED);
            //ArrayList<String> searchParams = new ArrayList<String>();
            //searchParams.add(departure);
            //searchParams.add(origin);
            //searchParams.add(destination);
            Date formattedDate = null;
            try {
                List<FlightItinerary> itineraries = null;
                formattedDate = Constants.date.parse(searchParams.get(0));

                itineraries = database.FlightDatabase.searchItineraries(searchParams.get(1),
                        searchParams.get(2), formattedDate, SortOrder.UNSORTED);


                // loop through itins and find match
                for (FlightItinerary itin: itineraries) {
                    if (itin.toString().equals(itins.get(positionOn))) {
                        // add it to bookedFlight
                        Constants.clientDatabase.searchClientEmail(client.getEmail()).bookFlight(itin);
                        Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
                        Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
                        break;

                    }
                }

                // update client

                Intent intent = new Intent(this, BookedFlights.class);
                intent.putExtra("User", Constants.clientDatabase.searchClientEmail(client.getEmail()));
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Successfully booked itinerary", Toast.LENGTH_SHORT).show();

            } catch (ParseException ex) {
                ex.printStackTrace();
            } catch (NoSuchCityException e) {
                e.printStackTrace();
            } catch (NoSuchClientException e) {
                e.printStackTrace();
            }

        } finally {

        }


    }


}
