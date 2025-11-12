//package com.ss.doctorandscheduling.service.repositories;
//
//import com.ss.doctorandscheduling.service.entities.Doctor;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//
//public interface DoctorRepository extends JpaRepository<Doctor, Long> {
//
//    Page<Doctor> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);
//
//    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);
//}


package com.ss.doctorandscheduling.service.repositories;

import com.ss.doctorandscheduling.service.entities.Doctor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Page<Doctor> findByDepartmentContainingIgnoreCase(String department, Pageable pageable);

    Page<Doctor> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<Doctor> findByActive(boolean active, Pageable pageable);

    Optional<Doctor> findByDoctorIdAndActiveTrue(Long doctorId);

    Optional<Doctor> findByDoctorIdAndDepartmentIgnoreCaseAndActiveTrue(Long doctorId, String department);

    Page<Doctor> findByActiveAndDepartmentContainingIgnoreCase(boolean active, String department, Pageable pageable);
    Page<Doctor> findByActiveAndNameContainingIgnoreCase(boolean active, String name, Pageable pageable);

}
