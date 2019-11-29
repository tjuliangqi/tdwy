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
            queryBuilder = QueryBuilders.matchQuery("roadText",value);
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

    public static Map<String, Object> getRoadByFilter(String type, String value, Boolean ifPrepara, String preparaString, RoadMapper roadMapper, int page) throws IOException {
        EsUtils esUtils = new EsUtils();
        Map<String, Object> map = new HashMap();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.TDWYINDEX);
        String roadText = null;
        String roadNum = null;
        if (type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            if (type.equals("0")){
                Map<String,String> stringMap = getRoadText(value);
                roadNum = stringMap.get("roadNum");
                boolQueryBuilder.must(QueryBuilders.matchQuery("roadText",stringMap.get("roadText")));
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
                if (key.toString().equals("time")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
//                    System.out.println(strings);
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("accessTime").from(strings[0]).to(strings[1]));
                }
                if (key.toString().equals("carType")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
                    for (String str:strings){
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("carTypeText",str+"*"));
                    }
                }
                if (key.toString().equals("carColor")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
                    for (String str:strings){
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("carColorText",str+"*"));
                    }
                }
                if (key.toString().equals("carNumColor")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
                    for (String str:strings){
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("carNumColorText",str+"*"));
                    }
                }
                if (key.toString().equals("carNumType")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
                    for (String str:strings){
                        boolQueryBuilder.filter(QueryBuilders.wildcardQuery("carNumTypeText",str+"*"));
                    }
                }
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchSourceBuilder.from(page);
        searchSourceBuilder.size(20);
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

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits){
            Map hitMap = searchHit.getSourceAsMap();
            String carList = String.valueOf(hitMap.get("carNum"));
            String accessTime = String.valueOf(hitMap.get("accessTime"));
            finalCarMap.put(accessTime,carList);
            roadNum = String.valueOf(hitMap.get("roadNum"));
        }
        RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(roadNum);
        Map result = new HashMap();
        result.put("roadText",roadText);
        result.put("carList",finalCarMap);
        result.put("lng", roadMySQL.getLng());
        result.put("lat", roadMySQL.getLat());
        result.put("size", searchResponse.getHits().totalHits);
        return result;
    }

    public static Map<String,String> getRoadText(String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Map<String, String> date = new HashMap();
        QueryBuilder queryBuilder = null;
        SearchSourceBuilder searchSourceBuilderDESC = new SearchSourceBuilder();
        SearchRequest searchRequestDESC = new SearchRequest(Config.ROADINDEX);
        queryBuilder = QueryBuilders.matchQuery("roadText",value);


        searchSourceBuilderDESC.query(queryBuilder);
        searchRequestDESC.source(searchSourceBuilderDESC);
        SearchResponse searchResponseDESC = null;
        try {
            searchResponseDESC = client.search(searchRequestDESC, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
        }
        client.close();
        SearchHit[] searchHitsDESC = searchResponseDESC.getHits().getHits();
        date.put("roadText",(String) searchHitsDESC[0].getSourceAsMap().get("roadText"));
        date.put("roadNum",(String) searchHitsDESC[0].getSourceAsMap().get("roadNum"));
        return date;
    }
}
