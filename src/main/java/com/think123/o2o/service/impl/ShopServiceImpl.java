package com.think123.o2o.service.impl;

import com.think123.o2o.Execption.ShopOperationExecption;
import com.think123.o2o.dao.ShopDao;
import com.think123.o2o.dto.ShopExecution;
import com.think123.o2o.entity.Shop;
import com.think123.o2o.enums.ShopStateEnum;
import com.think123.o2o.service.ShopService;
import com.think123.o2o.util.ImageUtil;
import com.think123.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImg, String fileName) {
        //控制判断
        if(shop!=null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }
        try {
            //1.增加信息
            //将状态置为0 ,审核中
            shop.setEnableStatus(0);
            shop.setCreateTime(new Date());
            shop.setLastEditTime(new Date());
            int effectNum=shopDao.insertShop(shop);
            if(effectNum<=0){
                throw new ShopOperationExecption("店铺创建失败!");
            }else{
                if(shopImg!=null){
                    //存储图片
                   try {
//                       addShop
                       addShopImg(shop,shopImg,fileName);
                   }catch (Exception e){
                       throw  new ShopOperationExecption("addShopImg ERROR"+e.getMessage());
                   }
                   //更新店铺的图片地址
                    effectNum=shopDao.updateShop(shop);
                   if(effectNum<=0){
                       throw  new ShopOperationExecption("更新图片地址失败");
                   }
                }
            }

        }catch (Exception e){
            throw  new RuntimeException("addShop error "+e.getMessage());
        }
        return new ShopExecution(ShopStateEnum.CHECK,shop);
    }

    private void addShopImg(Shop shop, InputStream shopImg,String fileName) {
        //获取shop图片目录的相对值路径
       String dest= PathUtil.getShopImagePath(shop.getShopId());
       String shopImgAddr= ImageUtil.generateThumbnail(shopImg,fileName,dest);
       shop.setShopImg(shopImgAddr);
    }
}