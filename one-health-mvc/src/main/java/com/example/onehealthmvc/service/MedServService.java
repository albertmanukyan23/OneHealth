package com.example.onehealthmvc.service;

import com.example.onehealthcommon.entity.MedServ;

import java.util.List;

public interface MedServService {

    List<MedServ> getPriceList();

    MedServ save(MedServ medServ);

    void delete(int id);
}
