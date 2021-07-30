package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class Stock {
    private String id;
    private String remarks;
    private String date;
    private ArrayList<Cargo> cargos;
}
