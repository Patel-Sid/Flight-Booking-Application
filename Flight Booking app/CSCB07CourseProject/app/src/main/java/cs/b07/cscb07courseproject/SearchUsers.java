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


import database.Constants;
import database.NoSuchClientException;
import user.*;

public class SearchUsers extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);
    }

    public void searchUsers(View view) {
        // get the email field
        EditText emailField = (EditText)findViewById(R.id.email_search_field);
        String email = emailField.getText().toString();
        // try to retrieve client
        try {
            User client = Constants.clientDatabase.searchClientEmail(email);
            // have client, now have options
            // ideally call the ClientEditInfo, pass along the client
            Intent intent = new Intent(this, UserActions.class);
            // this passes the client into ClientEditInfo, where the activity edits the info
            intent.putExtra("User", client);
            intent.putExtra("viewAsAdmin", true);
            startActivity(intent);

        } catch (NoSuchClientException ex) {
            ex.printStackTrace();
            // print message does not exist
            Context context = getApplicationContext();
            CharSequence text = "User does not exist";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }

    }

}
