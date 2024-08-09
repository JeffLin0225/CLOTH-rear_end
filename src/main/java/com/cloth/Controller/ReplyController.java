package com.cloth.Controller;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cloth.Dao.ReplyDTO;
import com.cloth.model.Reply;
import com.cloth.service.ReplyService;

@RestController
@RequestMapping("/backstage/replies")
@CrossOrigin
public class ReplyController {

    @Autowired
    private ReplyService replyService;

    @GetMapping("/all")
    public List<ReplyDTO> getAllReplies() {
        return replyService.getAllReplies();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReplyDTO> getReplyById(@PathVariable Integer id) {
        ReplyDTO replyDTO = replyService.findReplyDtoById(id);
        return ResponseEntity.ok(replyDTO);
    }

    @PostMapping("/add")
    public ResponseEntity<ReplyDTO> createReply(@RequestBody ReplyDTO replyDTO) {
        Reply reply = replyService.convertToEntity(replyDTO);
        reply = replyService.saveReply(reply);
        ReplyDTO replyDTOResponse = replyService.findReplyDtoById(reply.getId()); // 使用 findReplyDtoById 确保在事务范围内
        return ResponseEntity.ok(replyDTOResponse);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ReplyDTO> updateReply(@PathVariable Integer id, @RequestBody ReplyDTO replyDetails) {
        Reply reply = replyService.getReplyById(id);
        if (reply != null) {
            reply.setReplies(replyDetails.getReplies());
            reply.setImg(replyDetails.getImg());
            reply.setUpdated_at(new Date());
            reply = replyService.saveReply(reply);
            ReplyDTO updatedReplyDTO = replyService.findReplyDtoById(reply.getId());
            return ResponseEntity.ok(updatedReplyDTO);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteReply(@PathVariable Integer id) {
        boolean isDeleted = replyService.deleteReply(id);
        if (isDeleted) {
            return ResponseEntity.ok("回復刪除成功");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reply找不到");
        }
    }
}
