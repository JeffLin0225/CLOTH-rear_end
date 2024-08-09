package com.cloth.Repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cloth.model.Comment;

public interface CommentRepository extends JpaRepository<Comment ,Integer> {

    List<Comment> findByProductIdBetween(Integer startId, Integer endId);   //抓產品頁面id
    
    Page<Comment> findByProductIdBetween(Integer startId, Integer endId, Pageable pageable);//分頁
}