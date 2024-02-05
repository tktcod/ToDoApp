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

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final ScheduleRepository scheduleRepository;


    public CommentResponseDto createComment(Long id, CommentRequestDto requestDto, User user) {

        Schedule schedule = scheduleRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 할 일을 찾을 수 없습니다.")
        );

        Comment comment = commentRepository.save(new Comment(requestDto, user, schedule));
        return new CommentResponseDto(comment);

    }

}
