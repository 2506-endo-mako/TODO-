package com.example.spring_boot.controller.form;

import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class TasksForm {

    private int id;
    private String content;
    private int status;
    private Date limitDate;
    private Date createdDate;
    private Date updatedDate;

}
