package cn.tju.tdwy.daomain;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Map;

public class CarBean {
    private String day;
    private String carNum;
    private String carNumType;
    private String carNumColor;
    private String carColor;
    private String carBrand;
    private ArrayList fields_bind_time;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getCarNum() {
        return carNum;
    }

    public void setCarNum(String carNum) {
        this.carNum = carNum;
    }

    public String getCarNumType() {
        return carNumType;
    }

    public void setCarNumType(String carNumType) {
        this.carNumType = carNumType;
    }

    public String getCarNumColor() {
        return carNumColor;
    }

    public void setCarNumColor(String carNumColor) {
        this.carNumColor = carNumColor;
    }

    public String getCarColor() {
        return carColor;
    }

    public void setCarColor(String carColor) {
        this.carColor = carColor;
    }

    public String getCarBrand() {
        return carBrand;
    }

    public void setCarBrand(String carBrand) {
        this.carBrand = carBrand;
    }

    public ArrayList getFields_bind_time() {
        return fields_bind_time;
    }

    public void setFields_bind_time(ArrayList fields_bind_time) {
        this.fields_bind_time = fields_bind_time;
    }

    @Override
    public String toString() {
        return "CarBean{" +
                "day='" + day + '\'' +
                ", carNum='" + carNum + '\'' +
                ", carNumType='" + carNumType + '\'' +
                ", carNumColor='" + carNumColor + '\'' +
                ", carColor='" + carColor + '\'' +
                ", carBrand='" + carBrand + '\'' +
                ", fields_bind_time='" + fields_bind_time + '\'' +
                '}';
    }

}
