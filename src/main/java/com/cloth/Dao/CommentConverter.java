package com.cloth.Dao;

import org.springframework.stereotype.Component;

import com.cloth.model.Comment;

@Component
public class CommentConverter {   //包含转换器类
    public CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());
        dto.setUserId(comment.getUsers().getId());
        dto.setStar(comment.getStar());
        dto.setComments(comment.getComments());
        // Handle image if necessary
        return dto;
    }
}