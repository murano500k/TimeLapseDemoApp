package com.stc.timelapse.demo;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by artem on 8/14/17.
 */

public class OptionsHelper {
    private static final String TAG = "OptionsHelper";

    public static File getNextFile(Context context) {
        File outdir = new File(context.getString(R.string.out_dir_path));
        if(!outdir.exists()) {
            Log.d(TAG, "create dir");
            outdir.mkdirs();
        }else Log.d(TAG, "dir exists");
        SimpleDateFormat format=new SimpleDateFormat("EEE-d-MMM_HH-mm-ss");
        Date date=Calendar.getInstance().getTime();
        String picName =outdir.getAbsolutePath()+ format.format(date)+".jpg";
        Log.w(TAG, "getNextFile: "+picName );
        File pic =new File(picName);
        try {
            pic.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pic;
    }
}
