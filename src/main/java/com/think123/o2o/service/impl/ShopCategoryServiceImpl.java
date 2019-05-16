package com.think123.o2o.service.impl;

import com.think123.o2o.dao.ShopCategoryDao;
import com.think123.o2o.entity.ShopCategory;
import com.think123.o2o.service.ShopCatrgoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ShopCategoryServiceImpl implements ShopCatrgoryService {
    @Autowired
    private ShopCategoryDao shopCategoryDao;
    @Override
    public List<ShopCategory> getShopCategoryList(ShopCategory shopCategory) {
        return shopCategoryDao.queryShopCategory(shopCategory);
    }
}