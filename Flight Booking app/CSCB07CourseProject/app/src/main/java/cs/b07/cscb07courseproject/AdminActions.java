package cs.b07.cscb07courseproject;

import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import database.ClientDatabase;
import database.Constants;
import database.InvalidFileException;
import user.Administrator;
import user.User;

public class AdminActions extends AppCompatActivity {
    private User admin;
    private ClientDatabase clients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_actions);
        Intent intent = getIntent();
        // get client
        User admin = (User) intent.getSerializableExtra("Admin");
        this.admin = admin;
        // edit name fields
        String welcomeMessage = String.format("Hello %s!", admin.getFirstName());

        //EditText editEmailText = (EditText)findViewById(R.id.email_field);
        //editEmailText.setText(email, TextView.BufferType.EDITABLE);
        TextView firstNameText = (TextView) findViewById(R.id.welcome_name_display);
        firstNameText.setText(welcomeMessage);

    }




    public void goSearchUsers(View view) {
        Intent intent = new Intent(this, SearchUsers.class);
        startActivity(intent);
    }

    public void goEditFlights(View view) {
        Intent intent = new Intent(this, EditFlightInfo.class);
        startActivity(intent);
    }

    public void goCreateFlight(View view){
        Intent intent = new Intent(this, CreateFlight.class);
        intent.putExtra("Admin", admin);
        startActivity(intent);
    }

    /**
     * Loads information from a csv file specified by the user, and
     * passes this information to DisplayActivity.
     * @param view as usual
     */


    public void loadFileClients(View view) {
        EditText fileName = (EditText) findViewById(R.id.file_name);
        String fileNameText = fileName.getText().toString();


        File userdata = this.getApplicationContext().getFilesDir();
        File clientsFile = new File(userdata, fileNameText);

        try {
            Constants.clientDatabase.uploadClientInfo(clientsFile.getPath());
            Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
            Toast.makeText(this, "Successfully Loaded", Toast.LENGTH_SHORT).show();

        } catch (InvalidFileException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    public void loadFileFlights(View view) {
        EditText fileName = (EditText) findViewById(R.id.file_name);
        String fileNameText = fileName.getText().toString();


        File flightData = this.getApplicationContext().getFilesDir();
        File flightsFile = new File(flightData, fileNameText);

        try {
            Constants.flightDatabase.uploadFlightInfo(flightsFile.getPath());
            Constants.fileManager.saveFlightDatabase(LoginScreen.flightDatabase);
            Toast.makeText(this, "Successfully Loaded", Toast.LENGTH_SHORT).show();

        } catch (InvalidFileException ex) {
            ex.printStackTrace();
        }

    }


    /**
     public void loadFileJ(View view) {
     EditText fileName = (EditText) findViewById(R.id.file_name);
     String fileNameText = fileName.getText().toString();
     // location of the file
     String path = "/src/main/assets/";
     path = path + fileNameText;
     // send to uploadClientInfo
     try {
     Constants.clientDatabase.uploadClientInfo(path);
     Context context = getApplicationContext();
     CharSequence text = "Successfully Loaded!";
     int duration = Toast.LENGTH_SHORT;

     Toast toast = Toast.makeText(context, text, duration);
     toast.show();
     } catch (IOException ex) {
     ex.printStackTrace();
     } catch (InvalidFileException ex) {
     ex.printStackTrace();
     }



     }
     */
    public void loadFileJ(View view) {
        // get fields
        EditText fileName = (EditText) findViewById(R.id.file_name);
        String fileNameText = fileName.getText().toString();
        AssetManager am = this.getAssets();

        try {
            InputStream is = this.getResources().openRawResource(R.raw.clients);
            //InputStream is = am.open(fileNameText);
            try {
                Constants.clientDatabase.uploadClientInfoStream(is);
                Constants.fileManager.saveClientDatabase(LoginScreen.clientDatabase);
                Toast.makeText(this, "Successfully Loaded", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        //InputStream is = this.getResources().openRawResource(R.raw.clients);
    }

    public void loadFlightFile(View view) {
        // get fields
        EditText fileName = (EditText) findViewById(R.id.file_name);
        String fileNameText = fileName.getText().toString();
        AssetManager am = this.getAssets();
        try {
            InputStream is = this.getResources().openRawResource(R.raw.flights1);
            try {
                Constants.flightDatabase.uploadFlightInfoStream(is);
                Constants.flightFileManager.saveFlightDatabase(LoginScreen.flightDatabase);
                Toast.makeText(this, "Successfully Loaded", Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

        } catch (Exception ex) {
            Toast.makeText(this, "Test", Toast.LENGTH_SHORT).show();
            ex.printStackTrace();
        }
        //InputStream is = this.getResources().openRawResource(R.raw.clients);
    }
}
