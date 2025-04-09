package com.example.mapper;

import com.example.entity.Propagate;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PropagateMapper {

    int insert(Propagate propagate);

    void updateById(Propagate propagate);

    void deleteById(Integer id);

    @Select("select * from `propagate` where id = #{id}")
    Propagate selectById(Integer id);

    List<Propagate> selectAll(Propagate propagate);

    @Select("select * from propagate order by num desc limit 3")
    List<Propagate> selectTop3();
}
