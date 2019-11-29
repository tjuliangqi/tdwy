package cn.tju.tdwy.utils;

import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RoadMySQL;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class StringToJsonUtils {

    /**
     *
     * @param string example:String string = "[{'record_id': '32d749819fcf438bbb40f7a979a9531b', 'accessTime': '2019-07-01T14:50:55.000', 'roadText': '7215海星街与黄海路交口', 'roadNum': '120116001118', 'roadDirectNum': '01', 'dirNum': '0', 'picURL': '/17.56.42.98/data/disk2/NORMAL/120116050001118/2019/07/01/14/20190701145055287_1.jpg'}]";
     * @param roadMapper
     * @return
     */
    public static ArrayList<Map> stringToJson(String string, RoadMapper roadMapper){
        ArrayList<Map> fields_list = new ArrayList<>();
        try {
            string = string.replace("/","\\\\");
            //字符串转换JSON数组
            JSONArray jsonArray = new JSONArray(string);

            if(jsonArray.length() > 0){
                for (int i = 0;i < jsonArray.length();i++) {
                    //获得json数据
                    Map<String,Object> map = new HashMap<>();
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    //根据key建取值
                    String accessTime = jsonObject.getString("accessTime");
                    String roadText = jsonObject.getString("roadText");
                    String roadNum = jsonObject.getString("roadNum");
                    String roadDirectNum = jsonObject.getString("roadDirectNum");
                    String dirNum = jsonObject.getString("dirNum");
                    String picURL = jsonObject.getString("picURL");
                    //Integer.parseInt
                    picURL = "http://211.81.50.158/img/tdwy_pic/luhu.jpg";
                    RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(roadNum);
                    String lng = roadMySQL.getLng();
                    String lat = roadMySQL.getLat();
                    map.put("accessTime",accessTime);
                    map.put("roadText",roadText);
                    map.put("roadDirectNum",roadDirectNum);
                    map.put("dirNum",dirNum);
                    map.put("lng",lng);
                    map.put("lat",lat);
                    map.put("picURL",picURL);
                    fields_list.add(map);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return fields_list;
    }

    /**
     *
     * @param string example:String string = "{'120116001013': '2019-07-03T14:54:35.000'}";
     * @return
     */
    public static JSONObject stringToJson2(String string) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(string);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
}
