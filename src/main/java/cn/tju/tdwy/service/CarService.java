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
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.json.JSONException;

import java.io.IOException;
import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;
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
    public static Object carSearchList(String type, String value, boolean ifPrepara, String preparaString, RoadMapper roadMapper) throws IOException, JSONException {
        List<String> typeList1 = Arrays.asList("0", "1", "2", "4", "5");
        List<String> typeList2 = Arrays.asList("3");
        List<String> typeList3 = Arrays.asList("6", "7");
        Map<String, Integer> sizeMap = new HashMap<>();
        sizeMap.put("0",1);
        sizeMap.put("1",10);
        sizeMap.put("2",1);
        sizeMap.put("3",100);
        sizeMap.put("4",10);
        sizeMap.put("5",10);
        sizeMap.put("6",10);
        sizeMap.put("7",1);
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
                    ArrayList<Map> fields_bind_time = stringToJson(fields_bind_time_text, roadMapper);

                    carBean.setDay(day);
                    carBean.setCarNum(carNum);
                    carBean.setCarNumType(carNumType);
                    carBean.setCarNumColor(carNumColor);
                    carBean.setCarColor(carColor);
                    carBean.setCarType(carType);
                    carBean.setFields_bind_time(fields_bind_time);
                    carBeans.add(carBean);
                }

                resultMap.put("result",carBeans);
            }
            result = carBeans;
        } else if(typeList2.contains(type)){
            SearchRequest searchRequest = new SearchRequest(Config.TDWY_INDEX);
            searchRequest.source(searchSourceBuilder);
            SearchResponse searchResponse = client.search(searchRequest);
            SearchHit[] searchHits = searchResponse.getHits().getHits();

            if (searchHits.length < 1){
                System.out.println("查询结果为空，返回空map");
            }else {
                CarBean carBean = new CarBean();
                ArrayList<Map> fields_bind_time = new ArrayList<>();
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
                    picURL = "http://211.81.50.158/img/tdwy_pic/luhu.jpg";
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
                carBean.setFields_bind_time(fields_bind_time);
                carBeans.add(carBean);
                resultMap.put("result", carBeans);
            }
            result = carBeans;
        } else {
            SearchRequest searchRequest = new SearchRequest(Config.FOLLOW_INDEX);
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
                    Map carBJson = strToMap(carBList);

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
                    carFollowBean.setCarAList(carAJson);
                    carFollowBean.setCarBList(carBJson);
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
        sizeMap.put("1",10000);
        sizeMap.put("4",1000);
        sizeMap.put("5",1000);
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
            selectTags.put("carNumType",result1);
            selectTags.put("carNumColor",result2);
            selectTags.put("carColor",result3);
            selectTags.put("carType",result4);
        }
        return selectTags;
    }

}
