package com.think123.o2o.service;

import com.think123.o2o.dto.ShopExecution;
import com.think123.o2o.entity.Shop;

import java.io.InputStream;

public interface ShopService {
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName);

}