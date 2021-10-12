package com.example.demo.Controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.News;
import com.example.demo.mapper.NewsMapper;
import com.example.demo.mapper.NewsMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.websocket.server.PathParam;
import java.util.Date;

@RestController
@RequestMapping("/news")
public class NewsController {
    @Resource
    NewsMapper newsMapper;

    //插入
    @PostMapping
    public Result<?> save(@RequestBody News news){
        news.setTime(new Date());
        newsMapper.insert(news);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id){
        return Result.success(newsMapper.selectById(id));
    }

    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<News> wrapper = Wrappers.<News>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(News::getTitle, search);
        }

        //通过search查询，分页模糊查询
        return Result.success(newsMapper.selectPage(new Page<>(pageNum, pageSize), wrapper));
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody News news){
        newsMapper.updateById(news);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        newsMapper.deleteById(id);
        return Result.success();
    }
}
