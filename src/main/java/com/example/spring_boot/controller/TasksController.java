package com.example.spring_boot.controller;

import com.example.spring_boot.controller.form.TasksForm;
import com.example.spring_boot.service.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class TasksController {
    @Autowired
    TasksService tasksService;

    /*
     * TOP画面表示処理
     */
    @GetMapping
    //required = falseで@RequestParamで値の入力が何もなくてもエラーにならない
    public ModelAndView top(@RequestParam(name = "start_date", required = false) String startDate,
                            @RequestParam(name = "end_date", required = false) String endDate,
                            @RequestParam(name = "content", required = false) String content,
                            @RequestParam(name = "status", required = false) Integer status) {
        ModelAndView mav = new ModelAndView();
        // タスクを全件取得
        List<TasksForm> contentData = tasksService.findAllTasks(startDate,endDate, content, status);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("content", content);
        mav.addObject("status", status);
        return mav;
    }

//    /*
//     *ステータス変更処理
//     */
//    @PutMapping("/update/status")
//    //@PathVariable　→　URLのパスの一部を変数として受け取るためのアノテーション
//    public ModelAndView updateStatus(@RequestParam(name = "status", required = false) Integer status,@RequestParam String content, @ModelAttribute("formModel") TasksForm tasks) {
//        // UrlParameterのidを更新するentityにセット
//        tasks.setContent(content);
//        tasks.setStatus(status);
//
//        // 編集した投稿を更新
//        tasksService.saveTasks(tasks);
//        // rootへリダイレクト
//        return new ModelAndView("redirect:/");
//    }




}
