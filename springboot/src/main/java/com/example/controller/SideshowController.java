package com.example.controller;

import com.example.common.Result;
import com.example.entity.Sideshow;
import com.example.service.SideshowService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 轮播图信息前端请求接口
 */
@RestController
@RequestMapping("/sideshow")
public class SideshowController {

    @Resource
    private SideshowService sideshowService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Sideshow sideshow) {
        sideshowService.add(sideshow);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Sideshow sideshow) {
        sideshowService.updateById(sideshow);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        sideshowService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        sideshowService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Sideshow sideshow = sideshowService.selectById(id);
        return Result.success(sideshow);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Sideshow sideshow) {
        List<Sideshow> list = sideshowService.selectAll(sideshow);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Sideshow sideshow,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Sideshow> pageInfo = sideshowService.selectPage(sideshow, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
