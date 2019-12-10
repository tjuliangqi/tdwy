package cn.tju.tdwy.utils;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RoadMySQL;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.io.IOException;
import java.util.*;

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
                    picURL = picURL.replace("\\","/");
                    String ip = picURL.split("/")[1];
                    Config config = new Config();
                    picURL = "http://211.81.50.158"+picURL.replace(ip+"/data",config.ipMap.get(ip));
                    //picURL = "http://211.81.50.158/img/tdwy_pic/luhu.jpg";
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

    public static ArrayList followAddFields(String carNum, Map map, RoadMapper roadMapper) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder builder0 = QueryBuilders.matchQuery("carNum", carNum);
        searchSourceBuilder.from(0)
                .size(1)
                .query(builder0);
        SearchRequest searchRequest = new SearchRequest(Config.TDWY_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String picURL = "无图片";
        if (searchHits.length < 1){
            System.out.println("查询不到此车");
        }else {
            for (SearchHit searchHit:searchHits){
                //System.out.println(searchHit.getSourceAsString());
                picURL = (String)searchHit.getSourceAsMap().get("picName1");
                picURL = picURL.replace("\\","/").replace("//","/");
                String ip = picURL.split("/")[1];
                Config config = new Config();
                picURL = "http://211.81.50.158"+picURL.replace(ip+"/data",config.ipMap.get(ip));
            }

        }
        Set<String> keys = map.keySet();
        ArrayList fields_bind_time = new ArrayList();
        for (String key:keys){
            Map newMap = new HashMap();
            Object accessTime = map.get(key);
            RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(key);
            String lng = roadMySQL.getLng();
            String lat = roadMySQL.getLat();
            newMap.put("picURL",picURL);
            newMap.put("lng",lng);
            newMap.put("lat",lat);
            //newMap.put("roadNum",key);
            Config config = new Config();
            newMap.put("roadText",config.roadNum.get(key));
            newMap.put("accessTime",accessTime);
            fields_bind_time.add(newMap);
        }
        return fields_bind_time;
    }

//    public static void sortByAccesstime(ArrayList<Map> fields_bind_time){
//        Collections.sort(fields_bind_time , (Map b1, Map b2) -> b2.get("").compareTo(b1.get("")));
//        for (Map each:fields_bind_time){
//            String accessTime = each.get("accessTime");
//        }
//    }

    public static ArrayList<Map<String, String>> sortByAccesstime(ArrayList<Map<String, String>> list) {

        //实现Collections接口进行排序
        Collections.sort(list, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                return o1.get("accessTime").compareTo(o2.get("accessTime"));
            }
        });

        return list;
    }
}
