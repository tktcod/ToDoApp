package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.dto.ScheduleResponseDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CommentRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;


    public CommentResponseDto createComment(Long scheduleId, CommentRequestDto requestDto, User user) {

        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new NullPointerException("해당 할 일을 찾을 수 없습니다.")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, schedule));
        return new CommentResponseDto(comment);

    }

    @Transactional
    public CommentResponseDto updateSchedule(Long id, ScheduleRequestDto requestDto, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 댓글을 찾을 수 없습니다.")
        );

        // 작성자 확인
        if(!comment.getUser().getId().equals(user.getId())){
            throw new IllegalArgumentException("해당 댓글 작성자가 아닙니다.");
        }
        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }

}
