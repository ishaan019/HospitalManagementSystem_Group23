//package com.ss.doctorandscheduling.service.services;
//
//import com.ss.doctorandscheduling.service.entities.Doctor;
//import com.ss.doctorandscheduling.service.repositories.DoctorRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.stereotype.Service;
//
//@Service
//public class DoctorService {
//
//    @Autowired
//    private DoctorRepository doctorRepository;
//
//    public Page<Doctor> getAllDoctors(String department, String name, Pageable pageable) {
//        if (department != null && !department.isBlank()) {
//            return doctorRepository.findByDepartmentContainingIgnoreCase(department, pageable);
//        } else if (name != null && !name.isBlank()) {
//            return doctorRepository.findByNameContainingIgnoreCase(name, pageable);
//        } else {
//            return doctorRepository.findAll(pageable);
//        }
//    }
//
//    public Doctor getDoctorById(Long doctorId) {
//        return doctorRepository.findById(doctorId)
//                .orElseThrow(() -> new RuntimeException("Doctor not found with id " + doctorId));
//    }
//
//    public Doctor createDoctor(Doctor doctor) {
//        return doctorRepository.save(doctor);
//    }
//
//    public Doctor updateDoctor(Long doctorId, Doctor updatedDoctor) {
//        Doctor existingDoctor = getDoctorById(doctorId);
//        existingDoctor.setName(updatedDoctor.getName());
//        existingDoctor.setEmail(updatedDoctor.getEmail());
//        existingDoctor.setPhone(updatedDoctor.getPhone());
//        existingDoctor.setDepartment(updatedDoctor.getDepartment());
//        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
//        return doctorRepository.save(existingDoctor);
//    }
//
//    public void deleteDoctor(Long doctorId) {
//        doctorRepository.deleteById(doctorId);
//    }
//}
//


package com.ss.doctorandscheduling.service.services;

import com.ss.doctorandscheduling.service.dto.ErrorResponse;
import com.ss.doctorandscheduling.service.entities.Doctor;
import com.ss.doctorandscheduling.service.repositories.DoctorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Service
public class DoctorService {

    @Autowired
    private DoctorRepository doctorRepository;

    public Page<Doctor> getAllDoctors(String department, String name, Pageable pageable) {
        if (department != null && !department.isBlank()) {
            return doctorRepository.findByDepartmentContainingIgnoreCase(department, pageable);
        } else if (name != null && !name.isBlank()) {
            return doctorRepository.findByNameContainingIgnoreCase(name, pageable);
        } else {
            return doctorRepository.findAll(pageable);
        }
    }

    public Doctor getDoctorById(Long doctorId) {
        return doctorRepository.findById(doctorId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found with id " + doctorId));
    }

    // ✅ Check if doctor is active and belongs to department
    public Doctor validateDoctorForAppointment(Long doctorId, String department) {
        return doctorRepository.findByDoctorIdAndDepartmentIgnoreCaseAndActiveTrue(doctorId, department)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Doctor either inactive, not found, or not part of department: " + department
                ));
    }

    public Doctor createDoctor(Doctor doctor) {
        if (doctor.getActive() == false) {
            doctor.setActive(true);
        }
        return doctorRepository.save(doctor);
    }

    public Doctor updateDoctor(Long doctorId, Doctor updatedDoctor) {
        Doctor existingDoctor = getDoctorById(doctorId);

        // ✅ 1. Check for duplicate email (only if changed)
        if (!existingDoctor.getEmail().equalsIgnoreCase(updatedDoctor.getEmail())) {
            boolean emailExists = doctorRepository.findAll().stream()
                    .anyMatch(d -> !d.getDoctorId().equals(doctorId)
                            && d.getEmail().equalsIgnoreCase(updatedDoctor.getEmail()));
            if (emailExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A doctor with this email already exists.");
            }
            existingDoctor.setEmail(updatedDoctor.getEmail());
        }

        // ✅ 2. Check for duplicate phone (only if changed)
        if (!existingDoctor.getPhone().equals(updatedDoctor.getPhone())) {
            boolean phoneExists = doctorRepository.findAll().stream()
                    .anyMatch(d -> !d.getDoctorId().equals(doctorId)
                            && d.getPhone().equals(updatedDoctor.getPhone()));
            if (phoneExists) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A doctor with this phone number already exists.");
            }
            existingDoctor.setPhone(updatedDoctor.getPhone());
        }

        // ✅ 3. Update other editable fields
        existingDoctor.setName(updatedDoctor.getName());
        existingDoctor.setDepartment(updatedDoctor.getDepartment());
        existingDoctor.setSpecialization(updatedDoctor.getSpecialization());
        existingDoctor.setActive(updatedDoctor.getActive());

        // ✅ 4. Save
        return doctorRepository.save(existingDoctor);
    }


    // ✅ Activate/deactivate doctor
    public Doctor toggleDoctorStatus(Long doctorId, boolean active) {
        Doctor doctor = getDoctorById(doctorId);
        doctor.setActive(active);
        return doctorRepository.save(doctor);
    }

    public void deleteDoctor(Long doctorId) {
        if (!doctorRepository.existsById(doctorId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found with id " + doctorId);
        }
        doctorRepository.deleteById(doctorId);
    }

    // ✅ Centralized error helper (for future usage)
    public ErrorResponse buildError(int code, String message) {
        return ErrorResponse.builder()
                .code(code)
                .message(message)
                .correlationId(UUID.randomUUID().toString())
                .build();
    }

    public Page<Doctor> getDoctorsByActive(boolean active, String department, String name, Pageable pageable) {
        if (department != null && !department.isBlank()) {
            return doctorRepository.findByActiveAndDepartmentContainingIgnoreCase(active, department, pageable);
        } else if (name != null && !name.isBlank()) {
            return doctorRepository.findByActiveAndNameContainingIgnoreCase(active, name, pageable);
        } else {
            return doctorRepository.findByActive(active, pageable);
        }
    }

}
