package cn.tju.tdwy.controller;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RetResponse;
import cn.tju.tdwy.daomain.RetResult;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

import static cn.tju.tdwy.utils.NLPUtils.textToMap;

@RestController
@RequestMapping("/nlp")
public class NlpController {
    @Autowired
    RoadMapper roadMapper;
    @RequestMapping(value = "/nlpSearch", method = RequestMethod.POST)
    public RetResult<Object> nlpSearch(@RequestBody Map<String,String> map) throws IOException, JSONException {

        String value = String.valueOf(map.get("value"));

        Map resultMap = textToMap(value, roadMapper);

        return RetResponse.makeOKRsp(resultMap);
    }
}
