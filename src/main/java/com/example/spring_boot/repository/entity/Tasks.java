package com.example.spring_boot.repository.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

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


        @Column
        private String content;
        @Column
        private Integer status;
        @Column
        private Date limitDate;
        @Column(name = "created_date", insertable = false, updatable = false)
        private Date createdDate;
        @Column(name = "created_date", insertable = false, updatable = false)
        private Date updatedDate;

}
