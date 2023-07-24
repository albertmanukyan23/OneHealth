package com.example.onehealthrest.service.impl;


import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.mapper.MedServMapper;
import com.example.onehealthcommon.repository.MedServRepository;

import com.example.onehealthrest.service.MedServService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MedServServiceImpl implements MedServService {

    private final MedServRepository medServRepository;
    private final MedServMapper medServMapper;

    @Override
    public List<MedServ> getPriceList() {
        return medServRepository.findAll();
    }

    @Override
    public MedServ save(MedServ medServ) {
        return medServRepository.save(medServ);
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isPresent()) {
            medServRepository.deleteById(id);
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Optional<MedServ> findById(int id) {
        return medServRepository.findById(id);
    }

    @Override
    public MedServ update(int id, MedServDto requestDto) {
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isPresent()) {
            MedServ medServ = medServMapper.map(requestDto);
            medServ.setId(id);
            return medServ;
        }
        return null;
    }
}