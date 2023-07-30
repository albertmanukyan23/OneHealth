package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.MedServ;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreatCartDto {
    private int id;
    @ManyToOne
    private int userId;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "cart_medical",
            joinColumns = @JoinColumn(name = "cart_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_id"))
    private Set<MedServ> medServSet;
}
