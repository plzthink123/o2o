package com.think123.o2o.service.impl;

import com.think123.o2o.Execption.ShopOperationExecption;
import com.think123.o2o.dao.ShopDao;
import com.think123.o2o.dto.ShopExecution;
import com.think123.o2o.entity.Shop;
import com.think123.o2o.enums.ShopStateEnum;
import com.think123.o2o.service.ShopService;
import com.think123.o2o.util.ImageUtil;
import com.think123.o2o.util.PageCalculator;
import com.think123.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

@Service
public class ShopServiceImpl implements ShopService {
    @Autowired
    private ShopDao shopDao;

    @Override
    public ShopExecution getShopList(Shop shopCondition, int pageIndex, int pageSize) {
        //将页码转换成行码
        int rowIndex = PageCalculator.calculateRowIndex(pageIndex, pageSize);
        //依据查询条件，调用dao层返回相关的店铺列表
        List<Shop> shopList = shopDao.queryShopList(shopCondition, rowIndex, pageSize);
        //依据相同的查询条件，返回店铺总数
        int count = shopDao.queryShopCount(shopCondition);
        ShopExecution se = new ShopExecution();
        if (shopList != null) {
            se.setShopList(shopList);
            se.setCount(count);
        } else {
            se.setState(ShopStateEnum.INNER_ERROR.getState());
        }
        return se;
    }
    @Override
    @Transactional
    public ShopExecution addShop(Shop shop, InputStream shopImg, String fileName) {
        //控制判断
        if(shop==null){
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
                   try{
                       //addShop
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
    @Override
    public Shop getByShopId(long shopId) {
        return shopDao.queryByShopId(shopId);
    }

    @Override
    public ShopExecution modifyShop(Shop shop, InputStream shopImgInputStream, String fileName) throws ShopOperationExecption {
    try{
        if(shop==null||shop.getShopId()==null){
            return new ShopExecution(ShopStateEnum.NULL_SHOP);
        }else{
            //1.判断是否需要处理图片
            if(shopImgInputStream!=null&&fileName!=null&&!"".equals(fileName)){
                Shop tempShop = shopDao.queryByShopId(shop.getShopId());
                if(tempShop.getShopImg()!=null){
                    ImageUtil.deleteFileOrPath(tempShop.getShopImg());
                }
                addShopImg(shop,shopImgInputStream,fileName);
            }
        }
        //2.更新店铺信息
        shop.setLastEditTime(new Date());
        int i = shopDao.updateShop(shop);
        if(i<=0){
            return new ShopExecution(ShopStateEnum.INNER_ERROR);
        }else{
            shop=shopDao.queryByShopId(shop.getShopId());
            return new ShopExecution(ShopStateEnum.SUCCESS);
        }}catch (Exception e){
        throw  new ShopOperationExecption("modifyShopError"+e.getMessage());
    }
    }

}