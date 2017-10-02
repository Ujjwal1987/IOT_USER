package com.example.chase.iot_user;

/**
 * Created by chase on 03-05-2017.
 */

public class Group {
    String Grp_name, Grp_ID;

    public Group(String grp_name, String grp_ID) {
        Grp_name = grp_name;
        Grp_ID = grp_ID;
    }

    public String getGrp_name() {
        return Grp_name;
    }

    public void setGrp_name(String grp_name) {
        Grp_name = grp_name;
    }

    public String getGrp_ID() {
        return Grp_ID;
    }

    public void setGrp_ID(String grp_ID) {
        Grp_ID = grp_ID;
    }
}
