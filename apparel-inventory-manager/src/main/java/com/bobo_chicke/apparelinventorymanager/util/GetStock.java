package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

import java.util.ArrayList;

@Data
public class GetStock {
    private String state;
    private ArrayList<Stock> stocks;
}
