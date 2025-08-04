package com.example.spring_boot.repository.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Entity
@Table(name = "tasks")
@Getter
@Setter
public class Tasks {

        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;


        @Column(name = "content",insertable = true, updatable = true)
        private String content;

        @Column
        private Integer status;

        @Column(name = "limit_date",insertable = true, updatable = true)
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private Date limitDate;

        @Column(name = "created_date",insertable = false, updatable = false)
        private Date createdDate;

        @Column(name = "updated_date",insertable = false, updatable = true)
        private Date updatedDate;

}
