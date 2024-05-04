package com.example.scarlet.Interface;

import com.example.scarlet.Data.Product;
import com.example.scarlet.Data.ProductQuantity;

import java.util.List;

public interface DataReadyListener {
    void onDataReady(List<ProductQuantity> productQuantityList);
}
