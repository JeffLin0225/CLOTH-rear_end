package com.cloth.service;

import java.util.List;
import java.util.stream.Collectors;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cloth.Dao.ReplyDTO;
import com.cloth.Repository.AdminRepository;
import com.cloth.Repository.CommentRepository;
import com.cloth.Repository.ReplyRepository;
import com.cloth.model.Comment;
import com.cloth.model.Reply;
import com.cloth.model.Admin;

@Service
public class ReplyService {

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private AdminRepository adminRepository; // 注入 AdminRepository

    @Transactional(readOnly = true)
    public List<ReplyDTO> getAllReplies() {
        List<Reply> replies = replyRepository.findAll();
        return replies.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    
    @Transactional(readOnly = true)
    public ReplyDTO findReplyDtoById(Integer id) {
        Reply reply = getReplyById(id);
        // 确保懒加载属性已初始化
        Hibernate.initialize(reply.getComment());
        if (reply.getAdmin() != null) {
            Hibernate.initialize(reply.getAdmin());
        }
        if (reply.getComment() != null) {
            Hibernate.initialize(reply.getComment().getUsers());
            Hibernate.initialize(reply.getComment().getProduct());
        }
        return convertToDto(reply);
    }

    
    
    
    
    @Transactional(readOnly = true)
    public Reply getReplyById(Integer id) {
        Reply reply = replyRepository.findById(id).orElse(null);
        if (reply != null) {
            Hibernate.initialize(reply.getComment());
            if (reply.getAdmin() != null) {
                Hibernate.initialize(reply.getAdmin());
            }
            if (reply.getComment() != null) {
                Hibernate.initialize(reply.getComment().getUsers());
                Hibernate.initialize(reply.getComment().getProduct());
            }
        }
        return reply;
    }

    @Transactional
    public Reply saveReply(Reply reply) {
        return replyRepository.save(reply);
    }

    @Transactional
    public boolean deleteReply(Integer id) {
        if (replyRepository.existsById(id)) {
            replyRepository.deleteById(id);
            return true;
        } else {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public ReplyDTO convertToDto(Reply reply) {
        Hibernate.initialize(reply.getComment());
        if (reply.getComment() != null) {
            Hibernate.initialize(reply.getComment().getUsers());
            Hibernate.initialize(reply.getComment().getProduct());
        }

        ReplyDTO dto = new ReplyDTO();
        dto.setId(reply.getId());
        dto.setReplies(reply.getReplies());
        dto.setImg(reply.getImg());

        if (reply.getAdmin() != null) {
            dto.setAdminUsername(reply.getAdmin().getUsername());
            dto.setAdminId(reply.getAdmin().getId());
        }

        Comment comment = reply.getComment();
        if (comment != null) {
            dto.setCommentId(comment.getId());
            dto.setCommentComments(comment.getComments());
            dto.setCommentStar(comment.getStar());
            dto.setProductId(comment.getProduct().getId());
            dto.setUserId(comment.getUsers().getId());
            dto.setUserName(comment.getUsers().getUsername());
            dto.setCommentImg(comment.getImg());
        }

        dto.setCreatedAt(reply.getCreated_at());
        dto.setUpdatedAt(reply.getUpdated_at());
        return dto;
    }

    public Reply convertToEntity(ReplyDTO dto) {
        Reply reply = new Reply();
        reply.setId(dto.getId());
        reply.setReplies(dto.getReplies());
        reply.setImg(dto.getImg());

        Comment comment = commentRepository.findById(dto.getCommentId()).orElse(null);
        reply.setComment(comment);

        // 新增以下這段來設置 Admin
        if (dto.getAdminId() != null) {
            Admin admin = adminRepository.findById(dto.getAdminId()).orElse(null);
            reply.setAdmin(admin);
        }

        return reply;
    }
}
