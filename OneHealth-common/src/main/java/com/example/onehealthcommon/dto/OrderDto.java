package com.example.onehealthcommon.dto;

import com.example.onehealthcommon.entity.MedServ;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDto {
    private int id;
    @ManyToOne
    private int userId;
    private Date dateTime;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "order_medical",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "medical_id"))
    private Set<MedServ> medServSet;
}