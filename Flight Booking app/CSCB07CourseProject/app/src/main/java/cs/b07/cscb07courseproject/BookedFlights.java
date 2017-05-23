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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import database.Constants;
import database.NoSuchClientException;
import database.NoSuchFlightException;
import flight.Flight;
import flight.FlightItinerary;
import flight.InvalidFlightAddition;
import user.User;

import static android.view.View.GONE;

public class BookedFlights extends AppCompatActivity {
    private ListView lv;
    private ArrayList<String> myData;
    private User client;
    private Intent intent;
    String selected;

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
        User client = (User) intent.getSerializableExtra("User");
        this.client = client;
        List<String> itins = client.getbookedFlights();
        // create a ArrayList<String> for each flight itin
        myData = new ArrayList<String>();
        for (String itin: itins) {
            // get an itinerary
            // format it
            myData.add(itin);
        }
        // remove duplicates
        Set<String> hs = new HashSet<>();
        hs.addAll(myData);
        myData.clear();
        myData.addAll(hs);
        //User client = (User) intent.getSerializableExtra("User");
        //this.client = client;
        // set button unclickable
        findViewById(R.id.book_flight).setVisibility(GONE);
        // add in header
        myData.add(0, "Flight Itineraries");




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
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener()
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
        Toast.makeText(getApplicationContext(), this.client.getbookedFlights().size(), Toast.LENGTH_SHORT).show();



    }
    @Override
    protected void onStop() {
        // save data
        setResult(RESULT_OK, intent);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        setResult(RESULT_OK, intent);
        super.onDestroy();

    }

    @Override
    public void finish() {
        setResult(RESULT_OK, intent);
        super.finish();
    }
    @Override
    public void onPause() {
        // commit data
        setResult(RESULT_OK, intent);
        super.onPause();
    }


}
