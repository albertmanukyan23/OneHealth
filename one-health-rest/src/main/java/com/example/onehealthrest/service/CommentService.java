package com.example.onehealthrest.service;


import com.example.onehealthcommon.dto.CommentDto;
import com.example.onehealthcommon.entity.Comment;

import java.util.List;

public interface CommentService {
    CommentDto save(Comment comment);

    boolean deleteByIdComment(int id);

    List<CommentDto> getDoctorList(int page, int size);

}
