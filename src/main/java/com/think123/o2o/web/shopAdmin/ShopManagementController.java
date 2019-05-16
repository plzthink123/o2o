package com.think123.o2o.web.shopAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.think123.o2o.dto.ShopExecution;
import com.think123.o2o.entity.Area;
import com.think123.o2o.entity.PersonInfo;
import com.think123.o2o.entity.Shop;
import com.think123.o2o.entity.ShopCategory;
import com.think123.o2o.enums.ShopStateEnum;
import com.think123.o2o.service.AreaService;
import com.think123.o2o.service.ShopCatrgoryService;
import com.think123.o2o.service.ShopService;
import com.think123.o2o.util.HttpServletRequestUtil;
import com.think123.o2o.util.ImageUtil;
import com.think123.o2o.util.PathUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 实现店铺管理
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopManagementController  {

    @Autowired
    private ShopService shopService;
    @Autowired
    private ShopCatrgoryService shopCatrgoryService;
    @Autowired
    private AreaService areaService;

    @RequestMapping(value = "/getshopinitinfo", method = RequestMethod.GET)
    @ResponseBody
    private Map<String, Object> getShopInitInfo() {
        Map<String, Object> modelMap = new HashMap<String, Object>();
        List<ShopCategory> shopCategoryList = new ArrayList<ShopCategory>();
        List<Area> areaList = new ArrayList<Area>();
        try {
            shopCategoryList = shopCatrgoryService
                    .getShopCategoryList(new ShopCategory());
            areaList = areaService.getAreaList();
        } catch (Exception e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
        }
        modelMap.put("shopCategoryList", shopCategoryList);
        modelMap.put("areaList", areaList);
        modelMap.put("success", true);
        return modelMap;
    }

    @RequestMapping(value = "registershop",method = RequestMethod.POST )
    @ResponseBody
    private Map<String,Object> registerShop(HttpServletRequest request){
        Map<String,Object> modelMap=new HashMap<String,Object>();
        //1.接受并转换相应参数,店铺信息,图片信息
        String shopStr=HttpServletRequestUtil.getString(request,"shopStr");
        ObjectMapper mapper=new ObjectMapper();
        Shop shop=null;
        try {
            shop=mapper.readValue(shopStr, Shop.class);
        }catch (Exception e){
            modelMap.put("success",false);
            modelMap.put("errMsg",e.getMessage());
            return modelMap;
        }
//        文件上传
        CommonsMultipartFile shopImg=null;
        CommonsMultipartResolver commonsMultipartResolver=new CommonsMultipartResolver(
                request.getSession().getServletContext());
        if(commonsMultipartResolver.isMultipart(request)){
            MultipartHttpServletRequest multipartHttpServletRequest= (MultipartHttpServletRequest) request;
            shopImg= (CommonsMultipartFile) multipartHttpServletRequest.getFile("shopImg");
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","上传图片不能为空");
            return modelMap;
        }
        //2.注册店铺
        if(shop!=null && shopImg!=null){
            PersonInfo owner=new PersonInfo();
            owner.setUserId(1L);
            shop.setOwner(owner);
            File shopImgFile=new File(PathUtil.getImgBasePath()+ ImageUtil.getRandomFileName());

            ShopExecution se= null;
            try {
                se = shopService.addShop(shop,shopImg.getInputStream(),shopImg.getOriginalFilename());
                if(se.getState()== ShopStateEnum.CHECK.getState()){
                    modelMap.put("success",true);
                }else {
                    modelMap.put("success",false);
                    modelMap.put("errMsg",se.getStateInfo());
                }
            } catch (IOException e) {
                modelMap.put("success",false);
                modelMap.put("errMsg",se.getStateInfo());
            }
            return modelMap;
        }else{
            modelMap.put("success",false);
            modelMap.put("errMsg","请输入店铺信息");
            return modelMap;
        }

    }

    }
