package com.stc.timelapse.demo;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

public class SetupActivity extends Activity {
    private static final String TAG = "SetupActivity";
    private static final int REQUSET_CAMERA_PERMISSION = 5335;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        checkPermissions();
    }

    private void checkPermissions() {
        if (ActivityCompat.checkSelfPermission(SetupActivity.this, "android.permission.CAMERA")!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(SetupActivity.this ,new String[]{"android.permission.CAMERA"}, REQUSET_CAMERA_PERMISSION);
        }else {
            startActivity(new Intent(this, CameraActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Granted camera permission", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, CameraActivity.class));
            finish();
        }else {
            Toast.makeText(this, "Denied camera permission", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "onRequestPermissionsResult: denied" );
        }
    }
}
