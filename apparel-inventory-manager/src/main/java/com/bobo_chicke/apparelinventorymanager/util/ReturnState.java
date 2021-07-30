package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReturnState {
    private String state;
    private String token;
    private ArrayList<String> fail;
}
