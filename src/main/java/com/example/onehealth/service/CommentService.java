package com.example.onehealth.service;

import com.example.onehealth.entity.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {

    List<Comment> findCommentByDoctorId(int doctorId);

    void save(Comment comment);
    void deleteComment(int id);

    Optional<Comment> findById(int id);
}
