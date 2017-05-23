package cs.b07.cscb07courseproject;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.content.Intent;
import android.app.AlertDialog;
import android.widget.Toast;
import android.content.Context;
import android.widget.TextView;

import database.Constants;
import database.FileIO;
import user.*;
import database.Constants.*;
public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // get intent passed
        Intent intent = getIntent();

        // get key
        String email = intent.getSerializableExtra("emailKey").toString();
        String password = (String)intent.getSerializableExtra("passwordKey").toString();

        // set EditText
        EditText editEmailText = (EditText)findViewById(R.id.email_field);
        editEmailText.setText(email, TextView.BufferType.EDITABLE);
        EditText editPasswordText = (EditText)findViewById(R.id.password_field);
        editPasswordText.setText(password, TextView.BufferType.EDITABLE);

    }

    public void registerStudent(View view) {
        // moves to display service after
        //Intent intent = new Intent(this, Display);
        // get the field values
        // in order:
        EditText firstNameText = (EditText)findViewById(R.id.first_name_field);
        EditText lastNameText = (EditText)findViewById(R.id.last_name_field);
        EditText emailText = (EditText)findViewById(R.id.email_field);
        EditText passwordText = (EditText)findViewById(R.id.password_field);
        EditText passwordConf = (EditText)findViewById(R.id.password_conf_field);
        EditText addressText = (EditText)findViewById(R.id.address_field);
        EditText ccNumberText = (EditText)findViewById(R.id.ccNumber_field);
        EditText expiryDateText = (EditText)findViewById(R.id.expiry_date_field);
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String password_conf = passwordConf.getText().toString();
        String address = addressText.getText().toString();
        String ccNumber = ccNumberText.getText().toString();
        String expiryDate = expiryDateText.getText().toString();
        // check if email already exists
        if (Constants.clientDatabase.hasClient(email)) {
            // display a message that already contains user
            Context context = getApplicationContext();
            CharSequence text = "Email already in use!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else if (!password.equals(password_conf)){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        } else if (!(validate.vaidateInput.validateDate(expiryDate))) {
            Toast.makeText(this, "Incorrect Date Format. Format is: yyyy-mm-dd", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, LoginScreen.class);
            // create the User
            User user = new User(firstName, lastName,
                    email, address, ccNumber,
                    expiryDate, password);
            // Save to file
            Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
            // Say success
            Context context = getApplicationContext();
            CharSequence text = "Successfully Created!";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // pass User to MainActivity
            // put database in
            intent.putExtra(email, user);
            startActivity(intent);
        }
        // pass the User object to DisplayActivity
        //intent.putExtra("emailText.toString()", user);
        //startActivity(intent);

    }

}
