package com.example.onehealthcommon.manager;

import com.example.onehealthcommon.dto.DoctorSearchDto;
import com.example.onehealthcommon.entity.Doctor;
import com.example.onehealthcommon.entity.QDoctor;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;


@Data
@Component
@RequiredArgsConstructor
public class DoctorFilterManager {
    private final QDoctor qDoctor = QDoctor.doctor;
    @PersistenceContext
    private EntityManager entityManager;

    public List<Doctor> searchDoctorsByFilter(int page, int size, DoctorSearchDto doctorSearchDto) {
        var query = new JPAQuery<Doctor>(entityManager);
        JPAQuery<Doctor> from = query.from(qDoctor);
        getNameFilter(from, doctorSearchDto.getName());
        getSurnameFilter(from, doctorSearchDto.getSurname());
        getEmailFilter(from, doctorSearchDto.getEmail());
        getSpecialityFilter(from, doctorSearchDto.getSpeciality());
        getPagination(from, page, size);
        getSorting(from, doctorSearchDto.getSortBy(), doctorSearchDto.getSortDirection());
        return from.fetch();
    }

    private void getNameFilter(JPAQuery<Doctor> from, String name) {
        if (name != null && !name.isEmpty()) {
            from.where(qDoctor.name.contains(name));
        }
    }

    private void getSurnameFilter(JPAQuery<Doctor> from, String surname) {
        if (surname != null && !surname.isEmpty()) {
            from.where(qDoctor.surname.contains(surname));
        }
    }

    private void getEmailFilter(JPAQuery<Doctor> from, String email) {
        if (email != null && !email.isEmpty()) {
            from.where(qDoctor.email.contains(email));
        }
    }

    private void getSpecialityFilter(JPAQuery<Doctor> from, String speciality) {
        if (speciality != null && !speciality.isEmpty()) {
            from.where(qDoctor.speciality.contains(speciality));
        }
    }

    private void getPagination(JPAQuery<Doctor> from, int page, int size) {
        if (page > 0) {
            from.offset((long) page * size);
        }
        from.limit(size);
    }

    private void getSorting(JPAQuery<Doctor> from, String sortBy, String sortDirection) {
        if (sortBy != null && !sortBy.isEmpty()) {
            PathBuilder<Object> orderByExpression = new PathBuilder<>(Doctor.class, sortBy);
            from.orderBy(new OrderSpecifier("asc".equalsIgnoreCase(sortDirection) ?
                    Order.ASC : Order.DESC, orderByExpression));
        }
    }
}
