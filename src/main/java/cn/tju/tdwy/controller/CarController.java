package cn.tju.tdwy.controller;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RetResponse;
import cn.tju.tdwy.daomain.RetResult;
import cn.tju.tdwy.utils.HashcodeUtils;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static cn.tju.tdwy.service.CarService.carPrepara;
import static cn.tju.tdwy.service.CarService.carSearchList;
import static cn.tju.tdwy.service.CarService.picSearch;

@RestController
@RequestMapping("/transport/car")
public class CarController {
    @Autowired
    RoadMapper roadMapper;
    @RequestMapping(value = "/textSearchList", method = RequestMethod.POST)
    public RetResult<Object> textSearchList(@RequestBody Map<String,String> map) throws IOException, JSONException {
        //RetResult retResult = new RetResult();
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        Boolean ifPrepara = Boolean.valueOf(map.get("ifPrepara"));
        String preparaString = String.valueOf(map.get("preparaString"));

        Object carBeans_FollowBeans = carSearchList(type, value, ifPrepara, preparaString, roadMapper);
        return RetResponse.makeOKRsp(carBeans_FollowBeans);
    }

    @RequestMapping(value = "/prepara", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> prepara(@RequestBody Map<String,String> map) throws IOException, JSONException {
        //RetResult retResult = new RetResult();
        String type = String.valueOf(map.get("type"));
        String value = String.valueOf(map.get("value"));
        List<String> typeList = Arrays.asList("1", "4", "5");
        if (typeList.contains(type)){
            Map<String, Object> selectTags = carPrepara(type, value);

            return RetResponse.makeOKRsp(selectTags);
        } else {
            return RetResponse.makeErrRsp("筛选接口type只有1、4、5");
        }
    }

    @RequestMapping(value = "/pic", method = RequestMethod.POST)
    public RetResult<Map<String,Object>> pic(@RequestParam("file") MultipartFile file) {
        Map<String,Object> resultMap = new HashMap<>();
        try {
            String fileName = System.currentTimeMillis()+file.getOriginalFilename();
            String destFileName="C:\\Users\\user\\Desktop\\tdwy_upload"+File.separator+fileName;

            File destFile = new File(destFileName);
            destFile.getParentFile().mkdirs();
            file.transferTo(destFile);

            //String hashcodeSearchPic = HashcodeUtils.getPictureHashcode(destFileName);
            //System.out.println(hashcodeSearchPic);

            String carNum = picSearch(file.getOriginalFilename());
            resultMap.put("hashcode",carNum);

            return RetResponse.makeOKRsp(resultMap);
        } catch (Exception e){
            return RetResponse.makeErrRsp("获取hashcode出错");
        }

    }
}
