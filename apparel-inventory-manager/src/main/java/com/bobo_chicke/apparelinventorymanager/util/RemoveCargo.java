package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class RemoveCargo {
    private String token;
    private ArrayList<String> cargos;
}
