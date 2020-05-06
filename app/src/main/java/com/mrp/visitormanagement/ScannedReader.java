package com.mrp.visitormanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.mrp.visitormanagement.models.UserObject;
import com.mrp.visitormanagement.helper.EncryptionHelper;

public class ScannedReader extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_reader);
        if (this.getIntent().getSerializableExtra("scanned_string") == null) {
            try {
                throw (Throwable)(new RuntimeException("No encrypted String found in intent"));
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        } else {
            String decryptedString = EncryptionHelper.getInstance().getDecryptionString(this.getIntent().getStringExtra("scanned_string"));
            UserObject userObject = (UserObject)(new Gson()).fromJson(decryptedString, UserObject.class);
            TextView Name = (TextView)findViewById(R.id.scannedFullNameTextView);
            Name.setText(userObject.getFullName().toString());
            TextView place = (TextView)findViewById(R.id.scannedAgeTextView);
            place.setText(String.valueOf(userObject.getPlace()));
        }
    }
}
