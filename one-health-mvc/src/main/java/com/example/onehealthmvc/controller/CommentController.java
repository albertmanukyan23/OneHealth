package com.example.onehealthmvc.controller;
import com.example.onehealthcommon.entity.Comment;
import com.example.onehealthmvc.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
public class CommentController {

    private final CommentService commentService;
    @PostMapping("/create")
    public String createComment(@ModelAttribute Comment comment) {
        commentService.save(comment);
        return "redirect:/doctor/details/" + comment.getDoctor().getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteComment(@PathVariable("id") int id) {
        Optional<Comment> comment = commentService.findById(id);
         commentService.deleteComment(id);
        return "redirect:/doctor/details/" + comment.get().getDoctor().getId();
    }
}