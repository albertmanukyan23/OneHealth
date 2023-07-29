package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CommentDto;
import com.example.onehealthcommon.entity.Comment;
import com.example.onehealthcommon.exception.EntityNotFoundException;
import com.example.onehealthcommon.mapper.CommentMapper;
import com.example.onehealthcommon.repository.CommentRepository;
import com.example.onehealthrest.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;

    @Override
    public CommentDto save(Comment comment) {
        if (!comment.getOpinion().trim().equals("")) {
            comment.setDate(LocalDateTime.now());
            commentRepository.save(comment);
            CommentDto commentDto = commentMapper.map(comment);
            log.info("add method save() did not work ");
            return commentDto;
        }
        return null;
    }

    @Override
    public boolean deleteByIdComment(int id) {
        boolean delete = false;
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()) {
            try {
                throw new EntityNotFoundException("DeleteById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            commentRepository.deleteById(id);
            log.info("Comment with the " + id + " id was deleted");
            return delete = true;
        }
    }

    @Override
    public List<CommentDto> getDoctorList(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> content = commentRepository.findAll(pageable).getContent();
        if (content.isEmpty()) {
            try {
                throw new EntityNotFoundException("Is Empty " + content);
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            return commentMapper.mapListDto(content);

        }
    }
}