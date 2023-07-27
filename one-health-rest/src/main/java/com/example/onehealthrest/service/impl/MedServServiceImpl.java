package com.example.onehealthrest.service.impl;


import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.mapper.MedServMapper;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthrest.service.MedServService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MedServServiceImpl implements MedServService {

    private final MedServRepository medServRepository;
    private final MedServMapper medServMapper;

    @Override
    public List<MedServ> getPriceList() {
        return  medServRepository.findAll();
    }

    @Override
    public MedServDto save(MedServ medServ) {
        return medServMapper.mapTo(medServRepository.save(medServ));
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isPresent()) {
            medServRepository.deleteById(id);
            log.info("Medical Service with the id " + id + " id has been successfully deleted " );
            isDeleted = true;
        }
        return isDeleted;
    }

    @Override
    public Optional<MedServ> findById(int id) {
        return medServRepository.findById(id);
    }

    @Override
    public Optional<MedServDto> update(int id, CreateMedServDto requestDto) {
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isPresent()) {
            MedServ medServ = medServMapper.map(requestDto);
            medServ.setId(id);
            medServRepository.save(medServ);
            log.info("Medical Service with the id " + id + " id has been successfully updated " );
            return  Optional.of(medServMapper.mapTo(medServ));
        }
        return Optional.empty();
    }
}
