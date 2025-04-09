package com.example.mapper;

import com.example.entity.Sideshow;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SideshowMapper {

    int insert(Sideshow sideshow);

    void updateById(Sideshow sideshow);

    void deleteById(Integer id);

    @Select("select * from `sideshow` where id = #{id}")
    Sideshow selectById(Integer id);

    List<Sideshow> selectAll(Sideshow sideshow);

}
