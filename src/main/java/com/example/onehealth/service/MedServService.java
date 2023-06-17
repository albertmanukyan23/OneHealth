package com.example.onehealth.service;

import com.example.onehealth.entity.MedServ;

import java.util.List;

public interface MedServService {

    List<MedServ> getPriceList();

    MedServ save(MedServ medServ);

    void delete(int id);
}
