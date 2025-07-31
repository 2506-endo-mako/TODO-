package com.example.spring_boot.service;

import com.example.spring_boot.controller.form.TasksForm;
import com.example.spring_boot.repository.TasksRepository;
import com.example.spring_boot.repository.entity.Tasks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TasksService {
    @Autowired
    TasksRepository tasksRepository;

    /*
     * タスク情報取得処理
     */
    public List<TasksForm> findAllTasks() {
        List<Tasks> results = tasksRepository.findAllByOrderByLimitDateAsc();
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
