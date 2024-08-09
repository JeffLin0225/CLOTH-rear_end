package com.cloth.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.Dao.CommentConverter;
import com.cloth.Dao.CommentDTO;
import com.cloth.Repository.CommentRepository;
import com.cloth.Repository.ProductRepository;
import com.cloth.Repository.ReplyRepository;
import com.cloth.Repository.UsersRepository;

import com.cloth.model.Comment;
import com.cloth.model.Product;
import com.cloth.model.Reply;
import com.cloth.model.Users;

import io.jsonwebtoken.io.IOException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepo;
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UsersRepository usersRepository;
    
    @Autowired
    private CommentConverter commentConverter;
    
    @Autowired
    private  ReplyRepository replyRepository;
    
    

    public List<Comment> getAllComments() {
        return commentRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentsByProductIdRange(Integer startId, Integer endId) {
        return commentRepo.findByProductIdBetween(startId, endId);
    }

    @Transactional(readOnly = true)
    public Page<Comment> findCommentsByProductIdRange(Integer startId, Integer endId, Pageable pageable) {
        return commentRepo.findByProductIdBetween(startId, endId, pageable);
    }


    @Transactional(readOnly = true)
    public Comment findComById(Integer id) {
        Optional<Comment> optional = commentRepo.findById(id);
        if (optional.isPresent()) {
            Comment comment = optional.get();
            initializeLazyCollections(comment);
            return comment;
        } else {
            return null;
        }
    }

    public Comment insertOrUpdateMsg(Comment comment, MultipartFile file) throws java.io.IOException {
        if (file != null && !file.isEmpty()) {
            try {
                comment.setImg(file.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return commentRepo.save(comment);
    }

    @Transactional
    public CommentDTO createComment(Integer userId, Integer productId, Integer star, String comments, MultipartFile file) throws IOException {
        Users user = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with id " + productId));

        Comment comment = new Comment();
        comment.setProduct(product);
        comment.setUsers(user);
        comment.setStar(star);
        comment.setComments(comments);

        if (file != null && !file.isEmpty()) {
            try {
                try {
					comment.setImg(file.getBytes());
				} catch (java.io.IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            } catch (IOException e) {
                throw new IOException("Failed to upload file", e);
            }
        }

        // Manually set timestamps
        Date now = new Date();
        comment.setCreated_at(now);
        comment.setUpdated_at(now);
        
        Comment savedComment = commentRepo.save(comment);
        return commentConverter.convertToDTO(savedComment);
    }

    @Transactional
    public Comment updateComment(Integer id, Comment newComment, MultipartFile file) {
        Optional<Comment> optional = commentRepo.findById(id);

        if (optional.isPresent()) {
            Comment existingComment = optional.get();

            if (newComment.getStar() != null) {
                existingComment.setStar(newComment.getStar());
            }
            if (newComment.getComments() != null) {
                existingComment.setComments(newComment.getComments());
            }
            if (file != null && !file.isEmpty()) {
                try {
                    existingComment.setImg(file.getBytes());
                } catch (java.io.IOException e) {
                    e.printStackTrace();
                }
            }

            existingComment.setUpdated_at(new Date());
            return commentRepo.save(existingComment);
        } else {
            return null;
        }
    }

    @Transactional
    public void deleteComment(Integer id) {
        commentRepo.deleteById(id);
    }

    @Transactional
    public void initializeLazyCollections(Comment comment) {
        Hibernate.initialize(comment.getProduct());
        Hibernate.initialize(comment.getUsers());
        if (comment.getOrderdetail() != null) {
            Hibernate.initialize(comment.getOrderdetail().getCart());
        }
    }
    
  
    @Transactional
    public void deleteRepliesByCommentId(Integer commentId) {//--
        replyRepository.deleteByCommentId(commentId);
    }

    
    
    public List<Reply> getRepliesForComment(Integer commentId) {
        // 这个方法的实现取决于你如何存储和访问回复信息
        // 例如，如果你有一个 ReplyRepository，你可以使用它来查找回复
        return replyRepository.findByCommentId(commentId);
    
    }
}