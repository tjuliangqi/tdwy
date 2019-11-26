package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.utils.EsUtils;
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

import java.io.IOException;
import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;
import static cn.tju.tdwy.utils.JsonToMapUtils.toStringList;

public class roadService {
    public static Map<String, Object> roadSearchfilter(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Map<String, Object> date = new HashMap();
        QueryBuilder queryBuilder = null;
        SearchSourceBuilder searchSourceBuilderDESC = new SearchSourceBuilder();
        SearchSourceBuilder searchSourceBuilderASC = new SearchSourceBuilder();
        searchSourceBuilderDESC.sort("date", SortOrder.DESC);
        searchSourceBuilderASC.sort("date", SortOrder.ASC);
        SearchRequest searchRequestDESC = new SearchRequest(Config.ROADINDEX);
        SearchRequest searchRequestASC = new SearchRequest(Config.ROADINDEX);

        if (type.equals("1")) {
            queryBuilder = QueryBuilders.matchAllQuery();
        }
        if (type.equals("0")){
            queryBuilder = QueryBuilders.wildcardQuery("RoadText","*"+value+"*");
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
        list.add((String) searchHitsASC[0].getSourceAsMap().get("date"));
        list.add((String) searchHitsDESC[0].getSourceAsMap().get("date"));
        date.put("date", list);
        return date;

    }

    public static Map<String, Object> getRoadByFilter(String type, String value, Boolean ifPrepara, String preparaString) throws IOException {
        List<String> list = new ArrayList();
        EsUtils esUtils = new EsUtils();
        Map<String, Object> map = new HashMap();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.CONFERENCEINDEX);
        if (type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchAllQuery());
        }else {
            if (type.equals("0")){
                boolQueryBuilder.must(QueryBuilders.wildcardQuery("labels","*"+value+"*"));
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
                if (key.toString().equals("pubdate")){
                    if (map.get(key).toString().equals("")){
                        continue;
                    }
                    String[] strings = toStringList(map.get(key).toString());
//                    System.out.println(strings);
                    boolQueryBuilder.filter(QueryBuilders.rangeQuery("date").from(strings[0]).to(strings[1]));
                }
            }
        }
        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
            return null;
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits){
            Map hitMap = searchHit.getSourceAsMap();
            map.put("carBrand", Integer.valueOf(String.valueOf(hitMap.get("id"))));
            map.put("carColor", String.valueOf(hitMap.get("level")));
            map.put("carNumColor", String.valueOf(hitMap.get("name")));
            map.put("carNumType", String.valueOf(hitMap.get("location")));
            map.put("carNumList", String.valueOf(hitMap.get("homepage")));
            map.put("dirNum", String.valueOf(hitMap.get("date")));
            map.put("roadDirectNum", String.valueOf(hitMap.get("labels")));
            map.put("roadText", String.valueOf(hitMap.get("homepage")));
            map.put("axisList", String.valueOf(hitMap.get("date")));
        }
        return map;
    }
}
