package cs.b07.cscb07courseproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import android.widget.ListView;
import android.app.Activity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.view.Gravity;
import android.util.TypedValue;
import android.graphics.Typeface;
import java.lang.Object;
import java.util.List;
import android.widget.AdapterView;
import android.widget.Toast;

import android.view.View;

import flight.Flight;


public class DisplaySearchFlights extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_search_flights);

        // get intent
        Intent intent = getIntent();
        ArrayList<Flight> flights = (ArrayList<Flight>) intent.getSerializableExtra("Searches");

        populateListView(flights);
        registerClickCallback();

        /**
        // get list

        ArrayAdapter<Flight> adapter = new ArrayAdapter<Flight>(this,
                R.layout.activity_display_search_flights, flights);

        ListView listView = (ListView) findViewById(R.id.list_of_flights);
        listView.setAdapter(adapter);
         */
    }

    private void populateListView(ArrayList<Flight> flights) {
        // get the list of items

        // build adapter
        // context, layout to use, items to display
        ArrayAdapter<Flight> adapter = new ArrayAdapter<Flight>(this,
                R.layout.displaytable, flights);
        // configure listview
        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setAdapter(adapter);
    }
    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.listViewMain);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                TextView textView = (TextView) viewClicked;
                String message = "You clicked # " + position + ", which is string: " + textView.getText().toString();
                Toast.makeText(DisplaySearchFlights.this, message, Toast.LENGTH_LONG).show();
            }
        }
        );
    }


}
