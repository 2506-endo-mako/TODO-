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

        List<Tasks> results = tasksRepository.findByLimitDateBetweenAndContentAndStatusOrderByLimitDateAsc(start, end,content,status);
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
}
