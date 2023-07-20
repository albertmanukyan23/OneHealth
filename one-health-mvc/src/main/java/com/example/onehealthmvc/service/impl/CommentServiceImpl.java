package com.example.onehealthmvc.service.impl;

import com.example.onehealthcommon.entity.Comment;
import com.example.onehealthcommon.repository.CommentRepository;
import com.example.onehealthmvc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    @Override
    public List<Comment> findCommentByDoctorId(int doctorId) {
        return commentRepository.findAllByDoctorId(doctorId);
    }

    @Override
    public void save(Comment comment) {
        if (!comment.getOpinion().trim().equals("")) {
            comment.setDate(LocalDateTime.now());
            commentRepository.save(comment);
        }
    }

    @Override
    public void deleteComment(int id) {
        commentRepository.deleteById(id);
    }

    @Override
    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

}
