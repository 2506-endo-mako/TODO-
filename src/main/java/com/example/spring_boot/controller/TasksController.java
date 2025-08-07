package com.example.spring_boot.controller;

import com.example.spring_boot.controller.form.TasksForm;
import com.example.spring_boot.service.TasksService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    ) throws ParseException {

        ModelAndView mav = new ModelAndView();
        // タスクを全件取得
        List<TasksForm> contentData = tasksService.findAllTasks(startDate, endDate, content, status);
        // 画面遷移先を指定
        mav.setViewName("/top");
        // 投稿データオブジェクトを保管
        mav.addObject("contents", contentData);
        mav.addObject("startDate", startDate);
        mav.addObject("endDate", endDate);
        mav.addObject("content", content);
        mav.addObject("status", status);
        mav.addObject("errorMessages", session.getAttribute("errorMessages"));

//        //バリデーション「期限」には日付以外、statusには数字以外をバリデーションしたい
//        //期限は文字列だから「数字ではない」とすると、あてはまってしまう
//        //ステータスは数字だからmatchesが使えない
//
//        if (!startDate.matches("\\d{4}/\\d{2}/\\d{2}\uFEFF") || !endDate.matches("\\d{4}/\\d{2}/\\d{2}\uFEFF")) {
//            session.setAttribute("errorMessages", " ・不正なパラメータです");
//            return new ModelAndView("redirect:/");
//        }

        //今日の日付をString型に変換
        LocalDate today = LocalDate.now();
        String todayStr = today.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        mav.addObject("today", todayStr);

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
    public ModelAndView addContent(@Validated @ModelAttribute("formModel") TasksForm tasksForm, BindingResult result) throws ParseException {
        //バリデーション
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                // ここでメッセージを取得する。
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/new");
        }

        //初期値として1(未着手)を設定
        tasksForm.setStatus(1);
        tasksService.saveTasks(tasksForm);
        return new ModelAndView("redirect:/");
    }

    /*
     *ステータス変更処理
     */
    @PutMapping("/update/status")
    //@PathVariable　→　URLのパスの一部を変数として受け取るためのアノテーション
    public ModelAndView updateStatus(@RequestParam Integer id,
                                     @RequestParam(name = "status", required = false) Integer status,
                                     @ModelAttribute("formModel") TasksForm tasksForm) {

        tasksForm.setStatus(status);
        tasksForm.setId(id);
        // 編集した投稿を更新
        tasksService.updateTasks(tasksForm);
        // rootへリダイレクト
        return new ModelAndView("redirect:/");
    }

    /*
     *編集画面表示
     */
    @GetMapping(value={"edit/", "edit/{id}"})
    @ResponseBody
    public ModelAndView editContent(@PathVariable (required = false) Optional<String> id) {
        List<String> errorMessages = new ArrayList<>();
        ModelAndView mav = new ModelAndView();
        //バリデーション
        //Optionalは固まりだから、中身のidを取り出す
        //Optional<String> optionalString = Optional.of(String.valueOf(id));
        //Optional<String>型だとmatchesが使えないから、String型の変数に入れた
        String Id = id.orElse(null);

        //Optional型だと、数字ではなくOptional[数字]になり、文字として判断されてしまう→エラーメッセージが表示され、編集画面にいけない
        //Optionalをアンラップしたい
        //String unwrappingId = Id.orElse();


        if (!Id.matches("^[0-9]*$") || Id == null) {
            session.setAttribute("errorMessages", "・不正なパラメータです");
            return new ModelAndView("redirect:/");
        }

        Integer intId = Integer.parseInt(Id);
        //編集する投稿を取得
        TasksForm tasks = tasksService.editTasks(intId);

        if (tasks == null) {
            session.setAttribute("errorMessages", "・不正なパラメータです");
            return new ModelAndView("redirect:/");
        }

        mav.setViewName("/edit");
        // 編集内容を保管
        mav.addObject("formModel", tasks);
        //エラー表示
        setErrorMessage(mav);
        return mav;
    }

    /*
     * 編集処理
     */
    @PutMapping("/update/{id}")
    public ModelAndView updateContent(@PathVariable Integer id,
                                      @Validated @ModelAttribute("formModel") TasksForm tasks, BindingResult result) {
        //バリデーション
        if (result.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (ObjectError error : result.getAllErrors()) {
                // ここでメッセージを取得する。
                errorMessages.add(error.getDefaultMessage());
            }
            session.setAttribute("errorMessages", errorMessages);
            return new ModelAndView("redirect:/edit/{id}");
        }
        // UrlParameterのidを更新するentityにセット
        tasks.setId(id);
        tasksService.editTasks(tasks);
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
        if (session.getAttribute("errorMessages") != null) {
            mav.addObject("errorMessages", session.getAttribute("errorMessages"));

            //sessionの破棄
            session.invalidate();
        }
    }
}
