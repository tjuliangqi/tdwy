package cn.tju.tdwy.controller;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RetResponse;
import cn.tju.tdwy.daomain.RetResult;
import cn.tju.tdwy.service.roadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

@RestController

@RequestMapping("/road")
public class RoadController {
    @Autowired
    RoadMapper roadMapper;
    @RequestMapping(value = "/textSearchList", method = RequestMethod.POST)
    public RetResult<Map> getCheckCode(@RequestBody Map<String,Object> json) {
        String type = (String) json.get("type");
        String value = (String) json.get("value");
        Boolean ifPrepara = (Boolean) json.get("ifPrepara");
        String preparaString = (String) json.get("preparaString");
        Map result = null;
        try {
            result = roadService.getRoadByFilter(type, value, ifPrepara, preparaString, roadMapper);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(result);
    }

}
