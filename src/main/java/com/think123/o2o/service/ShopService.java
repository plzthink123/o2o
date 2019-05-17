package com.think123.o2o.service;

import com.think123.o2o.Execption.ShopOperationExecption;
import com.think123.o2o.dto.ShopExecution;
import com.think123.o2o.entity.Shop;

import java.io.InputStream;

public interface ShopService {
    /**
     * 添加商铺
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationExecption
     */
    ShopExecution addShop(Shop shop, InputStream shopImgInputStream,String fileName) throws ShopOperationExecption;

    /**
     * 通过店铺Id获取店铺信息
     *
     * @param shopId
     * @return
     */
    Shop getByShopId(long shopId);

    /**
     * 更新店铺信息，包括对图片的处理
     * @param shop
     * @param shopImgInputStream
     * @param fileName
     * @return
     * @throws ShopOperationExecption
     */
    ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationExecption;
    /**
     * 根据shopCondition分页返回相应店铺列表
     *
     * @param shopCondition
     * @param pageIndex
     * @param pageSize
     * @return
     */
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize);


}