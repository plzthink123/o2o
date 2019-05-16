package com.think123.o2o.service;

import com.think123.o2o.BaseTest;
import com.think123.o2o.entity.Area;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class AreaServiceTest extends BaseTest {

    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private AreaService areaService;
    @Test
    public void AreaServiceTest(){
        List<Area> areaList = areaService.getAreaList();
        for(Area area: areaList){
            System.out.println(area);
        }
        assertEquals("西安",areaList.get(1).getAreaName());
    }
}