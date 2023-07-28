package com.example.onehealthrest.service.impl;
import com.example.onehealthcommon.dto.CommentDto;
import com.example.onehealthcommon.entity.Comment;
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
            return commentDto;
        }
        log.info("add method save() did not work ");
        return null;
    }

    @Override
    public Optional<Comment> findById(int id) {
        return commentRepository.findById(id);
    }

    @Override
    public boolean deleteByIdComment(int id) {
        boolean delete = false;
        Optional<Comment> byId = commentRepository.findById(id);
        if (byId.isPresent()) {
            commentRepository.deleteById(id);
            log.info("Comment with the " + id + " id was deleted");
            delete = true;
        }
        log.info("Comment with the " + id + " id can not be deleted");
        return delete;
    }

    @Override
    public List<CommentDto> getDoctorList(int size, int page) {
        Pageable pageable = PageRequest.of(page, size);
        List<Comment> content = commentRepository.findAll(pageable).getContent();
        return commentMapper.mapListDto(content);
    }
}