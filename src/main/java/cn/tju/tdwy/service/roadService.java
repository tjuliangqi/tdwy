package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RoadMySQL;
import cn.tju.tdwy.daomain.carBase;
import cn.tju.tdwy.utils.EsUtils;
import com.sun.javafx.collections.MappingChange;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;
import static cn.tju.tdwy.utils.JsonToMapUtils.toStringList;
@Service("roadService")
public class roadService {

    public static Map<String, Object> roadSearchfilter(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Map<String, Object> date = new HashMap();
        QueryBuilder queryBuilder = null;
        SearchSourceBuilder searchSourceBuilderDESC = new SearchSourceBuilder();
        SearchSourceBuilder searchSourceBuilderASC = new SearchSourceBuilder();
        searchSourceBuilderDESC.sort("time", SortOrder.DESC);
        searchSourceBuilderASC.sort("time", SortOrder.ASC);
        SearchRequest searchRequestDESC = new SearchRequest(Config.ROADINDEX);
        SearchRequest searchRequestASC = new SearchRequest(Config.ROADINDEX);

        if (type.equals("1")) {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        if (type.equals("0")){
            queryBuilder = QueryBuilders.matchQuery("roadText",value+"*");
        }

        searchSourceBuilderDESC.query(queryBuilder);
        searchRequestDESC.source(searchSourceBuilderDESC);
        searchSourceBuilderASC.query(queryBuilder);
        searchRequestASC.source(searchSourceBuilderASC);
        SearchResponse searchResponseDESC = null;
        SearchResponse searchResponseASC = null;
        try {
            searchResponseDESC = client.search(searchRequestDESC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
        }
        try {
            searchResponseASC = client.search(searchRequestASC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
        }
        client.close();
        SearchHit[] searchHitsDESC = searchResponseDESC.getHits().getHits();
        SearchHit[] searchHitsASC = searchResponseASC.getHits().getHits();
        List<String> list = new ArrayList<>();
        list.add((String) searchHitsASC[0].getSourceAsMap().get("time"));
        list.add((String) searchHitsDESC[0].getSourceAsMap().get("time"));
        date.put("date", list);
        return date;

    }

    public static Map<String, Object> getRoadByFilter(String type, String value, Boolean ifPrepara, String preparaString, RoadMapper roadMapper) throws IOException {
        EsUtils esUtils = new EsUtils();
        Map<String, Object> map = new HashMap();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.ROADINDEX);
        if (type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            if (type.equals("0")){
                boolQueryBuilder.must(QueryBuilders.matchQuery("roadText",value));
            }else {
                return null;
            }
        }

        if (ifPrepara){
            try {
                map = strToMap(preparaString);
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
            for (Object key : map.keySet()){
                if (key.toString().equals("accessTime")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
//                    System.out.println(strings);
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").from(strings[0]).to(strings[1]));
                }

            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        System.out.println(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(searchRequest);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return null;
        }
        client.close();
        Map finalCarMap = new HashMap();
        Map finalIdMap = new HashMap();
        String roadNum = null;
        String roadText = null;
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits){
            Map hitMap = searchHit.getSourceAsMap();

            String carList = String.valueOf(hitMap.get("carList"));
            Map carMap = str2map(carList);

            String idList = String.valueOf(hitMap.get("idList"));
            Map idMap = str2map(idList);
            for1:
            for (Object key : idMap.keySet()){
                if (ifPrepara){
                    String uuid = (String) idMap.get(key);
                    carBase carBase = carBaseService.getRoadByFilter("1",uuid);
                    if (map.get("carNumType")!=null && map.get("carNumType").equals("")){
                        String[] strings = toStringList(map.get(key).toString());
                        for (String str:strings){
                            if (carBase.getCarNumTypeText().equals(str)){
                                idMap.remove(key);
                                carMap.remove(key);
                                continue for1;
                            }
                        }
                    }
                    if (map.get("carNumColor")!=null && map.get("carNumColor").equals("")){
                        String[] strings = toStringList(map.get(key).toString());
                        for (String str:strings){
                            if (carBase.getCarNumColorText().equals(str)){
                                idMap.remove(key);
                                carMap.remove(key);
                                continue for1;
                            }
                        }
                    }
                    if (map.get("carColor")!=null && map.get("carColor").equals("")){
                        String[] strings = toStringList(map.get(key).toString());
                        for (String str:strings){
                            if (carBase.getCarColorText().equals(str)){
                                idMap.remove(key);
                                carMap.remove(key);
                                continue for1;
                            }
                        }
                    }
                    if (map.get("carType")!=null && map.get("carType").equals("")){
                        String[] strings = toStringList(map.get(key).toString());
                        for (String str:strings){
                            if (carBase.getCarTypeText().equals(str)){
                                idMap.remove(key);
                                carMap.remove(key);
                                continue for1;
                            }
                        }
                    }
                }
            }
            finalCarMap.putAll(carMap);
            finalIdMap.putAll(idMap);

            roadNum = String.valueOf(hitMap.get("roadNum"));
            roadText =  String.valueOf(hitMap.get("roadText"));
        }
        RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(roadNum);
        System.out.println(roadNum);
        System.out.println(roadMySQL);
        Map result = new HashMap();
        result.put("roadText",roadText);
        result.put("carList",finalCarMap);
        result.put("idList", finalIdMap);
        result.put("lng", roadMySQL.getLng());
        result.put("lat", roadMySQL.getLat());
        return result;
    }

    public static Map<String,Object> str2map(String idList){
        Map idMapList = new HashMap();
        try {
            Map idMap = strToMap(idList);
            for (Object key : idMap.keySet()){
                String[] strings = toStringList(idMap.get(key).toString());
                for (String str:strings){
                    List<String> list = new ArrayList<>();
                    list.add(str);
                    idMapList.put(key,list);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return idMapList;
    }
}
