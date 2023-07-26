package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.UserType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private int id;
    private String name;
    private String surname;
    private String email;
    private Date regisDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date birthDate;
    private String picName;
    private UserType userType;

}
