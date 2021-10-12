package com.example.demo.Controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Book;
import com.example.demo.mapper.BookMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/book")
public class BookController {

    @Resource
    BookMapper bookMapper;

    //插入
    @PostMapping
    public Result<?> save(@RequestBody Book book){
        bookMapper.insert(book);
        return Result.success();
    }

    //查询
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search){

        LambdaQueryWrapper<Book> wrapper = Wrappers.<Book>lambdaQuery();
        if(StrUtil.isNotBlank(search)){
            wrapper.like(Book::getName, search);
        }

        //通过search查询，分页模糊查询
        return Result.success(bookMapper.selectPage(new Page<>(pageNum, pageSize), wrapper));
    }

    //更新
    @PutMapping
    public Result<?> update(@RequestBody Book book){
        bookMapper.updateById(book);
        return Result.success();
    }

    //删除
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        bookMapper.deleteById(id);
        return Result.success();
    }

}
