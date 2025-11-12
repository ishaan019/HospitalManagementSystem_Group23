//package com.ss.doctorandscheduling.service.controllers;
//
//import com.ss.doctorandscheduling.service.entities.Doctor;
//import com.ss.doctorandscheduling.service.services.DoctorService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/doctors")
//@Tag(name = "Doctor APIs", description = "APIs for managing doctors and schedules")
//public class DoctorController {
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @GetMapping
//    @Operation(summary = "List doctors with optional department or name filter")
//    public ResponseEntity<Page<Doctor>> listDoctors(
//            @RequestParam(required = false) String department,
//            @RequestParam(required = false) String name,
//            @ParameterObject Pageable pageable) {
//        return ResponseEntity.ok(doctorService.getAllDoctors(department, name, pageable));
//    }
//
//    @GetMapping("/{id}")
//    @Operation(summary = "Get doctor by ID")
//    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
//        return ResponseEntity.ok(doctorService.getDoctorById(id));
//    }
//
//    @PostMapping
//    @Operation(summary = "Create a new doctor")
//    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
//        return ResponseEntity.ok(doctorService.createDoctor(doctor));
//    }
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update a doctor")
//    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
//        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete a doctor")
//    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
//        doctorService.deleteDoctor(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}/slots")
//    @Operation(summary = "Check available slots for a doctor")
//    public ResponseEntity<List<String>> getAvailableSlots(@PathVariable Long id,
//                                                          @RequestParam(required = false) String date) {
//        // For demo, return static slots. You can implement DB-based schedule later
//        List<String> slots = List.of("09:00-09:30", "10:00-10:30", "11:00-11:30");
//        return ResponseEntity.ok(slots);
//    }
//
//}
//


//package com.ss.doctorandscheduling.service.controllers;
//
//import com.ss.doctorandscheduling.service.entities.Doctor;
//import com.ss.doctorandscheduling.service.services.DoctorService;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import org.springdoc.core.annotations.ParameterObject;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/v1/doctors")
//@Tag(name = "Doctor APIs", description = "APIs for managing doctors and schedules")
//public class DoctorController {
//
//    @Autowired
//    private DoctorService doctorService;
//
//    @GetMapping
//    @Operation(summary = "List doctors with optional department, name, or active filter")
//    public ResponseEntity<Page<Doctor>> listDoctors(
//            @RequestParam(required = false) String department,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) Boolean active,
//            @ParameterObject Pageable pageable) {
//        Page<Doctor> doctors;
//        if (active != null) {
//            doctors = (Page<Doctor>) doctorService.getAllDoctors(null, null, pageable)
//                    .map(d -> d.isActive() == active ? d : null)
//                    .filter(java.util.Objects::nonNull);
//        } else {
//            doctors = doctorService.getAllDoctors(department, name, pageable);
//        }
//        return ResponseEntity.ok(doctors);
//    }
//
//    @GetMapping("/{id}")
//    @Operation(summary = "Get doctor by ID")
//    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
//        return ResponseEntity.ok(doctorService.getDoctorById(id));
//    }
//
//    @PostMapping
//    @Operation(summary = "Create a new doctor")
//    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
//        return ResponseEntity.ok(doctorService.createDoctor(doctor));
//    }
//
//    @PutMapping("/{id}")
//    @Operation(summary = "Update a doctor")
//    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
//        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
//    }
//
//    @PatchMapping("/{id}/status")
//    @Operation(summary = "Activate or deactivate a doctor")
//    public ResponseEntity<Doctor> updateDoctorStatus(@PathVariable Long id, @RequestParam boolean active) {
//        return ResponseEntity.ok(doctorService.toggleDoctorStatus(id, active));
//    }
//
//    @DeleteMapping("/{id}")
//    @Operation(summary = "Delete a doctor")
//    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
//        doctorService.deleteDoctor(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping("/{id}/slots")
//    @Operation(summary = "Check available slots for a doctor")
//    public ResponseEntity<List<String>> getAvailableSlots(@PathVariable Long id,
//                                                          @RequestParam(required = false) String date) {
//        // Future: real scheduling integration
//        List<String> slots = List.of("09:00-09:30", "10:00-10:30", "11:00-11:30");
//        return ResponseEntity.ok(slots);
//    }
//
//    // ✅ Validation endpoint (used by Appointment Service)
//    @GetMapping("/{id}/validate")
//    @Operation(summary = "Validate if doctor is active and belongs to the given department")
//    public ResponseEntity<Doctor> validateDoctor(@PathVariable Long id, @RequestParam String department) {
//        try {
//            return ResponseEntity.ok(doctorService.validateDoctorForAppointment(id, department));
//        } catch (ResponseStatusException e) {
//            throw e; // handled globally by Spring Boot
//        }
//    }
//}

package com.ss.doctorandscheduling.service.controllers;

import com.ss.doctorandscheduling.service.entities.Doctor;
import com.ss.doctorandscheduling.service.services.DoctorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@Tag(name = "Doctor APIs", description = "APIs for managing doctors and schedules")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @GetMapping
    @Operation(summary = "List doctors with optional department, name, or active filter")
    public ResponseEntity<Page<Doctor>> listDoctors(
            @RequestParam(required = false) String department,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Boolean active,
            @ParameterObject Pageable pageable) {

        Page<Doctor> doctors;

        if (active != null) {
            // ✅ Filter by active + optional department/name
            doctors = doctorService.getDoctorsByActive(active, department, name, pageable);
        } else {
            // ✅ Default behavior (no active filter)
            doctors = doctorService.getAllDoctors(department, name, pageable);
        }

        return ResponseEntity.ok(doctors);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get doctor by ID")
    public ResponseEntity<Doctor> getDoctor(@PathVariable Long id) {
        return ResponseEntity.ok(doctorService.getDoctorById(id));
    }

    @PostMapping
    @Operation(summary = "Create a new doctor")
    public ResponseEntity<Doctor> createDoctor(@RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.createDoctor(doctor));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a doctor")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable Long id, @RequestBody Doctor doctor) {
        return ResponseEntity.ok(doctorService.updateDoctor(id, doctor));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Activate or deactivate a doctor")
    public ResponseEntity<Doctor> updateDoctorStatus(@PathVariable Long id, @RequestParam boolean active) {
        return ResponseEntity.ok(doctorService.toggleDoctorStatus(id, active));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a doctor")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/slots")
    @Operation(summary = "Check available slots for a doctor")
    public ResponseEntity<List<String>> getAvailableSlots(@PathVariable Long id,
                                                          @RequestParam(required = false) String date) {
        // Future: integrate with scheduling DB
        List<String> slots = List.of("09:00-09:30", "10:00-10:30", "11:00-11:30");
        return ResponseEntity.ok(slots);
    }

    // ✅ Validation endpoint (used by Appointment Service)
    @GetMapping("/{id}/validate")
    @Operation(summary = "Validate if doctor is active and belongs to the given department")
    public ResponseEntity<Doctor> validateDoctor(@PathVariable Long id, @RequestParam String department) {
        try {
            return ResponseEntity.ok(doctorService.validateDoctorForAppointment(id, department));
        } catch (ResponseStatusException e) {
            throw e; // Let Spring handle and return structured error JSON
        }
    }
}
