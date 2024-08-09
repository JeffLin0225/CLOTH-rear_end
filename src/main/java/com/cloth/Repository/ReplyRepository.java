package com.cloth.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cloth.model.Reply;

public interface ReplyRepository extends JpaRepository<Reply,Integer> {

    @Query("SELECT r FROM Reply r LEFT JOIN FETCH r.admin LEFT JOIN FETCH r.comment WHERE r.id = :id")
    Reply findByIdWithAdminAndComment(@Param("id") Integer id);

    
    void deleteByCommentId(Integer commentId);  //--


    List<Reply> findByCommentId(Integer commentId);
    
    
    
}
