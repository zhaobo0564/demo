package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

@Data
public class Cargo {
    private String id;
    private String name;
    private String color;
    private String material;
    private String size;
    private String exfactoryprice;
    private String retailprice;
    private String manufacturer;
    private int count;
}
