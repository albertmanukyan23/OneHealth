package com.example.onehealthcommon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserVerifyDto {

    private String name;
    private String surname;
    private boolean enabled;
}
