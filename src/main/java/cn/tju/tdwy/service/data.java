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
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;


import java.io.IOException;
import java.util.HashMap;
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
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
        }
        client.close();
        System.out.println(aggregationBuilder);
//        result = searchResponse.getAggregations().asMap();
        System.out.println(searchResponse);
        Aggregations aggregations = searchResponse.getAggregations();
        System.out.println(aggregations);
//        Terms byCompanyAggregation = aggregations.get("by_company");
//        Terms.Bucket elasticBucket = byCompanyAggregation.getBucketByKey("Elastic");
//        Avg averageAge = elasticBucket.getAggregations().get("average_age");
//        double avg = averageAge.getValue();
        return result;
    }


    public static Map<String,Object> night(){
        Map<String,Object> result = new HashMap<>();




        return result;
    }

    public static void main(String[] args) throws IOException {
        history();
    }
}
