package com.cloth.Dao;

import java.util.Date;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
public class ReplyDTO {
	
    private Integer id;
    private String replies;
    private byte[] img;
    private Integer adminId;
    private String adminUsername;
    private Integer commentId;
    private String commentComments;
    private Integer commentStar;
    private byte[] commentImg; 
    private Integer productId;
    private Integer userId;
    private String userName;
    private Date createdAt;
    private Date updatedAt;
	

}
