package com.example.spring_boot.repository;

import com.example.spring_boot.repository.entity.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {
    public List<Tasks> findByLimitDateBetweenAndContentAndStatusOrderByLimitDateAsc(Date start, Date end, String content, Integer status);
    public List<Tasks> findByLimitDateBetweenAndStatusOrderByLimitDateAsc(Date start, Date end, Integer status);
    public List<Tasks> findByLimitDateBetweenAndContentOrderByLimitDateAsc(Date start, Date end, String content);
    public List<Tasks> findByLimitDateBetweenOrderByLimitDateAsc(Date start, Date end);
}
