package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class ReturnGetCargo {
    private String state;
    private ArrayList<Cargo> cargos;
}
