package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatCommentDto {
    private int id;
    private int userId;
    private int doctorId;
    private String opinion;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime date;
}
