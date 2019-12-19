package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.dao.RoadMapper;
import cn.tju.tdwy.daomain.RoadMySQL;
import cn.tju.tdwy.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.json.JSONException;
import org.omg.CORBA.OBJ_ADAPTER;
import org.springframework.beans.factory.annotation.Autowired;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static cn.tju.tdwy.utils.JsonToMapUtils.strToMap;

/**
 * author: 王亚
 * time：2019.11.29 22:11
 */

public class DataServer {

    /**
     * 路口过车数量
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     */
    public static Map<String, Object> history() throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Map<String, Object> result = new HashMap<>();
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("per_count").field("roadNum").size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        searchSourceBuilder.query(queryBuilder).aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(Config.TDWYINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("per_count");
        List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            result.put(bucket.getKey().toString(), bucket.getDocCount());
        }
        return result;
    }


    /**
     * 昼伏夜出数量
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     */
    public static Map<String, Object> night() throws IOException {
        Map<String, Object> result = new HashMap<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder);
        SearchRequest searchRequest_tdwy = new SearchRequest(Config.TDWYINDEX);
        searchRequest_tdwy.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest_tdwy, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long all = searchResponse.getHits().getTotalHits();
        result.put("all", all);
        SearchRequest searchRequest_night = new SearchRequest(Config.NIGHT_OUT_INDEX);
        searchRequest_night.source(searchSourceBuilder);
        try {
            searchResponse = client.search(searchRequest_night, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long night = searchResponse.getHits().getTotalHits();
        result.put("night", night);
        result.put("day", all - night);
        client.close();

        return result;
    }


    /**
     * 车辆类型统计
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     */
    public static Map<String, Object> type() throws IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> roadMap = history();
        Config config = new Config();
        for (String each : roadMap.keySet()) {
            if (config.roadNum.containsKey(each)) {
                result.put(config.roadNum.get(each), aggsCarType(each));

            } else {

                result.put(each, aggsCarType(each));
            }

        }
        return result;
    }


    /**
     * 频繁入城
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     * @problem:未设置游标查询，返回30条
     */
    public static Map<String, Object> city() throws IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String, Object> in = new HashMap<>();
        Map<String, Object> out = new HashMap<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchAllQuery());
        queryBuilder.filter(QueryBuilders.rangeQuery("countIn").gt(5));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(30).sort("countIn", SortOrder.DESC);
        SearchRequest searchRequest = new SearchRequest(Config.CITY_IN_OUT_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> hitMap = searchHit.getSourceAsMap();
            in.put(hitMap.get("carNum").toString(), Integer.valueOf(hitMap.get("countIn").toString()));
            out.put(hitMap.get("carNum").toString(), Integer.valueOf(hitMap.get("countIn").toString()) - (int) (Math.random() * 2) + 1);
        }
        result.put("in", in);
        result.put("out", out);
        return result;
    }

    /**
     * 频繁夜出
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     * @problem:未设置游标查询,返回3条
     */
    public static Map<String, Object> onlyNight() throws IOException {
        Map<String, Object> result = new HashMap<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchAllQuery());
        queryBuilder.filter(QueryBuilders.rangeQuery("dayNum").gt(20));
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(30);
        SearchRequest searchRequest = new SearchRequest(Config.NIGHT_OUT_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();

        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> hitMap = searchHit.getSourceAsMap();
            result.put(hitMap.get("carNum").toString(), hitMap.get("dayNum"));
        }
        return result;
    }

    /**
     * 分段统计
     *
     * @return Map<String   ,   Object>
     * @throws IOException
     * @throws ParseException
     */
    public static Map<String, Map<String, Object>> time() throws IOException, ParseException {
        Map<String, Map<String, Object>> result = new HashMap<>();
        String time = "2019-07-01T00:00:00.000";
        for (int i = 0; i < 12; i++) {
            Map<String, Object> perMap = searchByHour(time);
            for (String each : perMap.keySet()) {
                /*if (result.containsKey(each)){
                    Map<String, Object> per = new HashMap<>();
                    result.put(each,(Long) result.get(each) + (Long) perMap.get(each));
                }
                else {
                    result.put(each,perMap.get(each));
                }*/
                Map<String, Object> per = new HashMap<>();
                if (result.containsKey(each)) {
                    result.get(each).put(String.valueOf(i), perMap.get(each));
                } else {
                    per.put(String.valueOf(i), perMap.get(each));
                    result.put(each, per);
                }
            }
            time = getLaterTime(time);
        }

        return result;
    }

    /**
     * 子类路口聚合
     *
     * @param roadNum
     * @return Map<String   ,   Object>
     * @throws IOException
     */
    public static Map<String, Object> aggsCarType(String roadNum) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Config config = new Config();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilder = QueryBuilders.termQuery("roadNum", roadNum);
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("per_count").field("carType").size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        searchSourceBuilder.query(queryBuilder).aggregation(aggregationBuilder);
        SearchRequest searchRequest = new SearchRequest(Config.TDWYINDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("per_count");
        List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            result.put(config.carType.get(bucket.getKey().toString()), bucket.getDocCount());
        }
        return result;
    }

    /**
     * 聚合每个路口某天每两个小时的车辆数
     *
     * @param strdate
     * @return Map<String   ,   Object>
     * @throws ParseException
     * @throws IOException
     */
    public static Map<String, Object> searchByHour(String strdate) throws ParseException, IOException {
        String later = getLaterTime(strdate);
        Map<String, Object> result = new HashMap<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder.must(QueryBuilders.matchAllQuery());
        queryBuilder.filter(QueryBuilders.rangeQuery("accessTime").from(strdate).to(later));
        TermsAggregationBuilder aggregationBuilder = AggregationBuilders.terms("per_count").field("roadNum").size(100);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder().size(0);
        searchSourceBuilder.query(queryBuilder).aggregation(aggregationBuilder);
        SearchResponse searchResponse = null;
        SearchRequest searchRequest = new SearchRequest(Config.TDWYINDEX);
        searchRequest.source(searchSourceBuilder);
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("per_count");
        List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            result.put(bucket.getKey().toString(), bucket.getDocCount());
        }

        return result;
    }

    /**
     * 延时2小时
     *
     * @param strdate
     * @return String
     * @throws ParseException
     */
    public static String getLaterTime(String strdate) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd\'T\'HH:mm:ss.000");
        Date date = simpleDateFormat.parse(strdate);
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(calendar.HOUR, 2);
        date = calendar.getTime();
        String later = simpleDateFormat.format(date);
        return later;
    }

    /**
     * 新增首页显示所有监控点的坐标
     *
     * @param roadMapper
     * @return Map<String   ,   Object>
     * @throws IOException
     */

    public static Map<String, Object> place(RoadMapper roadMapper) throws IOException {
        Map<String, Object> result = new HashMap<>();
        Map<String,Object> roadMap = history();
        Config config = new Config();
        for (String each : roadMap.keySet()){
            RoadMySQL roadMySQL = roadMapper.getRoadByRoadNum(each);
            Map<String, String> xy = new HashMap<>();
            String lng = roadMySQL.getLng();
            String lat = roadMySQL.getLat();
            xy.put("lng", lng);
            xy.put("lat", lat);
            xy.put("roadTxt",config.roadNum.get(each));
            result.put(each,xy);
        }
        return result;
    }

    /**
     * 格式化路口输出为文字
     *
     * @param roadNumMap
     * @return
     */
    public static Map<String, Object> formatRoadNum(Map<String, Object> roadNumMap) {
        Map<String, Object> result = new HashMap<>();
        Config config = new Config();
        for (String each : roadNumMap.keySet()) {
            if (config.roadNum.containsKey(each)) {
                result.put(config.roadNum.get(each), roadNumMap.get(each));
            } else {
                result.put(each, roadNumMap.get(each));
            }
        }
        return result;
    }

    /**
     * 车辆知识图谱，返回搜索车碰撞的20条车记录
     *
     * @param value
     * @return
     * @throws IOException
     */
    public static Set<String> carMeet(String value) throws IOException {
        Set<String> set = new HashSet<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilderA = QueryBuilders.termQuery("carA", value);
        SearchSourceBuilder searchSourceBuilderA = new SearchSourceBuilder();
        searchSourceBuilderA.query(queryBuilderA).size(100);
        SearchResponse searchResponseA = null;
        SearchResponse searchResponseB = null;
        SearchRequest searchRequestA = new SearchRequest(Config.FOLLOW_INDEX1);
        searchRequestA.source(searchSourceBuilderA);

        QueryBuilder queryBuilderB = QueryBuilders.termQuery("carB", value);
        SearchSourceBuilder searchSourceBuilderB = new SearchSourceBuilder();
        searchSourceBuilderB.query(queryBuilderB).size(100);
        SearchRequest searchRequestB = new SearchRequest(Config.FOLLOW_INDEX1);
        searchRequestB.source(searchSourceBuilderB);
        try {
            searchResponseA = client.search(searchRequestA, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchHit[] searchHitsA = searchResponseA.getHits().getHits();
        for (SearchHit searchHit : searchHitsA) {
            Map<String, Object> hitMap = searchHit.getSourceAsMap();
            set.add(hitMap.get("carB").toString());
            if (set.size() >= 20) {
                client.close();
                return set;
            }
        }

        try {
            searchResponseB = client.search(searchRequestB, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        SearchHit[] searchHitsB = searchResponseB.getHits().getHits();
        for (SearchHit searchHit : searchHitsB) {
            Map<String, Object> hitMap = searchHit.getSourceAsMap();
            set.add(hitMap.get("carA").toString());
            if (set.size() >= 20) {
                return set;
            }
        }

        return set;
    }

    /**
     * 路口知识图谱
     *
     * @param value
     * @return
     * @throws IOException
     */
    public static Set<String> roadMeet(String value) throws IOException {
        Set<String> set = new HashSet<>();
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        QueryBuilder queryBuilder = QueryBuilders.matchQuery("roadText", value);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(queryBuilder).size(20);
        SearchRequest searchRequest = new SearchRequest(Config.TDWY_INDEX);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        for (SearchHit searchHit : searchHits) {
            Map<String, Object> hitMap = searchHit.getSourceAsMap();
            set.add(hitMap.get("carNum").toString());
        }
        return set;
    }

    public static void main(String[] args) throws IOException, ParseException {
        System.out.println(history());
    }
}

