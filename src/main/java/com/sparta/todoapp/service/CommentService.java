package com.sparta.todoapp.service;

import com.sparta.todoapp.dto.CommentRequestDto;
import com.sparta.todoapp.dto.CommentResponseDto;
import com.sparta.todoapp.dto.ScheduleRequestDto;
import com.sparta.todoapp.entity.Comment;
import com.sparta.todoapp.entity.Schedule;
import com.sparta.todoapp.entity.User;
import com.sparta.todoapp.repository.CommentRepository;
import com.sparta.todoapp.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

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
    public CommentResponseDto updateComment(Long id, ScheduleRequestDto requestDto, User user) {

        Comment comment = commentRepository.findById(id).orElseThrow(
                () -> new NullPointerException("해당 댓글을 찾을 수 없습니다.")
        );

        // 작성자 확인
        try {
            if(!comment.getUser().getId().equals(user.getId())){
                throw new IllegalArgumentException("해당 댓글 작성자가 아닙니다.");
            }
        } catch (IllegalArgumentException ex) {

        }



        comment.update(requestDto);
        return new CommentResponseDto(comment);
    }


    public ResponseEntity<Map<String, String>> deleteComment(Long id, User user) {
        Map<String, String> responseBody = new HashMap<>();
        try {
            Comment comment = commentRepository.findById(id).orElseThrow(
                    () -> new NoSuchElementException("해당 댓글을 찾을 수 없습니다.")
            );

            // 작성자 확인
            if(!comment.getUser().getId().equals(user.getId())){
                throw new IllegalArgumentException("해당 댓글 작성자가 아닙니다.");
            } else {

                commentRepository.delete(comment);
                responseBody.put("message", "댓글이 성공적으로 삭제되었습니다.");
                return new ResponseEntity<>(responseBody, HttpStatus.OK);

            }
        } catch (NoSuchElementException ex) {
            responseBody.put("message", "해당 댓글을 찾을 수 없습니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException ex) {
            responseBody.put("message", "해당 댓글 작성자가 아닙니다.");
            return new ResponseEntity<>(responseBody, HttpStatus.BAD_REQUEST);
        }
    }
}
