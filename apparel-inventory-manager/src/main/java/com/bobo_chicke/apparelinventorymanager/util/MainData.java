package com.bobo_chicke.apparelinventorymanager.util;

import lombok.Data;

@Data
public class MainData {
    private String state;
    private int in_stock_count;
    private int out_stock_count;
    private int cargo_type_count;
    private int cargo_count;
}
