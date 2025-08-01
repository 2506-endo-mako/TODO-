package com.example.spring_boot.controller;

import com.example.spring_boot.controller.form.TasksForm;
import com.example.spring_boot.service.TasksService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class TasksController {
    @Autowired
    TasksService tasksService;
    @Autowired
    HttpSession session;

    /*
     * TOP画面表示処理
     */
    @GetMapping
    //required = falseで@RequestParamで値の入力が何もなくてもエラーにならない
    public ModelAndView top(@RequestParam(name = "start_date", required = false) String startDate,
                            @RequestParam(name = "end_date", required = false) String endDate,
                            @RequestParam(name = "content", required = false) String content,
                            @RequestParam(name = "status", required = false) Integer status
                            )throws ParseException {
        ModelAndView mav = new ModelAndView();
        // タスクを全件取得
        List<TasksForm> contentData = tasksService.findAllTasks();
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("content", content);
        mav.addObject("status", status);
        mav.addObject("date", LocalDate.now());
        //エラー表示
        setErrorMessage(mav);
        return mav;

    }
    /*
     * 新規タスク登録画面表示
     */
    @GetMapping("/new")
    public ModelAndView newContent() {
        ModelAndView mav = new ModelAndView();
        TasksForm tasksForm = new TasksForm();
        mav.setViewName("/new");
        mav.addObject("formModel", tasksForm);
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * 新規タスク登録処理
     */
    @PostMapping("/add")
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") TasksForm tasksForm, BindingResult result) {
        //バリデーション
        if (result.hasErrors()) {
            session.setAttribute("errorMessages", "タスクを入力してください");
            return new ModelAndView("redirect:/new");
        }
        //初期値として0(未着手)を設定
        tasksForm.setStatus(0);
        tasksService.saveTasks(tasksForm);
        return new ModelAndView("redirect:/");
    }

    /*
     *投稿の編集画面表示
     */
    @GetMapping("/edit/{id}")
    public ModelAndView editContent(@PathVariable Integer id) {
        ModelAndView mav = new ModelAndView();
        //編集する投稿を取得
        TasksForm tasks = tasksService.editTasks(id);
        mav.setViewName("/edit");
        // 編集内容を保管
        mav.addObject("formModel", tasks);
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent (@PathVariable Integer id,
                                       @Validated @ModelAttribute("formModel") TasksForm tasks, BindingResult result) {
        // UrlParameterのidを更新するentityにセット
        tasks.setId(id);
        tasksService.saveTasks(tasks);
        return new ModelAndView("redirect:/");
    }

    /*
     * 投稿削除処理
     */
    @DeleteMapping("/delete/{id}")
    public ModelAndView deleteContent(@PathVariable Integer id) {
        // 投稿をテーブルに格納
        tasksService.deleteTasks(id);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     * バリデーション
     */
    private void setErrorMessage(ModelAndView mav) {
        //errorMessagesがnullでなければビューに渡す
        if(session.getAttribute("errorMessages") != null) {
            mav.addObject("errorMessages", session.getAttribute("errorMessages"));

            //sessionの破棄
            session.invalidate();
        }
    }
}
