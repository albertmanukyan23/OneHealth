package com.example.onehealth.repository;

import com.example.onehealth.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByDoctorId(int doctorId);
}