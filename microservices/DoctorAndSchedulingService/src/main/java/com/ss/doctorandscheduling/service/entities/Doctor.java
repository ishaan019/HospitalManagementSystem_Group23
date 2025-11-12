//package com.ss.doctorandscheduling.service.entities;
//
//import jakarta.persistence.*;
//import lombok.*;
//import java.time.LocalDateTime;
//
//@Entity
//@Table(name = "doctors")
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Builder
//public class Doctor {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long doctorId;
//
//    @Column(nullable = false)
//    private String name;
//
//    @Column(nullable = false, unique = true)
//    private String email;
//
//    @Column(nullable = false, unique = true)
//    private String phone;
//
//    @Column(nullable = false)
//    private String department;
//
//    @Column(nullable = false)
//    private String specialization;
//
//    @Column(nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    public void prePersist() {
//        createdAt = LocalDateTime.now();
//    }
//}


package com.ss.doctorandscheduling.service.entities;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "doctors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long doctorId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String phone;

    @Column(nullable = false)
    private String department;

    @Column(nullable = false)
    private String specialization;

    @Column(nullable = false)
    private boolean active = true; // ✅ new field for active/inactive

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        if (!this.active) this.active = true;
    }

    public boolean getActive() {
        return active;
    }
}
