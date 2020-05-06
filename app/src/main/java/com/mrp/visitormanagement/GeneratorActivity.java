package com.mrp.visitormanagement;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.mrp.visitormanagement.helper.EncryptionHelper;
import com.mrp.visitormanagement.helper.QRCodeHelper;
import com.mrp.visitormanagement.models.UserObject;
import com.google.android.material.textfield.TextInputEditText;

public class GeneratorActivity extends AppCompatActivity {
    TextInputEditText fullNameTextField;
    TextInputEditText placeTextField;
    ImageView qrCodeView;
    Button qrCodeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generator);
        fullNameTextField = findViewById(R.id.fullNameEditText);
        placeTextField = findViewById(R.id.placeTextField);
        qrCodeView = findViewById(R.id.qrCodeImageView);
        qrCodeButton = findViewById(R.id.generateQrCodeButton);

        qrCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkEditText()) {
                    hideKeyboard();
                    UserObject user = new UserObject(fullNameTextField.getText().toString(), placeTextField.getText().toString());
                    String serializeString = new Gson().toJson(user);
                    String encryptedString = EncryptionHelper.getInstance().encryptionString(serializeString).encryptMsg();
                    setImageBitmap(encryptedString);
                }
            }
        });
    }

    private void setImageBitmap(String encryptedString) {
        Bitmap bitmap = QRCodeHelper.newInstance(this).setContent(encryptedString).setErrorCorrectionLevel(ErrorCorrectionLevel.Q).setMargin(2).getQRCOde();
        qrCodeView.setImageBitmap(bitmap);
    }

    /**
     * Hides the soft input keyboard if it is shown to the screen.
     */

    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }


    private boolean checkEditText(){
        if (TextUtils.isEmpty(fullNameTextField.getText().toString())) {
            Toast.makeText(this, "fullName field cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(placeTextField.getText().toString())) {
            Toast.makeText(this, "age field cannot be empty!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
