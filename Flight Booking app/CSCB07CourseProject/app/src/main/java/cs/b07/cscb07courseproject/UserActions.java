package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import database.NoSuchClientException;
import user.*;

import database.Constants;

public class UserActions extends AppCompatActivity {
    private User client;
    private boolean viewAsAdmin;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_actions);
        intent = getIntent();
        // get client
        User client = (User) intent.getSerializableExtra("User");
        this.client = client;
        viewAsAdmin = (boolean) intent.getSerializableExtra("viewAsAdmin");
        // edit name fields
        String welcomeMessage;
        if(viewAsAdmin){
            welcomeMessage = String.format("Viewing %s %s's account.", client.getFirstName(), client.getLastName());
        }
        else{
            welcomeMessage = String.format("Welcome back %s!", client.getFirstName());
        }

        //EditText editEmailText = (EditText)findViewById(R.id.email_field);
        //editEmailText.setText(email, TextView.BufferType.EDITABLE);
        EditText firstNameText = (EditText) findViewById(R.id.welcome_name_display);
        firstNameText.setText(welcomeMessage);

    }

    public void goSearchFlights(View view) {
        Intent intent = new Intent(this, SearchFlights.class);
        intent.putExtra("viewAsAdmin", viewAsAdmin);
        intent.putExtra("User", client);
        startActivity(intent);
    }
    public void goClientEditInfo(View view){
        Intent intent = new Intent(this, ClientEditInfo.class);
        intent.putExtra("User", client);
        startActivity(intent);
    }
    public void goBookedFlights(View view) {
        Intent intent = new Intent(this, BookedFlights.class);
        try{
            User user =  Constants.clientDatabase.searchClientEmail(client.getEmail());
            intent.putExtra("User",user);
        }
        catch (NoSuchClientException e){
            intent = new Intent(this, LoginScreen.class);
            Toast.makeText(this, "Could not find user", Toast.LENGTH_SHORT).show();
        }
        startActivity(intent);
    }

    public void goSearchItinerary(View view) {
        Intent intent = new Intent(this, SearchItinerary.class);
        intent.putExtra("User", client);
        startActivity(intent);
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
