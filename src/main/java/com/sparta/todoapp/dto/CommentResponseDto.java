package com.sparta.todoapp.dto;

import com.sparta.todoapp.entity.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentResponseDto {
    private Long scheduleId;
    private Long commentId;
    private String contents;

    public CommentResponseDto(Comment comment) {
        this.scheduleId = comment.getSchedule().getId();
        this.commentId = comment.getId();
        this.contents = comment.getContents();
    }
}
