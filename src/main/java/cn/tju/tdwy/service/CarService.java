package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.CarBean;
import cn.tju.tdwy.daomain.CarFollowBean;
import cn.tju.tdwy.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;

import static cn.tju.tdwy.utils.StringToJsonUtils.stringToJson;
import static cn.tju.tdwy.utils.StringToJsonUtils.stringToJson2;
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
    public static Map<String,Object> carSearchList(String type, String value, boolean ifPrepara, String preparaString, RoadMapper roadMapper) throws IOException, JSONException {
        List<String> typeList1 = Arrays.asList("0", "1", "2", "3", "4", "5");
        List<String> typeList2 = Arrays.asList("6", "7");

        Map<String,Object> resultMap = new HashMap<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        ArrayList<CarBean> carBeans = new ArrayList<>();
        ArrayList<CarFollowBean> carFollowBeans = new ArrayList<>();
        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = queryTextToBuilder(type, value, ifPrepara, preparaString);
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
                    String carBrand = (String)searchHit.getSourceAsMap().get("carBrand");
                    String fields_bind_time_text = (String)searchHit.getSourceAsMap().get("fields_bind_time");

                    // fields_bind_time 封装成json返回
                    ArrayList<Map> fields_bind_time = stringToJson(fields_bind_time_text, roadMapper);

                    carBean.setDay(day);
                    carBean.setCarNum(carNum);
                    carBean.setCarNumType(carNumType);
                    carBean.setCarNumColor(carNumColor);
                    carBean.setCarColor(carColor);
                    carBean.setCarBrand(carBrand);
                    carBean.setFields_bind_time(fields_bind_time);
                    carBeans.add(carBean);
                }

                resultMap.put("result",carBeans);
            }
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
                    String time = (String)searchHit.getSourceAsMap().get("time");
                    time = time.replace("T00:00:00","");
                    String carA = (String)searchHit.getSourceAsMap().get("carA");
                    String carB = (String)searchHit.getSourceAsMap().get("carB");
                    String carAList = (String)searchHit.getSourceAsMap().get("carAList");
                    String carBList = (String)searchHit.getSourceAsMap().get("carBList");
                    int count = (Integer) searchHit.getSourceAsMap().get("count");

                    // carAList carBList 封装成json返回
                    JSONObject carAJson = stringToJson2(carAList);
                    JSONObject carBJson = stringToJson2(carBList);

                    carFollowBean.setTime(time);
                    carFollowBean.setCarA(carA);
                    carFollowBean.setCarB(carB);
                    carFollowBean.setCarAList(carAJson);
                    carFollowBean.setCarBList(carBJson);
                    carFollowBean.setCount(count);
                    carFollowBeans.add(carFollowBean);
                }

                resultMap.put("result",carFollowBeans);

            }
        }

        return resultMap;
    }

    /**
     * description 返回筛选类型接口，伴随和碰撞不筛选
     * @param type type只有 0 ~ 5 七种
     * @param value
     * @return
     * @throws IOException
     */
    public static Map<String, Object> carPrepara(String type, String value) throws IOException, JSONException {
        List<String> typeList1 = Arrays.asList("0", "1", "2", "3", "4", "5");

        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.client;
        SearchSourceBuilder searchSourceBuilder;
        searchSourceBuilder = queryTextToBuilder(type, value, false, "{}");
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
            selectTags.put("carBrand",result4);
        }
        return selectTags;
    }

}
