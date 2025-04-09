package com.example.controller;

import com.example.common.Result;
import com.example.entity.Propagate;
import com.example.service.PropagateService;
import com.github.pagehelper.PageInfo;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 健康宣传信息前端请求接口
 */
@RestController
@RequestMapping("/propagate")
public class PropagateController {

    @Resource
    private PropagateService propagateService;

    /**
     * 新增
     */
    @PostMapping("/add")
    public Result add(@RequestBody Propagate propagate) {
        propagateService.add(propagate);
        return Result.success();
    }

    /**
     * 修改
     */
    @PutMapping("/update")
    public Result update(@RequestBody Propagate propagate) {
        propagateService.updateById(propagate);
        return Result.success();
    }

    /**
     * 单个删除
     */
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        propagateService.deleteById(id);
        return Result.success();
    }

    /**
     * 批量删除
     */
    @DeleteMapping("/delete/batch")
    public Result delete(@RequestBody List<Integer> ids) {
        propagateService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 单个查询
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        Propagate propagate = propagateService.selectById(id);
        return Result.success(propagate);
    }

    /**
     * 查询所有
     */
    @GetMapping("/selectAll")
    public Result selectAll(Propagate propagate) {
        List<Propagate> list = propagateService.selectAll(propagate);
        return Result.success(list);
    }

    /**
     * 分页查询
     */
    @GetMapping("/selectPage")
    public Result selectPage(Propagate propagate,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Propagate> pageInfo = propagateService.selectPage(propagate, pageNum, pageSize);
        return Result.success(pageInfo);
    }

    /**
     * 查询浏览量最高的三个宣传帖子
     */
    @GetMapping("/selectTop3")
    public Result selectTop3() {
        List<Propagate> list = propagateService.selectTop3();
        return Result.success(list);
    }

    /**
     * 前台分页查询
     */
    @GetMapping("/selectPageFront")
    public Result selectPageFront(Propagate propagate,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Propagate> pageInfo = propagateService.selectPageFront(propagate, pageNum, pageSize);
        return Result.success(pageInfo);
    }

}
