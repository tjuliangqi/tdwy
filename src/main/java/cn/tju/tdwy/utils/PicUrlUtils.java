package cn.tju.tdwy.utils;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PicUrlUtils {

    public static String httpURLConectionGET(List<String> picUrlList) {
        for (String eachUrl : picUrlList){
            int responseCode = 404;
            try {
                URL url = new URL(eachUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                //connection.connect();// 连接会话
                //connection.disconnect();// 断开连接
                responseCode = connection.getResponseCode();
            } catch (Exception e) {
                continue;
            }
            if (responseCode == 200){
                return eachUrl;
            }
        }

        return "http://211.81.50.158/tdwy/img98/disk2/NORMAL/120116050001147/2019/07/02/18/no_this_car_pic.jpg";
    }
}
