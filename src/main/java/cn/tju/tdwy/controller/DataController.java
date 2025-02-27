package cn.tju.tdwy.controller;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RetResponse;
import cn.tju.tdwy.daomain.RetResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static cn.tju.tdwy.service.DataServer.*;

@RestController
@RequestMapping("/data")
public class DataController {

    @Autowired
    RoadMapper roadMapper;
    @RequestMapping(value = "/history", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> dataHistory() {
        Map<String,Object> result = new HashMap<>();
        Map<String,Object> roadNumMap = new HashMap<>();
        Config config = new Config();
        try {
            roadNumMap = history();

        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询总量出错");
        }
        result = formatRoadNum(roadNumMap);
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/night", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> dataNight() {
        Map<String,Object> result = new HashMap<>();
        try {
            result = night();
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询昼伏夜出出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/type", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> dataType() {
        Map<String,Object> result = new HashMap<>();
        try {
            result = type();
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询车辆类型统计出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/city", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> dataCity() {
        Map<String,Object> result = new HashMap<>();
        try {
            result = city();
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询频繁入城出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/onlyNight", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> dataOnlyNight() {
        Map<String,Object> result = new HashMap<>();
        try {
            result = onlyNight();
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询频繁夜出出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/time", method = RequestMethod.POST)
    public RetResult<Map<String,Map<String,Object>>> dataTime() {
        Map<String,Map<String,Object>> result = new HashMap<>();
        Map<String,Map<String,Object>> roadNumMap = new HashMap<>();
        Config config = new Config();
        try {
            roadNumMap = time();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("分时段统计出错");
        }
        for (String each : roadNumMap.keySet()){
            if (config.roadNum.containsKey(each)){
                result.put(config.roadNum.get(each),roadNumMap.get(each));
            }else {
                result.put(each,roadNumMap.get(each));
            }
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/places", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> places() {
        Map<String,Object> result = new HashMap<>();
        try {
            result = place(roadMapper);
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询首页出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/carGraph", method = RequestMethod.POST)
    public RetResult<Set<String>> carGraph(@RequestBody Map<String,String> map) {
        Set<String> result = new HashSet<>();
        String value = map.get("value");
        try {
            result = carMeet(value);
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询车辆图谱出错");
        }
        return RetResponse.makeOKRsp(result);
    }

    @RequestMapping(value = "/roadGraph", method = RequestMethod.POST)
    public RetResult<Set<String>> roadGraph(@RequestBody Map<String,String> map) {
        Set<String> result = new HashSet<>();
        String value = map.get("value");
        try {
            result = roadMeet(value);
        } catch (IOException e) {
            e.printStackTrace();
            return RetResponse.makeErrRsp("查询路口图谱出错");
        }
        return RetResponse.makeOKRsp(result);
    }


}
