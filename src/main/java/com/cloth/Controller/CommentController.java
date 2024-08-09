package com.cloth.Controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomNumberEditor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloth.Dao.CommentDTO;
import com.cloth.Dao.OrderStatusDTO;
import com.cloth.Dao.ReplyDTO;
import com.cloth.Repository.OrderdetailRepository;
import com.cloth.model.Comment;
import com.cloth.model.Orderdetail;
import com.cloth.service.CommentService;

@RestController
@RequestMapping("/backstage/comments")
@CrossOrigin
public class CommentController {

    @Autowired
    private CommentService commentService;

    @Autowired
    private OrderdetailRepository orderdetailRepository;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Integer.class, new CustomNumberEditor(Integer.class, true));
    }

    @GetMapping("/all")
    @Transactional
    public List<CommentDTO> getAllComments() {
        return commentService.getAllComments().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    @Transactional
    public ResponseEntity<CommentDTO> getCommentById(@PathVariable Integer id) {
        Comment comment = commentService.findComById(id);
        if (comment != null) {
            // 初始化懶加載集合
            commentService.initializeLazyCollections(comment);
            return ResponseEntity.ok(convertToDTO(comment));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<?> addComment(
            @RequestParam("userId") Integer userId,
            @RequestParam("productId") Integer productId,
            @RequestParam("star") Integer star,
            @RequestParam("comments") String comments,
            @RequestParam(value = "file", required = false) MultipartFile file) throws IOException {

        CommentDTO dto = commentService.createComment(userId, productId, star, comments, file);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/products/{startId}/{endId}")
    @Transactional
    public ResponseEntity<List<CommentDTO>> getCommentsForProductRange(@PathVariable Integer startId,
            @PathVariable Integer endId) {
        List<Comment> comments = commentService.findCommentsByProductIdRange(startId, endId);
        List<CommentDTO> commentDTOs = comments.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(commentDTOs);
    }

    @GetMapping("/products/{startId}/{endId}/page")
    @Transactional(readOnly = true)
    public ResponseEntity<Page<CommentDTO>> getCommentsForProductRangeWithPagination(
            @PathVariable Integer startId,
            @PathVariable Integer endId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "4") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder) {

        // 驗證排序方向
        Sort.Direction direction = Sort.Direction.fromString(sortOrder);

        // 建立分頁和排序物件
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Order.by(sortBy).with(direction)));

        // 查詢並轉換為 DTO
        Page<Comment> commentsPage = commentService.findCommentsByProductIdRange(startId, endId, pageable);
        Page<CommentDTO> commentDTOPage = commentsPage.map(this::convertToDTO);

        return ResponseEntity.ok(commentDTOPage);
    }

    @PutMapping("/update/{id}")
    @Transactional
    public ResponseEntity<CommentDTO> updateComment(@PathVariable Integer id,
            @RequestPart("comment") Comment newComment,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        Comment updatedComment = commentService.updateComment(id, newComment, file);
        if (updatedComment != null) {
            return ResponseEntity.ok(convertToDTO(updatedComment));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    @Transactional
    public ResponseEntity<Void> deleteComment(@PathVariable Integer id) {
        Comment comment = commentService.findComById(id);
        if (comment != null) {
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Transactional
    private CommentDTO convertToDTO(Comment comment) {
        CommentDTO dto = new CommentDTO();
        dto.setId(comment.getId());

        if (comment.getProduct() != null) {
            dto.setProductId(comment.getProduct().getId());
            dto.setProductName(comment.getProduct().getName());
        }

        if (comment.getUsers() != null) {
            dto.setUserId(comment.getUsers().getId());
            dto.setUsername(comment.getUsers().getUsername());
            dto.setUserImg(comment.getUsers().getImg());
        }

        dto.setStar(comment.getStar());
        dto.setComments(comment.getComments());
        dto.setImg(comment.getImg());
        dto.setCreatedAt(comment.getCreated_at());
        dto.setUpdatedAt(comment.getUpdated_at());

        if (comment.getOrderdetail() != null) {
            dto.setOrderdetail(comment.getOrderdetail().getId());

            if (comment.getOrderdetail().getCart() != null) {
                dto.setCartId(comment.getOrderdetail().getCart().getId());
                dto.setCartStatus(comment.getOrderdetail().getCart().getStatus());
            }
        }

        // 获取回复并设置到 DTO 中
        List<ReplyDTO> replyDTOs = commentService.getRepliesForComment(comment.getId()).stream()
                .map(reply -> {
                    ReplyDTO replyDTO = new ReplyDTO();
                    replyDTO.setId(reply.getId());
                    replyDTO.setReplies(reply.getReplies());
                    replyDTO.setCreatedAt(reply.getCreated_at());
                    return replyDTO;
                })
                .collect(Collectors.toList());
        dto.setReplies(replyDTOs);

        return dto;
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<?> getOrderStatusByUserId(@PathVariable Integer userId) {
        List<Orderdetail> orderDetails = orderdetailRepository.findByCartUsersId(userId);

        if (orderDetails.isEmpty()) {
            return ResponseEntity.noContent().build(); // 没有订单
        }

        List<OrderStatusDTO> orderStatuses = orderDetails.stream()
                .map(order -> new OrderStatusDTO(order.getId(), order.getCart().getStatus(),
                        order.getCart().getProduct().getId()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderStatuses);
    }

    @DeleteMapping("/deleteCommentReply/{id}")
    @Transactional
    public ResponseEntity<Void> deleteCommentReply(@PathVariable Integer id) {
        Comment comment = commentService.findComById(id);
        if (comment != null) {
            // 刪除相關回復
            commentService.deleteRepliesByCommentId(id);

            // 刪除評論
            commentService.deleteComment(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}