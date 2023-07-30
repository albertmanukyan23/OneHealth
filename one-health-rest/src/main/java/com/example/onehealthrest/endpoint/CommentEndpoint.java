package com.example.onehealthrest.endpoint;
import com.example.onehealthcommon.dto.CommentDto;
import com.example.onehealthcommon.dto.CreatCommentDto;
import com.example.onehealthcommon.mapper.CommentMapper;
import com.example.onehealthrest.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/comment")
@Slf4j
public class CommentEndpoint {
    private final CommentService commentService;
    private final CommentMapper commentMapper;
    @PostMapping
    public ResponseEntity <CommentDto> addComment(@RequestBody CreatCommentDto dto) {
        return ResponseEntity.ok(commentService.save(commentMapper.mapDto(dto)));
    }
    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteComment(@RequestParam ("deleteId") int deleteId ){
        boolean delete = commentService.deleteByIdComment(deleteId);
        return ResponseEntity.ok(delete);

    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getCommentList(@RequestParam(defaultValue = "5") int size,
                                                                 @RequestParam(defaultValue = "1") int page) {
        return ResponseEntity.ok(commentService.getDoctorList(size, page - 1));
    }
}
