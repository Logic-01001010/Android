package com.example.flash;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class MainActivity extends AppCompatActivity {

    Button flashlight, blinkflash;
    private static final int CAMERA_REQUEST = 123;
    boolean hasCameraFlash = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        flashlight = findViewById(R.id.flashlightToggle);
        blinkflash = findViewById(R.id.blinkToggle);

        flashlight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hasCameraFlash) {
                    if (flashlight.getText().toString().contains("On")) {
                        flashlight.setText("Flashlight Off");
                        blinkflash.setText("Blink Flashlight Off");
                        flashLightOff();
                    } else {
                        blinkflash.setText("Blink Flashlight On");
                        flashlight.setText("Flashlight On");
                        flashLightOn();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Can't Using on this Device", Toast.LENGTH_SHORT).show();
                }
            }
        });

        blinkflash.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (flashlight.getText().toString().contains("On")) {
                    blinkflash();
                } else {
                    Toast.makeText(MainActivity.this, "Press the First Button", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void flashLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, false);
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void flashLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cameraId = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cameraId, true);
        } catch (CameraAccessException e) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void blinkflash() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        String myString = "0101010101";
        long blinkDelay = 50;
        for (int i = 0; i < myString.length(); i++) {
            if (myString.charAt(i) == '0') {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, true);
                } catch (CameraAccessException e) {
                }
            } else {
                try {
                    String cameraId = cameraManager.getCameraIdList()[0];
                    cameraManager.setTorchMode(cameraId, false);
                } catch (CameraAccessException e) {
                }
            }
            try {
                Thread.sleep(blinkDelay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}