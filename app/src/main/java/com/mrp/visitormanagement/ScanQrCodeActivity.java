package com.mrp.visitormanagement;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import android.widget.TextView;


import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;


import java.util.Collections;


import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScanQrCodeActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private static final int MY_CAMERA_REQUEST_CODE = 6515;
    private ImageView QrcodeImageView;
    private ImageView flashOnOffImageView;
    private ZXingScannerView qrCodeScanner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_scan_qr_code);

        QrcodeImageView = findViewById(R.id.barcodeBackImageView);
        flashOnOffImageView = findViewById(R.id.flashOnOffImageView);
        qrCodeScanner = findViewById(R.id.qrCodeScanner);
        QrcodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setScannerProperties();
        QrcodeImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (qrCodeScanner.getFlash()) {
                    qrCodeScanner.setFlash(false);
                    flashOnOffImageView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.flash_on_vector_icon));
                } else {
                    qrCodeScanner.setFlash(true);
                    flashOnOffImageView.setBackground(ContextCompat.getDrawable(view.getContext(), R.drawable.flash_on_vector_icon));
                }
            }
        });
    }

    /**
     * Set bar code scanner basic properties.
     */

    private void setScannerProperties() {
        qrCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.QR_CODE));
        qrCodeScanner.setAutoFocus(true);
        qrCodeScanner.setLaserColor(R.color.colorAccent);
        qrCodeScanner.setMaskColor(R.color.colorAccent);
        qrCodeScanner.setAspectTolerance(0.5f);
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 6515) {
            if (grantResults[0] == 0) {
                this.openCamera();
            } else if (grantResults[0] == -1) {
                this.showCameraSnackBar();
            }
        }

    }

    private final void openCamera() {
        qrCodeScanner.startCamera();
        qrCodeScanner.setResultHandler(this);
    }


    private final void showCameraSnackBar() {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) this, "android.permission.CAMERA")) {
            Snackbar snackbar = Snackbar.make(findViewById(R.id.scanQrCodeRootView), this.getResources().getString(R.string.app_needs_your_camera_permission_in_order_to_scan_qr_code), Snackbar.LENGTH_LONG);
            View view1 = snackbar.getView();
            view1.setBackgroundColor(ContextCompat.getColor((Context) this, R.color.whiteColor));
            TextView textView = (TextView) view1.findViewById(R.id.snackbar_text);
            textView.setTextColor(ContextCompat.getColor((Context) this, R.color.colorPrimary));
            snackbar.show();
        }

    }


    @Override
    public void handleResult(Result result) {
        if (result != null) {
            Intent sIntent = new Intent(ScanQrCodeActivity.this, ScannedReader.class).putExtra("scanned_string", result.getText());
            startActivity(sIntent);
//            resumeCamera();
        }
    }

    private void resumeCamera() {
        Handler handler = new Handler();
        handler.postDelayed((Runnable) (new Runnable() {
            public final void run() {
                qrCodeScanner.resumeCameraPreview(ScanQrCodeActivity.this);
            }
        }), 2000L);
    }

    protected void onPause() {
        super.onPause();
        qrCodeScanner.stopCamera();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 6515);
            } else {
                qrCodeScanner.startCamera();
                qrCodeScanner.setResultHandler(this);
            }
        }
    }
}
