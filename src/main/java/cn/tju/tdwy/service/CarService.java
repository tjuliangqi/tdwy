package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.CarBean;
import cn.tju.tdwy.daomain.CarFollowBean;
import cn.tju.tdwy.daomain.RoadMySQL;
import cn.tju.tdwy.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;
import static cn.tju.tdwy.utils.StringToJsonUtils.followAddFields;
import static cn.tju.tdwy.utils.StringToJsonUtils.sortByAccesstime;
import static cn.tju.tdwy.utils.StringToJsonUtils.stringToJson;
import static cn.tju.tdwy.utils.ToBuildersUtils.queryTextToBuilder;

public class CarService {
    /**
     *
     * @param type
     * @param value
     * @param ifPrepara
     * @param preparaString
     * @return
     * @throws IOException
     * @throws JSONException
     */
    public static Object carSearchList(String type, String value, boolean ifPrepara, String preparaString, boolean ifDetail, RoadMapper roadMapper) throws IOException, JSONException {
        List<String> typeList1 = Arrays.asList("0", "2");
        List<String> typeList2 = Arrays.asList("1", "4", "5", "nlp4", "nlp5");
        List<String> typeList3 = Arrays.asList("3");
        List<String> typeList4 = Arrays.asList("6", "7", "nlp6");
        Map<String, Integer> sizeMap = new HashMap<>();
        sizeMap.put("false0",30);
        sizeMap.put("false1",30);
        sizeMap.put("false2",30);
        sizeMap.put("false3",100);
        sizeMap.put("false4",30);
        sizeMap.put("false5",30);
        sizeMap.put("false6",30);
        sizeMap.put("false7",1);
        sizeMap.put("true0",32);
        sizeMap.put("true1",30);
        sizeMap.put("true2",32);
        sizeMap.put("true3",100);
        sizeMap.put("true4",30);
        sizeMap.put("true5",30);
        sizeMap.put("true6",30);
        sizeMap.put("true7",1);
        sizeMap.put("nlp4",1);
        sizeMap.put("nlp5",1);
        Map<String,Object> resultMap = new HashMap<>();
        Object result = new Object();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        ArrayList<CarBean> carBeans = new ArrayList<>();
        ArrayList<CarFollowBean> carFollowBeans = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = queryTextToBuilder(type, value, ifPrepara, preparaString, sizeMap);
        if (typeList1.contains(type)){
            SearchRequest searchRequest = new SearchRequest(Config.CAR_INDEX);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits.length < 1){
                System.out.println("查询结果为空，返回空map");
            }else {
                ArrayList<Map<String,String>> fields_bind_time_add = new ArrayList<>();
                CarBean carBean = new CarBean();
                for (SearchHit searchHit:searchHits){
                    //System.out.println(searchHit.getSourceAsString());

                    String day = (String)searchHit.getSourceAsMap().get("day");
                    day = day.replace("T00:00:00","");
                    String carNum = (String)searchHit.getSourceAsMap().get("carNum");
                    String carNumType = (String)searchHit.getSourceAsMap().get("carNumType");
                    String carNumColor = (String)searchHit.getSourceAsMap().get("carNumColor");
                    String carColor = (String)searchHit.getSourceAsMap().get("carColor");
                    String carType = (String)searchHit.getSourceAsMap().get("carBrand");
                    String fields_bind_time_text = (String)searchHit.getSourceAsMap().get("fields_bind_time");

                    // fields_bind_time 封装成json返回
                    ArrayList<Map<String,String>> fields_bind_time = stringToJson(fields_bind_time_text, roadMapper);
                    fields_bind_time_add.addAll(fields_bind_time);
                    //carBean.setDay(day);
                    carBean.setCarNum(carNum);
                    carBean.setCarNumType(carNumType);
                    carBean.setCarNumColor(carNumColor);
                    carBean.setCarColor(carColor);
                    carBean.setCarType(carType);
                    List fields_bind_time_add2 = (List) sortByAccesstime(fields_bind_time_add, ifDetail).get("fields_bind_time");
                    String picURL = (String) sortByAccesstime(fields_bind_time_add, ifDetail).get("picURL");
                    carBean.setPicURL(picURL);
                    carBean.setFields_bind_time(fields_bind_time_add2);
                    //carBeans.add(carBean);
                }
                carBeans.add(carBean);
                resultMap.put("result",carBeans);
            }
            result = carBeans;
        } else if (typeList2.contains(type)){
            SearchRequest searchRequest = new SearchRequest(Config.CAR_INDEX);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits.length < 1){
                System.out.println("查询结果为空，返回空map");
            }else {
                for (SearchHit searchHit:searchHits){
                    //System.out.println(searchHit.getSourceAsString());
                    CarBean carBean = new CarBean();
                    String day = (String)searchHit.getSourceAsMap().get("day");
                    day = day.replace("T00:00:00","");
                    String carNum = (String)searchHit.getSourceAsMap().get("carNum");
                    String carNumType = (String)searchHit.getSourceAsMap().get("carNumType");
                    String carNumColor = (String)searchHit.getSourceAsMap().get("carNumColor");
                    String carColor = (String)searchHit.getSourceAsMap().get("carColor");
                    String carType = (String)searchHit.getSourceAsMap().get("carBrand");
                    String fields_bind_time_text = (String)searchHit.getSourceAsMap().get("fields_bind_time");

                    // fields_bind_time 封装成json返回
                    ArrayList<Map<String,String>> fields_bind_time = stringToJson(fields_bind_time_text, roadMapper);

                    //carBean.setDay(day);
                    carBean.setCarNum(carNum);
                    carBean.setCarNumType(carNumType);
                    carBean.setCarNumColor(carNumColor);
                    carBean.setCarColor(carColor);
                    carBean.setCarType(carType);
                    List fields_bind_time_new = (List) sortByAccesstime(fields_bind_time, ifDetail).get("fields_bind_time");
                    String picURL = (String) sortByAccesstime(fields_bind_time, ifDetail).get("picURL");
                    carBean.setPicURL(picURL);
                    carBean.setFields_bind_time(fields_bind_time_new);
                    carBeans.add(carBean);
                }

                resultMap.put("result",carBeans);
            }
            result = carBeans;
        } else if(typeList3.contains(type)){
            SearchRequest searchRequest = new SearchRequest(Config.TDWY_INDEX);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits.length < 1){
                System.out.println("查询结果为空，返回空map");
            }else {
                CarBean carBean = new CarBean();
                ArrayList<Map<String,String>> fields_bind_time = new ArrayList<>();
                String carNum = "";
                String carNumTypeText = "";
                String carNumColorText = "";
                String carColorText = "";
                String carTypeText = "";
                for (SearchHit searchHit : searchHits) {
                    //System.out.println(searchHit.getSourceAsString());
                    carNum = (String) searchHit.getSourceAsMap().get("carNum");
                    carNumTypeText = (String) searchHit.getSourceAsMap().get("carNumType");
                    carNumColorText = (String) searchHit.getSourceAsMap().get("carNumColorText");
                    carColorText = (String) searchHit.getSourceAsMap().get("carColorText");
                    carTypeText = (String) searchHit.getSourceAsMap().get("carTypeText");
                    String roadText = (String) searchHit.getSourceAsMap().get("roadText");
                    String roadDirectNum = (String) searchHit.getSourceAsMap().get("roadDirectNum");
                    String dirNum = (String) searchHit.getSourceAsMap().get("dirNum");
                    String accessTime = (String) searchHit.getSourceAsMap().get("accessTime");
                    String roadNum = (String) searchHit.getSourceAsMap().get("roadNum");
                    String picURL = (String) searchHit.getSourceAsMap().get("picName1");

                    picURL = picURL.replace("\\","/").replace("//","/");
                    String ip = picURL.split("/")[1];
                    Config config = new Config();
                    picURL = "http://211.81.50.158"+picURL.replace(ip+"/data",config.ipMap.get(ip));
                    //picURL = "http://211.81.50.158/img/tdwy_pic/luhu.jpg";
                    RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(roadNum);
                    String lng = roadMySQL.getLng();
                    String lat = roadMySQL.getLat();
                    // fields_bind_time 封装成json返回
                    Map amap = new HashMap();
                    amap.put("accessTime",accessTime);
                    amap.put("roadText",roadText);
                    amap.put("roadDirectNum",roadDirectNum);
                    amap.put("dirNum",dirNum);
                    amap.put("lng",lng);
                    amap.put("lat",lat);
                    amap.put("picURL",picURL);
                    fields_bind_time.add(amap);
                }
                Config config = new Config();
                carNumTypeText = config.carNumType.get(carNumTypeText);
                carBean.setCarNum(carNum);
                carBean.setCarNumType(carNumTypeText);
                carBean.setCarNumColor(carNumColorText);
                carBean.setCarColor(carColorText);
                carBean.setCarType(carTypeText);
                List fields_bind_time_new = (List) sortByAccesstime(fields_bind_time, ifDetail).get("fields_bind_time");
                String picURL = (String) sortByAccesstime(fields_bind_time, ifDetail).get("picURL");
                carBean.setPicURL(picURL);
                carBean.setFields_bind_time(fields_bind_time_new);
                carBeans.add(carBean);
                resultMap.put("result", carBeans);
            }
            result = carBeans;
        } else {
            SearchRequest searchRequest = new SearchRequest(Config.FOLLOW_INDEX1);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits.length < 1){
                System.out.println("查询结果为空，返回空map");
            }else {
                for (SearchHit searchHit:searchHits){
//            System.out.println(searchHit.getSourceAsString());
                    CarFollowBean carFollowBean = new CarFollowBean();
                    String day = (String)searchHit.getSourceAsMap().get("time");
                    day = day.replace("T00:00:00.000","");
                    String carA = (String)searchHit.getSourceAsMap().get("carA");
                    String carB = (String)searchHit.getSourceAsMap().get("carB");
                    String carAList = (String)searchHit.getSourceAsMap().get("carAList");
                    String carBList = (String)searchHit.getSourceAsMap().get("carBList");
                    String carANumType = (String)searchHit.getSourceAsMap().get("carANumType");
                    String carBNumType = (String)searchHit.getSourceAsMap().get("carBNumType");
                    Config config = new Config();
                    carANumType = config.carNumType.get(carANumType);
                    carBNumType = config.carNumType.get(carBNumType);
                    String carANumColor = (String)searchHit.getSourceAsMap().get("carANumColorText");
                    String carBNumColor = (String)searchHit.getSourceAsMap().get("carBNumColorText");
                    String carAColor = (String)searchHit.getSourceAsMap().get("carAColorText");
                    String carBColor = (String)searchHit.getSourceAsMap().get("carBColorText");
                    String carAType = (String)searchHit.getSourceAsMap().get("carATypeText");
                    String carBType = (String)searchHit.getSourceAsMap().get("carBTypeText");
                    int count = (Integer) searchHit.getSourceAsMap().get("count");

                    // carAList carBList 封装成json返回
                    Map carAJson = strToMap(carAList);
                    ArrayList carA_fields_bind_time = followAddFields(carA, carAJson, roadMapper, ifDetail);
                    List carA_fields_bind_time_sort = (List) sortByAccesstime(carA_fields_bind_time, ifDetail).get("fields_bind_time");
                    String carA_picURL = (String) sortByAccesstime(carA_fields_bind_time, ifDetail).get("picURL");

                    Map carBJson = strToMap(carBList);
                    ArrayList carB_fields_bind_time = followAddFields(carB, carBJson, roadMapper, ifDetail);
                    List carB_fields_bind_time_sort = (List) sortByAccesstime(carB_fields_bind_time, ifDetail).get("fields_bind_time");
                    String carB_picURL = (String) sortByAccesstime(carB_fields_bind_time, ifDetail).get("picURL");

                    carFollowBean.setDay(day);
                    carFollowBean.setCarA(carA);
                    carFollowBean.setCarB(carB);
                    carFollowBean.setCarANumType(carANumType);
                    carFollowBean.setCarBNumType(carBNumType);
                    carFollowBean.setCarANumColor(carANumColor);
                    carFollowBean.setCarBNumColor(carBNumColor);
                    carFollowBean.setCarAColor(carAColor);
                    carFollowBean.setCarBColor(carBColor);
                    carFollowBean.setCarAType(carAType);
                    carFollowBean.setCarBType(carBType);
                    carFollowBean.setCarAPicUrl(carA_picURL);
                    carFollowBean.setCarBPicUrl(carB_picURL);
                    carFollowBean.setCarAList(carA_fields_bind_time_sort);
                    carFollowBean.setCarBList(carB_fields_bind_time_sort);
                    carFollowBean.setCount(count);
                    carFollowBeans.add(carFollowBean);
                }
                resultMap.put("result",carFollowBeans);
            }
            result = carFollowBeans;
        }

        //return resultMap;
        return result;
    }

    /**
     * description 返回筛选类型接口，伴随和碰撞不筛选 0、2、3是因为只返回一条没必要筛选
     * @param type type只有 1、4、5 三种
     * @param value
     * @return
     * @throws IOException
     */
    public static Map<String, Object> carPrepara(String type, String value) throws IOException, JSONException {
        Map<String, Integer> sizeMap = new HashMap<>();
        sizeMap.put("false1",10000);
        sizeMap.put("false4",1000);
        sizeMap.put("false5",1000);
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = queryTextToBuilder(type, value, false, "{}", sizeMap);
        SearchRequest searchRequest;

        searchRequest = new SearchRequest(Config.CAR_INDEX);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();

        ArrayList<String> carNumTypeList = new ArrayList<>();
        ArrayList<String> carNumColorList = new ArrayList<>();
        ArrayList<String> carColorList = new ArrayList<>();
        ArrayList<String> carBrandList = new ArrayList<>();

        Map<String,Object> selectTags = new HashMap<>();
        if (searchHits.length < 2){
            System.out.println("查询条数过少，无法筛选");
        }else {
            for (SearchHit searchHit:searchHits){
                //System.out.println(searchHit.getSourceAsString());
                // find all carNumType
                String carNumType = (String)searchHit.getSourceAsMap().get("carNumType");
                carNumTypeList.add(carNumType);
                // find all carNumColor
                String carNumColor = (String)searchHit.getSourceAsMap().get("carNumColor");
                carNumColorList.add(carNumColor);
                // find all carColor
                String carColor = (String)searchHit.getSourceAsMap().get("carColor");
                carColorList.add(carColor);
                // find all carBrand
                String carBrand = (String)searchHit.getSourceAsMap().get("carBrand");
                carBrandList.add(carBrand);
            }

            Set result1 = new HashSet(carNumTypeList);
            Set result2 = new HashSet(carNumColorList);
            Set result3 = new HashSet(carColorList);
            Set result4 = new HashSet(carBrandList);
            if (result1.size()>10) {
                List <String> list = new ArrayList<String>(result1);
                 result1 = new HashSet(list.subList(0,10));
            }
            if (result2.size()>10) {
                List <String> list = new ArrayList<String>(result2);
                result2 = new HashSet(list.subList(0,10));
            }
            if (result3.size()>10) {
                List <String> list = new ArrayList<String>(result3);
                result3 = new HashSet(list.subList(0,10));
            }
            if (result4.size()>10) {
                List <String> list = new ArrayList<String>(result4);
                result4 = new HashSet(list.subList(0,10));
            }
            selectTags.put("号牌种类",result1);
            selectTags.put("号牌颜色",result2);
            selectTags.put("车身颜色",result3);
            selectTags.put("车辆品牌",result4);
        }
        return selectTags;
    }


    public static String picSearch(String picName) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder builder0 = QueryBuilders.wildcardQuery("picName1", "*"+picName);
        searchSourceBuilder.from(0)
                .size(1)
                .query(builder0);

        SearchRequest searchRequest;

        searchRequest = new SearchRequest(Config.TDWY_INDEX);

        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest);
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        String carNum = "查询不到此图片";
        if (searchHits.length < 1){
            System.out.println("查询不到此图片");
        }else {
            for (SearchHit searchHit:searchHits){
                //System.out.println(searchHit.getSourceAsString());
                carNum = (String)searchHit.getSourceAsMap().get("carNum");
            }

        }
        return carNum;
    }

}
