package com.example.onehealthcommon.manager;

import com.example.onehealthcommon.dto.PatientSearchDto;
import com.example.onehealthcommon.entity.Patient;
import com.example.onehealthcommon.entity.QPatient;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
@Data
public class PatientFilterManager {
    private final QPatient qPatient = QPatient.patient;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Patient> searchPatientsByFilter(int page, int size, PatientSearchDto patientSearchDto) {
        var query = new JPAQuery<Patient>(entityManager);
        JPAQuery<Patient> from = query.from(qPatient);
        applyNameFilter(from, patientSearchDto.getName());
        applySurnameFilter(from, patientSearchDto.getSurname());
        applyEmailFilter(from, patientSearchDto.getEmail());
        applyRegionFilter(from, patientSearchDto.getRegion());
        applyAddressFilter(from, patientSearchDto.getAddress());
        applyNationFilter(from, patientSearchDto.getNation());
        applyBirthdayFilter(from, patientSearchDto.getBirthdayFrom(), patientSearchDto.getBirthdayTo());

        applyPagination(from, page, size);
        applySorting(from, patientSearchDto.getSortBy(), patientSearchDto.getSortDirection());

        return from.fetch();
    }

    private void applyNameFilter(JPAQuery<Patient> from, String name) {
        if (name != null && !name.isEmpty()) {
            from.where(qPatient.name.contains(name));
        }
    }

    private void applySurnameFilter(JPAQuery<Patient> from, String surname) {
        if (surname != null && !surname.isEmpty()) {
            from.where(qPatient.surname.contains(surname));
        }
    }

    private void applyEmailFilter(JPAQuery<Patient> from, String email) {
        if (email != null && !email.isEmpty()) {
            from.where(qPatient.email.contains(email));
        }
    }

    private void applyRegionFilter(JPAQuery<Patient> from, String region) {
        if (region != null && !region.isEmpty()) {
            from.where(qPatient.region.contains(region));
        }
    }

    private void applyAddressFilter(JPAQuery<Patient> from, String address) {
        if (address != null && !address.isEmpty()) {
            from.where(qPatient.address.contains(address));
        }
    }

    private void applyNationFilter(JPAQuery<Patient> from, String nation) {
        if (nation != null && !nation.isEmpty()) {
            from.where(qPatient.nation.contains(nation));
        }
    }

    private void applyBirthdayFilter(JPAQuery<Patient> from, Date birthdayFrom, Date birthdayTo) {
        if (birthdayFrom != null && birthdayTo != null) {
            from.where(qPatient.birthDate.between(birthdayFrom, birthdayTo));
        }
    }

    private void applyPagination(JPAQuery<Patient> from, int page, int size) {
        if (page > 0) {
            from.offset((long) page * size);
        }
        from.limit(size);
    }

    private void applySorting(JPAQuery<Patient> from, String sortBy, String sortDirection) {
        if (sortBy != null && !sortBy.isEmpty()) {
            PathBuilder<Object> orderByExpression = new PathBuilder<Object>(Patient.class, sortBy);
            from.orderBy(new OrderSpecifier("asc".equalsIgnoreCase(sortDirection) ? Order.ASC : Order.DESC, orderByExpression));
        }
    }
}
