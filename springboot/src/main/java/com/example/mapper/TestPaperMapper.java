package com.example.mapper;

import com.example.entity.TestPaper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TestPaperMapper {

    int insert(TestPaper testPaper);

    void updateById(TestPaper testPaper);

    void deleteById(Integer id);

    @Select("select * from `test_paper` where id = #{id}")
    TestPaper selectById(Integer id);

    List<TestPaper> selectAll(TestPaper testPaper);

    @Select("select * from test_paper where status = '审核通过' order by test_num desc limit 8")
    List<TestPaper> selectAllDesc();
}
