package cs.b07.cscb07courseproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.content.Intent;
import android.widget.EditText;
import android.widget.Toast;


import java.text.ParseException;

import database.Constants;
import database.InvalidInputException;
import database.NoSuchClientException;
import user.*;

public class UpdatePassword extends AppCompatActivity {
    private User client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Get current user from previous activity
        Intent intent = getIntent();
        User client = (User) intent.getSerializableExtra("User");
        String userId = client.getEmail();
        try {
            this.client = Constants.clientDatabase.searchClientEmail(userId);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void updatePassword(View view) {
        // get the Edit fields
        EditText passwordText = (EditText)findViewById(R.id.new_password);
        String password = passwordText.getText().toString();
        // CHECK FOR LENGTH
        // CHECK FOR LENGTH
        // CHECK FOR LENGTH
        // CHECK FOR LENGTH
        //client.setPassword(password);
        try {
            Constants.clientDatabase.editClient(client, ClientField.PASSWORD, password);
            Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
            Toast.makeText(this, "Successfully updated password", Toast.LENGTH_SHORT).show();
            // send back to login
            Intent intent = new Intent(this, LoginScreen.class);
            // pass in the user
            intent.putExtra("User", client);
            startActivity(intent);
        } catch (NoSuchClientException ex) {
            Toast.makeText(this, "No Such Client", Toast.LENGTH_SHORT).show();

        } catch (InvalidInputException ex) {
            Toast.makeText(this, "Invalid Input", Toast.LENGTH_SHORT).show();


        } catch (ParseException ex) {
            Toast.makeText(this, "Parse error", Toast.LENGTH_SHORT).show();

        }





    }


}
