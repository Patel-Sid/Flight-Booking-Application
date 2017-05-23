package cs.b07.cscb07courseproject;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import database.Constants;
import database.ClientDatabase;
import database.NoSuchClientException;
import user.ClientField;
import user.User;

public class ClientEditInfo extends AppCompatActivity {
    private Spinner selectSpinner;
    ArrayAdapter<CharSequence> adapter;
    private User client;
    private EditText valueEntry, valueConfirm;
    private DatePicker expDate;
    private Button submitButton;
    private static DateFormat date = new SimpleDateFormat("yyyy-MM-dd");
    private ClientField editField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_edit_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        // Fill table for current user info
        displayUser();

        // Set up fields for user action
        setUp();

        // Allow user to interact
        userAction();
    }

    private void displayUser(){
        // Fill table for current user info
        TextView clientUser = (TextView) findViewById(R.id.clientUser);
        TextView clientFirstName = (TextView) findViewById(R.id.clientFirstName);
        TextView clientLastName = (TextView) findViewById(R.id.clientLastName);
        TextView clientAddress = (TextView) findViewById(R.id.clientAddress);
        TextView clientCreditCard = (TextView) findViewById(R.id.clientCreditCard);
        TextView clientExpiryDate = (TextView) findViewById(R.id.clientExpiryDate);
        clientUser.setText(client.getEmail());
        clientFirstName.setText(client.getFirstName());
        clientLastName.setText(client.getLastName());
        clientAddress.setText(client.getAddress());
        clientCreditCard.setText(client.getCcNumber());
        clientExpiryDate.setText(date.format(client.getExpiryDate()));
    }

    private void setUp(){
        selectSpinner = (Spinner) findViewById(R.id.field_select);
        adapter = ArrayAdapter.createFromResource(this, R.array.clientField_select,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectSpinner.setAdapter(adapter);
        valueEntry = (EditText) findViewById(R.id.new_value);
        valueConfirm = (EditText) findViewById(R.id.confirm_value);
        expDate = (DatePicker) findViewById(R.id.date_picker);
        expDate.setMinDate(System.currentTimeMillis());
        submitButton = (Button) findViewById(R.id.submit_button);
        valueEntry.setVisibility(EditText.GONE);
        valueConfirm.setVisibility(EditText.GONE);
        expDate.setVisibility(DatePicker.GONE);
        submitButton.setEnabled(false);
    }

    private void userAction(){
        selectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                String selected = (String) parent.getItemAtPosition(pos);
                if (pos == 0){
                    valueEntry.setVisibility(EditText.GONE);
                    valueConfirm.setVisibility(EditText.GONE);
                    submitButton.setEnabled(false);
                    expDate.setVisibility(DatePicker.GONE);
                }
                else if(pos == 1){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    valueConfirm.setVisibility(EditText.VISIBLE);
                    submitButton.setEnabled(true);
                    expDate.setVisibility(DatePicker.GONE);
                    valueEntry.setHint("Enter new password");
                    valueConfirm.setHint("Confirm password");
                    editField = ClientField.PASSWORD;
                }
                else if(pos == 2){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    valueConfirm.setVisibility(EditText.GONE);
                    submitButton.setEnabled(true);
                    expDate.setVisibility(DatePicker.GONE);
                    valueEntry.setHint("Enter new First Name");
                    editField = ClientField.FIRSTNAME;
                }
                else if(pos == 3){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    valueConfirm.setVisibility(EditText.GONE);
                    submitButton.setEnabled(true);
                    expDate.setVisibility(DatePicker.GONE);
                    valueEntry.setHint("Enter new Last Name");
                    editField = ClientField.LASTNAME;
                }
                else if(pos == 4){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    valueConfirm.setVisibility(EditText.GONE);
                    submitButton.setEnabled(true);
                    expDate.setVisibility(DatePicker.GONE);
                    valueEntry.setInputType(InputType.TYPE_CLASS_TEXT);
                    valueEntry.setHint("Enter new Address");
                    editField = ClientField.ADDRESS;
                }
                else if(pos == 5){
                    valueEntry.setVisibility(EditText.VISIBLE);
                    valueConfirm.setVisibility(EditText.GONE);
                    valueEntry.setInputType(InputType.TYPE_CLASS_TEXT);
                    submitButton.setEnabled(true);
                    expDate.setVisibility(DatePicker.VISIBLE);
                    valueEntry.setHint("Enter new Credit Card Number");
                    editField = ClientField.CREDITCARD;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void editInfo(View view){
        int result = -1;
        if (editField == ClientField.PASSWORD){
            result = editPassword();
        } else if (editField == ClientField.FIRSTNAME) {
            result = editFirstName();
        } else if (editField == ClientField.LASTNAME) {
            result = editLastName();
        } else if (editField == ClientField.ADDRESS) {
            result = editAddress();
        }else if (editField == ClientField.CREDITCARD) {
            result = editCreditCard();
        }
        // Save changes and Toast confirmation if successful
        if (result==0) {
            Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
            displayUser();
            Toast.makeText(this, "Change successful", Toast.LENGTH_SHORT).show();
        }
    }

    private int editPassword(){
        // Check that both passwords are the same, then save password
        String password = valueEntry.getText().toString();
        String passwordConf = valueConfirm.getText().toString();
        if (password.equals(passwordConf)){
            try {
                Constants.clientDatabase.editClient(client, ClientField.PASSWORD, password);
                return 0;
            }
            catch (Exception e){
                Toast.makeText(this, "Password update failed", Toast.LENGTH_LONG).show();
                return -1;
            }
        }
        else{
            Toast.makeText(this, "Password must match", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private int editFirstName(){
        String newName = valueEntry.getText().toString();
        try {
            Constants.clientDatabase.editClient(client, ClientField.FIRSTNAME, newName);
            return 0;
        }
        catch (Exception e){
            Toast.makeText(this, "Name change failed", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private int editLastName(){
        String newName = valueEntry.getText().toString();
        try {
            Constants.clientDatabase.editClient(client, ClientField.LASTNAME, newName);
            return 0;
        }
        catch (Exception e){
            Toast.makeText(this, "Name change failed", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private int editAddress(){
        String newAddress = valueEntry.getText().toString();
        try {
            Constants.clientDatabase.editClient(client, ClientField.ADDRESS, newAddress);
            return 0;
        }
        catch (Exception e){
            Toast.makeText(this, "Address change failed", Toast.LENGTH_LONG).show();
            return -1;
        }
    }

    private int editCreditCard(){
        try{
            String newCreditCard = valueEntry.getText().toString();
            String newExp = String.format("%d-%02d-%02d",
                    expDate.getYear(), expDate.getMonth()+1, expDate.getDayOfMonth());
            Constants.clientDatabase.editClient(client, ClientField.CREDITCARD, newCreditCard);
            Constants.clientDatabase.editClient(client, ClientField.EXPIRYDATE, newExp);
            return 0;
        }
        catch (Exception e){
            Toast.makeText(this, "Credit Card change failed", Toast.LENGTH_LONG).show();
            return -1;
        }
    }


}
