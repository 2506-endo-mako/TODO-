package com.example.spring_boot.service;

import com.example.spring_boot.controller.form.TasksForm;
import com.example.spring_boot.repository.TasksRepository;
import com.example.spring_boot.repository.entity.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class TasksService {
    @Autowired
    TasksRepository tasksRepository;

    /*
     * タスク情報取得処理
     */
    public List<TasksForm> findAllTasks(String startDate, String endDate, String content, Integer status) {
        if (!StringUtils.isEmpty(startDate)) {
            startDate += " 00:00:00.000";
        } else {
            startDate = "2020-01-01 00:00:00.000";
        }
        //もしendDateに値があったらその値 + " 23:59:59"をDaoに渡したい
        if (!StringUtils.isEmpty(endDate)) {
            endDate += " 23:59:59.999";
        } else {
            endDate = "2100-12-31 23:59:59.999";
        }

        Date start = null;
        Date end = null;
        try {
            SimpleDateFormat sdFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            start = sdFormat.parse(startDate);
            end = sdFormat.parse(endDate);

        } catch (ParseException e) {
            //例外が発生した場所や原因をより詳細に把握できる
            e.printStackTrace();
            return null;
        }
        //宣言する
        List<Tasks> results = null;
        //分岐させたい
        //②日付とステータス③ステータスのみ④日付と内容⑥内容とステータス⑦日付と内容とステータス
        //日付のみ（初期表示　→　全取得したい）
        if(StringUtils.isEmpty(content) && status == null) {
            results = tasksRepository.findByLimitDateBetweenOrderByLimitDateAsc(start, end);
        }
        //日付とステータス
        if(StringUtils.isEmpty(content) && status != null){
                results = tasksRepository.findByLimitDateBetweenAndStatusOrderByLimitDateAsc(start, end, status);
        }
        //日付と内容
        if(!StringUtils.isEmpty(content) && status == null) {
            results = tasksRepository.findByLimitDateBetweenAndContentOrderByLimitDateAsc(start, end, content);
        }

        //内容と日付とステータス
        if(!StringUtils.isEmpty(content) && status != null) {
            results = tasksRepository.findByLimitDateBetweenAndContentAndStatusOrderByLimitDateAsc(start, end, content, status);
        }

        List<TasksForm> tasks2 = setTasksForm(results);
        return tasks2;
    }
    /*
     * DBから取得したデータをFormに設定
     */
    private List<TasksForm> setTasksForm(List<Tasks> results) {
        List<TasksForm> tasks2 = new ArrayList<>();

        for (int i = 0; i < results.size(); i++) {
            TasksForm tasks = new TasksForm();
            Tasks result = results.get(i);
            tasks.setId(result.getId());
            tasks.setContent(result.getContent());
            tasks.setStatus(result.getStatus());
            tasks.setLimitDate(result.getLimitDate());
            tasks2.add(tasks);
        }
        return tasks2;
    }

    /*
     * レコード編集
     */
    public void editTasks(TasksForm reqTasks) {
        Tasks saveTasks = setTasksEntity(reqTasks);
        tasksRepository.save(saveTasks);
    }
    /*
     * レコード追加
     */
    public void saveTasks(TasksForm reqTasks) {
        Tasks saveTasks = setTasksEntity(reqTasks);
        tasksRepository.save(saveTasks);
    }

    /*
     * ステータス更新
     */
    public void updateTasks(TasksForm reqTasks) {
        //select文流す　WHERE句はkEYのidのみ
            Tasks saveTasks = new Tasks();
            saveTasks = (tasksRepository.findById(reqTasks.getId()).orElse(null));
            saveTasks = updateSetTasksEntity(reqTasks, saveTasks);
            tasksRepository.save(saveTasks);
    }

    /*
     * リクエストから取得した情報をEntityに設定
     */

    private Tasks updateSetTasksEntity(TasksForm reqTasks,Tasks saveTasks) {
        //現在日時を取得
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        //変数endにsdfからformatメソッドで引数dateを渡したものを代入して
        //現在日時をendDateに入れている
        String strDate = sdf.format(date);
        Date nowDate;
        try {
            nowDate = sdf.parse(strDate);
        } catch (ParseException e) {
            //例外が発生した場所や原因をより詳細に把握できる
            e.printStackTrace();
            return null;
        }

        Tasks tasks = new Tasks();
        tasks.setId(saveTasks.getId());
        tasks.setContent(saveTasks.getContent());
        tasks.setStatus(reqTasks.getStatus());
        tasks.setLimitDate(saveTasks.getLimitDate());
        tasks.setCreatedDate(saveTasks.getCreatedDate());
        tasks.setUpdatedDate(nowDate);
        return tasks;


    }

    private Tasks setTasksEntity(TasksForm reqTasks) {
        //現在日時を取得
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

        //変数endにsdfからformatメソッドで引数dateを渡したものを代入して
        //現在日時をendDateに入れている
        String strDate = sdf.format(date);
        Date nowDate;
        try {
            nowDate = sdf.parse(strDate);
        } catch (ParseException e) {
            //例外が発生した場所や原因をより詳細に把握できる
            e.printStackTrace();
            return null;
        }

        Tasks tasks = new Tasks();
        tasks.setId(reqTasks.getId());
        tasks.setContent(reqTasks.getContent());
        tasks.setStatus(reqTasks.getStatus());
        tasks.setLimitDate(reqTasks.getLimitDate());
        tasks.setCreatedDate(reqTasks.getCreatedDate());
        tasks.setUpdatedDate(nowDate);
        return tasks;


    }
    /*
     *投稿の削除
     */
    public void deleteTasks(Integer id) {
        //idを指定してdelete文を実行したい
        tasksRepository.deleteById(id);
    }

    /*
     * 編集する投稿を１件取得
     */
    public TasksForm editTasks(Integer intId) {
        List<Tasks> results = new ArrayList<>();
        results.add((Tasks) tasksRepository.findById(intId).orElse(null)); //nullかもしれない（optional）
        //①resultsから0番目の要素を取り出して新しい変数に入れる
        Tasks result = results.get(0);
        //②取り出した変数がnullかどうかチェックする→③nullだったらnullを返す
        if(result == null){
            return null;
        }
        List<TasksForm> tasks2 = setTasksForm(results);
        return tasks2.get(0);
    }
}
