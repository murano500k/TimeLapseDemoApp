package com.stc.timelapse.demo;

import android.media.Image;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by artem on 8/15/17.
 */

public class CameraTaskManager {
    private static final String TAG = "CameraTaskManager";
    private static final long TASK_PERIOD_SECONDS = 1;
    CameraInterface cameraInterface;
    private Subscription subscription;
    private int counter;

    public CameraTaskManager(CameraInterface cameraInterface) {
        this.cameraInterface = cameraInterface;
    }
    public void scheduleTask(){
        Log.d(TAG, "scheduleTask() called");
        if(subscription!=null && !subscription.isUnsubscribed()) subscription.unsubscribe();
        counter=0;
        subscription = Observable.interval(1000, TASK_PERIOD_SECONDS*1000, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    public void call(Long aLong) {
                        if(counter++<10) cameraInterface.updateCounter(counter);
                        else {
                            cameraInterface.takePicture();
                            counter=0;
                        }
                    }
                });
    }
    public void cancelTask(){
        Log.d(TAG, "cancelTask() called");
        if(subscription!=null && !subscription.isUnsubscribed())subscription.unsubscribe();
    }

    public void savePhoto(final Image image) {
        Log.d(TAG, "savePhoto() called with: image = [" + image + "]");

        Observable.fromCallable(new Callable<String>() {

            @Override
            public String call() throws Exception {
                File picFile = createPicFile();

                ByteBuffer buffer = image.getPlanes()[0].getBuffer();
                byte[] bytes = new byte[buffer.remaining()];
                buffer.get(bytes);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(picFile);
                    output.write(bytes);
                    cameraInterface.saveLastPhotoPath(picFile.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "save err: ",e );
                } finally {
                    image.close();
                    if (null != output) {
                        try {
                            output.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }
                return picFile.getAbsolutePath();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, "save: "+s);
                        cameraInterface.updateCounter(0);
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        Log.e(TAG, "save: ",throwable );
                        cameraInterface.showMessage(throwable.getMessage());
                    }
                });
    }
    public File createPicFile(){
        SimpleDateFormat format=new SimpleDateFormat("EEE-d-MMM_HH-mm-ss");
        Date date= Calendar.getInstance().getTime();
        String picName ="/sdcard/timelapse/pic_"+ format.format(date)+".jpg";
        Log.w(TAG, "createFile: "+picName );

        File file = new File(picName );

        if(!file.exists()){
            try {
                Log.d(TAG, "file dont exist. Create result = "+file.createNewFile());
            } catch (IOException e) {
                e.printStackTrace();
                Log.e(TAG, "createPicFile: ",e );
                return null;
            }
        }
        return file;
    }


}
