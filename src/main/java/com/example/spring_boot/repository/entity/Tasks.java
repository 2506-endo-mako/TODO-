package com.example.spring_boot.repository.entity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.util.Date;

@Entity
@Table(name = "tasks")
public class Tasks {

        @Id
        @Column
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        @Column
        private String content;
        @Column
        private int status;
        @Column
        private Date limitDate;
        @Column
        private Date createdDate;
        @Column
        private Date updatedDate;

}
