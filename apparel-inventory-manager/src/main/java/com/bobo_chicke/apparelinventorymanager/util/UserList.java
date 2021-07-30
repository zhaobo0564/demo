package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class UserList {
    private String state;
    private ArrayList<UserInfo> userlist;
}
