package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

@Data
public class AddCargo {
    private String token;
    private String name;
    private String color;
    private String material;
    private String size;
    private String exfactoryprice;
    private String retailprice;
    private String manufacturer;
}
