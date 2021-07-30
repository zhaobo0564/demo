package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class AddStock {
    private String token;
    private String remarks;
    private ArrayList<Cargo> cargos;
}
