package com.jackson.service;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jackson.mapper.ChapterMapper;
import com.jackson.model.Chapter;
import com.jackson.result.Results;
import org.pegdown.PegDownProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ChapterService extends ServiceImpl<ChapterMapper, Chapter> {

    @Autowired
    private ChapterMapper chapterMapper;

    /**
     * 获取实验内容
     */
    public Results getText(Integer chapterId){

        String text = chapterMapper.getText(chapterId);

        PegDownProcessor pdp = new PegDownProcessor(Integer.MAX_VALUE);

        return Results.success(pdp.markdownToHtml(text));
    }
}
