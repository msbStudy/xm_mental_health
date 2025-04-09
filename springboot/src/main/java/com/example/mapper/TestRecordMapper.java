package com.example.mapper;

import com.example.entity.TestRecord;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TestRecordMapper {

    int insert(TestRecord testRecord);

    void updateById(TestRecord testRecord);

    void deleteById(Integer id);

    @Select("select * from `test_record` where id = #{id}")
    TestRecord selectById(Integer id);

    List<TestRecord> selectAll(TestRecord testRecord);

}
