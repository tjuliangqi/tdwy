package cn.tju.tdwy.utils;


import cn.tju.tdwy.Config;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;


public class EsUtils {
    public RestHighLevelClient client = null;

    public EsUtils(){
        try{
            RestHighLevelClient client = new RestHighLevelClient(RestClient.builder(
                    new HttpHost(Config.ES_IP, Config.PORT, "http")).setMaxRetryTimeoutMillis(5*60*1000));
            this.client = client;
        }catch (Exception ex) {
            ex.printStackTrace();
        }

    }
    public RestHighLevelClient getConnection(){
        return  client;
    }

}


