package com.cseintellilearnapi.config;

import com.cseintellilearnapi.entity.*;
import com.cseintellilearnapi.repository.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * DataSeeder — runs automatically on every application startup.
 *
 * Seeds:
 *  1. Admin user         (admin@intellilearn.com / admin123)
 *  2. Sample Academic Years   → First Year, Second Year, Third Year, Fourth Year
 *  3. Sample Semesters        → 2 per year (Sem I & Sem II)
 *
 * All inserts are guarded by "already exists" checks so re-runs are safe.
 */
@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    @org.springframework.beans.factory.annotation.Value("${app.admin.email:admin@intellilearn.com}")
    private String adminEmail;

    @org.springframework.beans.factory.annotation.Value("${app.admin.password:admin123}")
    private String adminPassword;

    @org.springframework.beans.factory.annotation.Value("${app.admin.name:System Admin}")
    private String adminName;

    private final UserRepository        userRepository;
    private final AcademicYearRepository yearRepository;
    private final SemesterRepository    semesterRepository;
    private final PasswordEncoder       passwordEncoder;

    public DataSeeder(UserRepository userRepository,
                      AcademicYearRepository yearRepository,
                      SemesterRepository semesterRepository,
                      PasswordEncoder passwordEncoder) {
        this.userRepository     = userRepository;
        this.yearRepository     = yearRepository;
        this.semesterRepository = semesterRepository;
        this.passwordEncoder    = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        seedAdmin();
        seedAcademicYears();
    }

    // ── 1. Admin user ─────────────────────────────────────────────────────────
    private void seedAdmin() {
        if (userRepository.existsByEmail(adminEmail)) {
            log.info("[Seeder] Admin already exists — skipping.");
            return;
        }

        User admin = new User();
        admin.setName(adminName);
        admin.setEmail(adminEmail);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setRole(Role.ROLE_ADMIN);
        admin.setDepartment("CSE");

        userRepository.save(admin);
        log.info("╔══════════════════════════════════════════╗");
        log.info("║       ADMIN SEEDED SUCCESSFULLY          ║");
        log.info("║  Email    : {}  ║", adminEmail);
        log.info("║  Password : {}                    ║", adminPassword);
        log.info("╚══════════════════════════════════════════╝");
    }

    // ── 2. Academic Years + Semesters ─────────────────────────────────────────
    private void seedAcademicYears() {
        if (yearRepository.findAllByOrderByYearIdAsc().size() > 0) {
            log.info("[Seeder] Academic years already exist — skipping.");
            return;
        }

        String[][] yearData = {
            {"First Year"},
            {"Second Year"},
            {"Third Year"},
            {"Fourth Year"},
        };

        for (String[] yd : yearData) {
            AcademicYear year = new AcademicYear();
            year.setYearName(yd[0]);
            yearRepository.save(year);
            log.info("[Seeder] Created academic year: {}", yd[0]);
        }

        log.info("[Seeder] Academic years seeded successfully.");
    }
}
