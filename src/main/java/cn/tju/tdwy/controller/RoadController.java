package cn.tju.tdwy.controller;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RetResponse;
import cn.tju.tdwy.daomain.RetResult;
import cn.tju.tdwy.service.DataServer;
import cn.tju.tdwy.service.roadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@RestController

@RequestMapping("/road")
public class RoadController {
    @Autowired
    RoadMapper roadMapper;
    @RequestMapping(value = "/textSearchList", method = RequestMethod.POST)
    public RetResult<Map> searchList(@RequestBody Map<String,Object> json) {
        String type = (String) json.get("type");
        String value = (String) json.get("value");
        Boolean ifPrepara = (Boolean) json.get("ifPrepara");
        String preparaString = (String) json.get("preparaString");
        Integer page = (Integer) json.get("page");
        Map result = null;
        try {
            if (type.equals("4")){
                result = DataServer.history();
            }else if (type.equals("3")){
                result = roadService.getRoadByFilter("1", value, ifPrepara, preparaString, roadMapper, page);
            }else {
                result = roadService.getRoadByFilter(type, value, ifPrepara, preparaString, roadMapper, page);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/getPrepara", method = RequestMethod.POST)
    public RetResult<Map> getprepara(@RequestBody Map<String,Object> json) {
        String type = (String) json.get("type");
        String value = (String) json.get("value");
        Map result = null;
        try {
            if (type.equals("4")){
                result = null;
            }else if (type.equals("3")){
                result = roadService.roadSearchfilter("1", value);
            }else {
                result = roadService.roadSearchfilter(type, value);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return RetResponse.makeOKRsp(result);
    }
}
