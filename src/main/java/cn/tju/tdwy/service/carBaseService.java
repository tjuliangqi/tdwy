package cn.tju.tdwy.service;

import cn.tju.tdwy.Config;
import cn.tju.tdwy.daomain.carBase;
import cn.tju.tdwy.utils.EsUtils;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.Map;

public class carBaseService {
    public static carBase getRoadByFilter(String type, String value) throws IOException {
        EsUtils esUtils = new EsUtils();
        RestHighLevelClient client = esUtils.getConnection();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        SearchRequest searchRequest = new SearchRequest(Config.TDWYINDEX);

        if (type.equals("0")){
            boolQueryBuilder.must(QueryBuilders.matchQuery("carNum",value));
        }else if(type.equals("1")){
            boolQueryBuilder.must(QueryBuilders.matchQuery("id",value));
        }

//        boolQueryBuilder.filter(QueryBuilders.rangeQuery("time").from(strings[0]).to(strings[1]));

        searchSourceBuilder.query(boolQueryBuilder);
        searchRequest.source(searchSourceBuilder);
        searchSourceBuilder.size(1);
        SearchResponse searchResponse = null;
        try {
            searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        } catch (IOException e) {
            e.printStackTrace();
            client.close();
        }
        client.close();
        SearchHit[] searchHits = searchResponse.getHits().getHits();
        SearchHit searchHit = searchHits[0];
        Map hitMap = searchHit.getSourceAsMap();
        carBase carBase = new carBase();
        carBase.setAccessTime(String.valueOf(hitMap.get("accessTime")));
        carBase.setCarBrand(String.valueOf(hitMap.get("carBrand")));
        carBase.setCarColorText(String.valueOf(hitMap.get("carColor")));
        carBase.setCarNumColorText(String.valueOf(hitMap.get("carNumColorText")));
        carBase.setCarNumTypeText(String.valueOf(hitMap.get("carNumTypeText")));
        carBase.setDirNumText(String.valueOf(hitMap.get("dirNum")));
        carBase.setCarTypeText(String.valueOf(hitMap.get("carTypeText")));
        return carBase;
    }
}
