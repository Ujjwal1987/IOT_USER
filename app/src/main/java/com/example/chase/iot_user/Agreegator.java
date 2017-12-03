package com.example.chase.iot_user;

/**
 * Created by chase on 20-11-2017.
 */

public class Agreegator {

    String agree_name, agree_id;

    public Agreegator(String agree_name, String agree_id) {
        this.agree_id = agree_id;
        this.agree_name = agree_name;
    }

    public String getAgree_name() {
        return agree_name;
    }

    public void setAgree_name(String agree_name) {
        this.agree_name = agree_name;
    }

    public String getAgree_id() {
        return agree_id;
    }

    public void setAgree_id(String agree_id) {
        this.agree_id = agree_id;
    }
}
