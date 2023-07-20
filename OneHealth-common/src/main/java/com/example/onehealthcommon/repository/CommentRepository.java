package com.example.onehealthcommon.repository;

import com.example.onehealthcommon.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByDoctorId(int doctorId);
}