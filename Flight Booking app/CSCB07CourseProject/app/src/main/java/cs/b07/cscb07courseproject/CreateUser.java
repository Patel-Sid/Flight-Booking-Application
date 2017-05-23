package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import database.ClientExistsException;
import database.Constants;
import user.User;

public class CreateUser extends AppCompatActivity {
    private User admin;
    private EditText firstNameText, lastNameText, emailText, passwordText, passwordConf,
            addressText, ccNumberText;
    private DatePicker expiryDatePicker;
    private CheckBox isAdmin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
        admin = (User) getIntent().getSerializableExtra("Admin");

        setUp();
    }


    public void setUp(){
        firstNameText = (EditText)findViewById(R.id.first_name_field);
        lastNameText = (EditText)findViewById(R.id.last_name_field);
        emailText = (EditText)findViewById(R.id.email_field);
        passwordText = (EditText)findViewById(R.id.password_field);
        passwordConf = (EditText)findViewById(R.id.password_conf_field);
        addressText = (EditText)findViewById(R.id.address_field);
        ccNumberText = (EditText)findViewById(R.id.ccNumber_field);
        expiryDatePicker = (DatePicker) findViewById(R.id.expiry_date_field);
        expiryDatePicker.setMinDate(System.currentTimeMillis());
        isAdmin = (CheckBox) findViewById(R.id.isAdmin);
    }

    public void registerAccount(View view){
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String password_conf = passwordConf.getText().toString();
        String address = addressText.getText().toString();
        String ccNumber = ccNumberText.getText().toString();
        String expiry = String.format("%04d-%02d-%02d", expiryDatePicker.getYear(),
                expiryDatePicker.getMonth(), expiryDatePicker.getDayOfMonth());
        Boolean newAdmin = isAdmin.isChecked();
        
        if (!(password.equals(password_conf))){
            Toast.makeText(this, "Passwords do not match!", Toast.LENGTH_SHORT).show();
        }
        else if(Constants.clientDatabase.hasClient(email)){
            Toast.makeText(this, "Email in use!", Toast.LENGTH_SHORT).show();
        }
        else{
            try{
                User newUser = new User(firstName, lastName, email, address, ccNumber,
                        expiry, password);
                if (newAdmin){
                    newUser.makeAdmin();
                }
                Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
                Toast.makeText(this, "Account Created!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, AdminActions.class);
                intent.putExtra("Admin", admin);
                startActivity(intent);
                return;
            } catch (Exception e) {
                Toast.makeText(this, "Account creation failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
