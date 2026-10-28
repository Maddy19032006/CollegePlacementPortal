package com.college.placement.config;

import com.college.placement.entity.Application;
import com.college.placement.entity.ApplicationStatus;
import com.college.placement.entity.Company;
import com.college.placement.entity.PreparationScore;
import com.college.placement.entity.Role;
import com.college.placement.entity.Skill;
import com.college.placement.entity.StudentProfile;
import com.college.placement.entity.StudentSkill;
import com.college.placement.entity.User;
import com.college.placement.repository.ApplicationRepository;
import com.college.placement.repository.CompanyRepository;
import com.college.placement.repository.PreparationScoreRepository;
import com.college.placement.repository.SkillRepository;
import com.college.placement.repository.StudentProfileRepository;
import com.college.placement.repository.StudentSkillRepository;
import com.college.placement.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Seeds demo data on first startup so the application is immediately
 * demonstrable with a fresh database. Runs only when the users table is empty.
 *
 * All demo passwords come from configuration (see .env.example) - none are
 * hardcoded here.
 */
@Component
public class DemoDataSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(DemoDataSeeder.class);

    private final UserRepository userRepository;
    private final StudentProfileRepository studentProfileRepository;
    private final CompanyRepository companyRepository;
    private final ApplicationRepository applicationRepository;
    private final PreparationScoreRepository preparationScoreRepository;
    private final SkillRepository skillRepository;
    private final StudentSkillRepository studentSkillRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.demo-passwords.student}")
    private String studentPassword;

    @Value("${app.demo-passwords.officer}")
    private String officerPassword;

    @Value("${app.demo-passwords.admin}")
    private String adminPassword;

    public DemoDataSeeder(UserRepository userRepository,
                          StudentProfileRepository studentProfileRepository,
                          CompanyRepository companyRepository,
                          ApplicationRepository applicationRepository,
                          PreparationScoreRepository preparationScoreRepository,
                          SkillRepository skillRepository,
                          StudentSkillRepository studentSkillRepository,
                          PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.studentProfileRepository = studentProfileRepository;
        this.companyRepository = companyRepository;
        this.applicationRepository = applicationRepository;
        this.preparationScoreRepository = preparationScoreRepository;
        this.skillRepository = skillRepository;
        this.studentSkillRepository = studentSkillRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        if (userRepository.count() > 0) {
            log.info("Existing data found - skipping demo data seeding");
            return;
        }
        log.info("Seeding demo data...");

        seedAdmin();
        seedOfficer();
        StudentProfile arjun = seedStudent("arjun", "arjun.sharma@college.edu", "Arjun Sharma",
                "CS2021001", "CSE", 2026, new BigDecimal("8.6"),
                List.of("Java", "SQL", "Spring Boot", "Data Structures"),
                new PreparationScore(null, 80, 75, 85, 70, 78));
        StudentProfile priya = seedStudent("priya", "priya.iyer@college.edu", "Priya Iyer",
                "CS2021002", "IT", 2026, new BigDecimal("9.1"),
                List.of("Python", "Machine Learning", "SQL"),
                new PreparationScore(null, 85, 65, 80, 90, 88));
        StudentProfile rahul = seedStudent("rahul", "rahul.verma@college.edu", "Rahul Verma",
                "EC2021007", "ECE", 2026, new BigDecimal("7.2"),
                List.of("C++", "Embedded C"),
                new PreparationScore(null, 60, 55, 65, 50, 72));

        Company infosys = seedCompany("Infotech Solutions", "Software Engineer", "8.5",
                "CSE, IT, ECE", "Java, SQL", 2026, LocalDate.now().plusDays(20),
                "Service-based company hiring across all departments.");
        Company datawave = seedCompany("DataWave Analytics", "Data Analyst", "9.0",
                "CSE, IT", "Python, SQL", 2026, LocalDate.now().plusDays(10),
                "Analytics firm focused on data platforms.");
        Company circuitcore = seedCompany("CircuitCore Systems", "Embedded Engineer", "7.0",
                "ECE, EEE", "C++", 2026, LocalDate.now().plusDays(35),
                "Embedded systems and IoT product company.");

        seedApplication(arjun, infosys, ApplicationStatus.SHORTLISTED);
        seedApplication(priya, datawave, ApplicationStatus.ONLINE_TEST);
        seedApplication(rahul, circuitcore, ApplicationStatus.APPLIED);

        log.info("Demo data seeding complete");
    }

    private void seedAdmin() {
        userRepository.save(new User("admin", "admin@college.edu",
                passwordEncoder.encode(adminPassword), "System Administrator", Role.ADMIN));
    }

    private void seedOfficer() {
        userRepository.save(new User("officer", "officer@college.edu",
                passwordEncoder.encode(officerPassword), "Prof. Meena Rao", Role.PLACEMENT_OFFICER));
    }

    private StudentProfile seedStudent(String username, String email, String fullName,
                                       String registrationNumber, String department, int graduationYear,
                                       BigDecimal cgpa, List<String> skills, PreparationScore scores) {
        User user = userRepository.save(new User(username, email,
                passwordEncoder.encode(studentPassword), fullName, Role.STUDENT));

        StudentProfile profile = new StudentProfile();
        profile.setUser(user);
        profile.setRegistrationNumber(registrationNumber);
        profile.setPhone("98765432" + (10 + registrationNumber.length()));
        profile.setDepartment(department);
        profile.setProgram("B.Tech");
        profile.setGraduationYear(graduationYear);
        profile.setCgpa(cgpa);
        profile.setCertifications(" participated in college coding contests");
        profile.setProjects("Academic mini-projects and a capstone project");
        profile.setInternshipExperience("Industry-ready coursework and hands-on lab experience");
        profile.setExtracurricularActivities("Coding club participant and peer mentor");
        profile.setResumeSummary("Motivated " + department + " student looking for software roles");
        profile = studentProfileRepository.save(profile);

        for (String skillName : skills) {
            Skill skill = skillRepository.findByNameIgnoreCase(skillName)
                    .orElseGet(() -> skillRepository.save(new Skill(skillName)));
            studentSkillRepository.save(new StudentSkill(profile, skill));
        }

        scores.setStudent(profile);
        preparationScoreRepository.save(scores);
        return profile;
    }

    private Company seedCompany(String name, String jobRole, String minCgpa,
                                String departments, String skills, int minGraduationYear,
                                LocalDate deadline, String description) {
        Company company = new Company();
        company.setName(name);
        company.setJobRole(jobRole);
        company.setPackageCtc(new BigDecimal("6.5"));
        company.setMinCgpa(new BigDecimal(minCgpa));
        company.setRequiredDepartments(departments);
        company.setRequiredSkills(skills);
        company.setMinGraduationYear(minGraduationYear);
        company.setApplicationDeadline(deadline);
        company.setDescription(description);
        return companyRepository.save(company);
    }

    private void seedApplication(StudentProfile student, Company company, ApplicationStatus status) {
        Application application = new Application();
        application.setStudent(student);
        application.setCompany(company);
        application.setStatus(status);
        applicationRepository.save(application);
    }
}
