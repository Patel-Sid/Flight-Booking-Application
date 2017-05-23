package database;

import android.content.Context;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;

import cs.b07.cscb07courseproject.LoginScreen;
import user.Administrator;
import user.User;

public class FileIO implements Serializable{

    private static final long serialVersionUID = -9055766510788075198L;

    public FileIO(){
    }

    public void saveFlightDatabase(File flightFile) {
        try{
            OutputStream file = new FileOutputStream(flightFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(Constants.flightDatabase);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveClientDatabase(File clientFile) {
        try{
            OutputStream file = new FileOutputStream(clientFile);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);
            output.writeObject(Constants.clientDatabase);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void readFlightDatabase(File flightFile){
        if (flightFile.exists()){
            String filePath = flightFile.getPath();
            try{
                InputStream file = new FileInputStream(filePath);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);
                FlightDatabase flightDb = (FlightDatabase)input.readObject();
                Constants.flightDatabase = flightDb;
                input.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                flightFile.createNewFile();
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void readClientDatabase(File clientFile){
        if (clientFile.exists()){
            String filePath = clientFile.getPath();
            try {
                InputStream file = new FileInputStream(filePath);
                InputStream buffer = new BufferedInputStream(file);
                ObjectInput input = new ObjectInputStream(buffer);
                ClientDatabase clientDb = (ClientDatabase) input.readObject();
                Constants.clientDatabase = clientDb;
                input.close();
                if (!Constants.clientDatabase.hasClient("admin")){
                    User admin = new User("Adminstrator", "Account", "admin", "N/a", "N/a",
                            "2000-01-01", "password");
                    admin.makeAdmin();
                    Constants.clientDatabase.addClient(admin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                clientFile.createNewFile();
                if (!Constants.clientDatabase.hasClient("admin")){
                    User admin = new User("Adminstrator", "Account", "admin", "N/a", "N/a",
                            "2000-01-01", "password");
                    admin.makeAdmin();
                    Constants.clientDatabase.addClient(admin);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
