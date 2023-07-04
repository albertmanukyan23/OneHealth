package com.example.onehealth.service.impl;
import com.example.onehealth.entity.Patient;
import com.example.onehealth.entity.UserType;
import com.example.onehealth.repository.PatientRepository;
import com.example.onehealth.service.EmailSenderService;
import com.example.onehealth.service.PatientService;
import com.example.onehealth.service.UserService;
import com.example.onehealth.util.ImageDownloader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final PatientRepository patientRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final ImageDownloader imageDownloader;
    private final EmailSenderService emailSenderService;

    @Value("${site.url}")
    private String siteUrl;

    @Override
    public List<Patient> getPatient() {
        return patientRepository.findAll();
    }

    @Override
    public Page<Patient> getPatientPag(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public void update(Patient patient, MultipartFile multipartFile) throws IOException {
        Optional<Patient> byId = patientRepository.findById(patient.getId());
        if (byId.isPresent()) {
            Patient patientDbData = byId.get();
            if (patientRepository.findByEmail(patient.getEmail()).isEmpty() || patient.getEmail().equals(patientDbData.getEmail())) {
                patientDbData.setName(patient.getName());
                patientDbData.setSurname(patient.getSurname());
                patientDbData.setEmail(patient.getEmail());
                patientDbData.setPassword(passwordEncoder.encode(patient.getPassword()));
                patientDbData.setBirthDate(patient.getBirthDate());
                patientDbData.setPicName(patient.getPicName());
                patientDbData.setRegion(patient.getRegion());
                patientDbData.setNation(patient.getNation());
                patientDbData.setAddress(patient.getAddress());
                patient.setRegisDate(new Date());
                imageDownloader.saveProfilePicture(multipartFile, patient);
                patientRepository.save(patientDbData);
            }
        }
    }

    @Override
    public Optional<Patient> findPatientById(int id) {
        return patientRepository.findById(id);
    }

    @Override
    public Page<Patient> getPatientPageData(Optional<Integer> page, Optional<Integer> size) {
        int currentPage = page.orElse(1);
        int pageSize = size.orElse(5);
        Pageable pageable = PageRequest.of(currentPage - 1, pageSize);
        return patientRepository.findAll(pageable);
    }

    @Override
    public List<Integer> getPageNumbers(int totalPages) {
        if (totalPages > 0) {
            return IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
        }
        return null;
    }
    @Override
    @Transactional
    public void save(MultipartFile multipartFile, Patient patient) throws IOException {
        patient.setRegisDate(new Date());
        imageDownloader.saveProfilePicture(multipartFile, patient);
        patient.setUserType(UserType.PATIENT);
        UUID token = UUID.randomUUID();
        patient.setToken(token.toString());
        patient.setEnabled(false);
        userService.registerUser(patient);
        verifyAccountWithEmail(patient.getId());
    }

    @Override
    public Optional<Patient> findByEmail(String email) {
        return patientRepository.findByEmail(email);
    }


    @Override
    public void verifyAccount(String email, String token) {
        Optional<Patient> byEmail = patientRepository.findByEmail(email);
        if (byEmail.get().getToken().equals(token)) {
            Patient patientData = byEmail.get();
            patientData.setEnabled(true);
            patientData.setToken(null);
            patientRepository.save(patientData);
        }
    }

    @Override
    public void confirmationMessage(String email) {
        Optional<Patient> byEmail = patientRepository.findByEmail(email);
        if (byEmail.isPresent()) {
            Patient patient = byEmail.get();
            UUID token = UUID.randomUUID();
            patient.setToken(token.toString());
            patientRepository.save(patient);
            verifyAccountMessageEmail(patient.getId());
        }

    }

    @Override
    public void changePassword(String email, String token) {
        Optional<Patient> byEmail = patientRepository.findByEmail(email);
        if (byEmail.get().getToken().equals(token)) {
            Patient patient = byEmail.get();
            patient.setToken(null);
            patientRepository.save(patient);
        }
    }

    @Override
    public void updatePassword(String email, String token, String password, String passwordRepeat) {
        Optional<Patient> byEmail = patientRepository.findByEmail(email);
        if (byEmail.isPresent() && byEmail.get().isEnabled()) {
            if (password.equals(passwordRepeat) && byEmail.get().getToken() == null) {
                Patient patient = byEmail.get();
                patient.setPassword(passwordEncoder.encode(password));
                patientRepository.save(patient);
            }
        }
    }

    @Async
    public void verifyAccountMessageEmail(int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {
            Patient patient = byId.get();
            emailSenderService.sendSimpleEmail(patient.getEmail(),
                    "Welcome", "Hi" + patient.getName() +
                            "\n" + "Confirm to rest password " +
                            siteUrl + "/patient/change-password-page?email=" + patient.getEmail() + "&token=" + patient.getToken());
        }
    }
    @Async
    public void verifyAccountWithEmail(int id) {
        Optional<Patient> byId = patientRepository.findById(id);
        if (byId.isPresent()) {
            Patient patient = byId.get();
            emailSenderService.sendSimpleEmail(patient.getEmail(),
                    "Welcome", "Hi" + patient.getName() +
                            "\n" + "Please verify your email by clicking on this url " +
                            siteUrl + "/patient/verify?email=" + patient.getEmail() + "&token=" + patient.getToken());
        }
    }
}
