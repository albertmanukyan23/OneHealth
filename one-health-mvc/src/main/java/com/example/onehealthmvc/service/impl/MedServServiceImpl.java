package com.example.onehealthmvc.service.impl;


import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.repository.MedServRepository;
import com.example.onehealthmvc.service.MedServService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MedServServiceImpl implements MedServService {
    private  final MedServRepository medServRepository;

    @Override
    public List<MedServ> getPriceList() {
        return medServRepository.findAll();
    }

    @Override
    public MedServ save(MedServ medServ) {
        return medServRepository.save(medServ);
    }

    @Override
    public void delete(int id) {
        medServRepository.deleteById(id);
    }
}
