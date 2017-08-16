package com.stc.timelapse.demo;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import java.io.File;

import static android.content.ContentValues.TAG;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, 0);
        }else {
            createDir();
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]==PackageManager.PERMISSION_GRANTED){
            createDir();
            startActivity(new Intent(this, SetupActivity.class));
            finish();
        }
        else Toast.makeText(this, "No perm", Toast.LENGTH_SHORT).show();
    }
    private void createDir() {
        String picName = "/sdcard/timelapse";
        Log.w(TAG, "createDir: " + picName);

        File file = new File(picName);

        if (!file.exists()) {
            Log.d(TAG, "dir dont exist. Create result = " + file.mkdirs());
        }
        Log.d(TAG, "createDir: " + file.getAbsolutePath());
    }
}
