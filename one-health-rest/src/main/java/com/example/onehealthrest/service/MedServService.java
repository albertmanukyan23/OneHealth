package com.example.onehealthrest.service;

import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;

import java.util.List;
import java.util.Optional;

public interface MedServService {

    List<MedServ> getPriceList();

    MedServDto save(MedServ medServ);

    boolean delete(int id);

    Optional<MedServ> findById(int id);

    MedServ update(int id, CreateMedServDto requestDto);

}
