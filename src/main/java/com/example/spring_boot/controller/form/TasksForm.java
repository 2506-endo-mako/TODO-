package com.example.spring_boot.controller.form;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Timestamp;
import java.util.Date;

@Getter
@Setter
public class TasksForm {

    private int id;

    @NotBlank(message = "タスクを入力してください")
    @Pattern(regexp = "[^\\u3000]*", message = "タスクを入力してください")
    @Size(max = 140, message = "タスクは140文字以内で入力してください")
    private String content;

    private int status;

    @NotNull(message = "期限を設定してください")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @FutureOrPresent(message = "無効な日付です")
    private Date limitDate;

    private Date createdDate;

    private Date updatedDate;

}
