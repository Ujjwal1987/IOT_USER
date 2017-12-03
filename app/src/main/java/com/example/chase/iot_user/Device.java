package com.example.chase.iot_user;

/**
 * Created by chase on 06-08-2017.
 */

public class Device {
    String Dev_type, Dev_id;

    public Device(String dev_id, String dev_type) {
        Dev_id = dev_id;
        Dev_type = dev_type;
    }

    public String getDev_type() {
        return Dev_type;
    }

    public void setDev_type(String dev_type) {
        Dev_type = dev_type;
    }

    public void setDev_id(String dev_id) {
        Dev_id = dev_id;
    }
    public String getDev_id() {
        return Dev_id;
    }

}
