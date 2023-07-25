package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserPasswordUpdaterDto {

    private String email;
    private String oldPassword;
    private String newPassword;
}
