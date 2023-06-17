package com.example.onehealth.service.impl;

import com.example.onehealth.entity.MedServ;
import com.example.onehealth.repository.MedServRepository;
import com.example.onehealth.service.MedServService;
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
