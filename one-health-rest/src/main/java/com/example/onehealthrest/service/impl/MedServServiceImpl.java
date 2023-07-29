/**
 * Service implementation for managing medical services.
 * This class provides methods to perform CRUD (Create, Read, Update, Delete) operations
 * on medical services and interact with the underlying database using the MedServRepository.
 */

package com.example.onehealthrest.service.impl;

import com.example.onehealthcommon.dto.CreateMedServDto;
import com.example.onehealthcommon.dto.MedServDto;
import com.example.onehealthcommon.entity.MedServ;
import com.example.onehealthcommon.exception.EntityNotFoundException;
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
        List<MedServ> all = medServRepository.findAll();
        if (all.isEmpty()) {
            try {
                throw new EntityNotFoundException("Is Empty" + all);
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return all;
    }

    @Override
    public MedServDto save(MedServ medServ) {
        return medServMapper.mapTo(medServRepository.save(medServ));
    }

    @Override
    public boolean delete(int id) {
        boolean isDeleted = false;
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isEmpty()) {
            try {
                throw new EntityNotFoundException("DeleteById with " + id + " does not exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }
        } else {
            log.info("Medical Service with the id " + id + " id has been successfully deleted ");
            medServRepository.deleteById(id);
            return isDeleted = true;
        }
    }


    /**
     * Updates the details of a medical service with the specified ID.
     *
     * @param id          The ID of the medical service to be updated.
     * @param requestDto  The DTO containing the updated details for the medical service.
     * @return An optional containing the updated medical service DTO if found and updated successfully,
     *         or an empty optional if the medical service with the given ID does not exist.
     */
    @Override
    public Optional<MedServDto> update(int id, CreateMedServDto requestDto) {
        Optional<MedServ> optionalMedServ = medServRepository.findById(id);
        if (optionalMedServ.isEmpty()) {
            try {
                throw new EntityNotFoundException("MedServ with " + id + " there exist");
            } catch (EntityNotFoundException e) {
                throw new RuntimeException(e);
            }

        }else {
            MedServ medServ = medServMapper.map(requestDto);
            medServ.setId(id);
            medServRepository.save(medServ);
            log.info("Medical Service with the id " + id + " id has been successfully updated ");
            return Optional.of(medServMapper.mapTo(medServ));
        }
    }

    @Override
    public Optional<MedServ> findById(int id) {
        return medServRepository.findById(id);
    }
}
