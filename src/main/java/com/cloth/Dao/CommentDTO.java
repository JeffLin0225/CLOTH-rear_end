package com.cloth.Dao;

import java.util.Date;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CommentDTO {
    private Integer id;
    private Integer productId;
    private String productName;
    private Integer Orderdetail;
    private Integer cartId;
    private Integer cartStatus; 
    private Integer userId;
    private String username;
    private byte[] userImg;
    private Integer star;
    private String comments;
    private byte[] img;
    private Date createdAt;
    private Date updatedAt; 
    
 
    // 新增的字段
    private List<ReplyDTO> replies;
    
}