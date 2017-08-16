package com.stc.timelapse.demo;

/**
 * Created by artem on 8/15/17.
 */

public interface CameraInterface {
    void takePicture();
    void showMessage(String msg);
    void updateCounter(int val);
    void saveLastPhotoPath(String path);
    String getLastPhotoPath();
}
