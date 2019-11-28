package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchResponseSections;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;


import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class data {

    public static Map<String,Object> history() throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        Map<String,Object> result = new HashMap<>();
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
        System.out.println(aggregationBuilder);
        ParsedStringTerms parsedStringTerms = searchResponse.getAggregations().get("per_count");
        List<? extends Terms.Bucket> buckets = parsedStringTerms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            result.put(bucket.getKey().toString(),bucket.getDocCount());
        }
        System.out.println(result);
        return result;
    }


    public static Map<String,Object> night() throws IOException {
        Map<String,Object> result = new HashMap<>();
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
        result.put("all",all);
        SearchRequest searchRequest_night = new SearchRequest(Config.NIGHT_OUT_INDEX);
        searchRequest_night.source(searchSourceBuilder);
        try {
            searchResponse = client.search(searchRequest_night, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        long night = searchResponse.getHits().getTotalHits();
        result.put("night",night);
        result.put("day",all-night);
        client.close();
        System.out.println(result);
        return result;
    }

    public static Map<String,Object> city(){
        Map<String,Object> result = new HashMap<>();



        return result;
    }

    public static void main(String[] args) throws IOException {
        night();
    }
}
