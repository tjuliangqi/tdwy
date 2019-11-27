package cn.tju.tdwy.dao;


import cn.tju.tdwy.daomain.RoadMySQL;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface RoadMapper {

    @Select("SELECT * FROM `road` WHERE `roadNum` = #{roadNum}")
    RoadMySQL getRoadByRoadNum(String roadNum);
    @Insert({"INSERT INTO `tdwy`.`road`(`roadNum`, `roadText`, " +
            "`lng`, `lat`) VALUES (#{roadNum},#{roadText},#{lng},#{lat})"})
    int insertData(RoadMySQL roadMySQL);
}
