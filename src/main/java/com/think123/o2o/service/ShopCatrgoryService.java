package com.think123.o2o.service;

import com.think123.o2o.entity.ShopCategory;

import java.util.List;

public interface ShopCatrgoryService {
    public static final String SCLISTKEY = "shopcategorylist";
    /**
     * 根据查询条件获取ShopCategory列表
     *
     * @param shopCategoryCondition
     * @return
     */
    List<ShopCategory> getShopCategoryList(ShopCategory shopCategoryCondition);
}
