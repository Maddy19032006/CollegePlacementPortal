package com.college.placement.api;

import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.transaction.annotation.Transactional;

import com.college.placement.entity.Role;
import com.college.placement.entity.User;
import com.college.placement.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * End-to-end API tests with MockMvc against the real Spring context
 * (H2 in-memory database, no server needed).
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void seedOfficer() {
        if (userRepository.findByUsername("officer1").isEmpty()) {
            userRepository.save(new User("officer1", "officer1@college.edu",
                    passwordEncoder.encode("Officer@123"), "Officer One", Role.PLACEMENT_OFFICER));
        }
    }

    @Test
    void registerStudentCreatesAccountWithHashedPassword() throws Exception {
        Map<String, String> body = Map.of(
                "username", "newstudent",
                "email", "newstudent@college.edu",
                "fullName", "New Student",
                "password", "Strong@Pass1",
                "role", "STUDENT",
                "studentId", "STD-001",
                "department", "CS",
                "degree", "B.Tech");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newstudent"));

        User saved = userRepository.findByUsername("newstudent").orElseThrow();
        org.junit.jupiter.api.Assertions.assertNotEquals("Strong@Pass1", saved.getPassword(),
                "Password must not be stored in plain text");
        org.junit.jupiter.api.Assertions.assertTrue(
                passwordEncoder.matches("Strong@Pass1", saved.getPassword()));
    }

    @Test
    void weakPasswordIsRejected() throws Exception {
        Map<String, String> body = Map.of(
                "username", "weakpw",
                "email", "weakpw@college.edu",
                "fullName", "Weak PW",
                "password", "weak",
                "role", "STUDENT",
                "studentId", "STD-002",
                "department", "CS",
                "degree", "B.Tech");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void placementOfficerRegistrationRequiresEmployeeId() throws Exception {
        Map<String, String> body = Map.of(
                "username", "notanofficer",
                "email", "notanofficer@college.edu",
                "fullName", "Not An Officer",
                "password", "Strong@Pass1",
                "role", "PLACEMENT_OFFICER");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Employee ID is required for placement officers"));
    }

    @Test
    void placementOfficerRegistrationSucceeds() throws Exception {
        Map<String, String> body = Map.of(
                "username", "newofficer",
                "email", "newofficer@college.edu",
                "fullName", "New Officer",
                "password", "Strong@Pass1",
                "role", "PLACEMENT_OFFICER",
                "employeeId", "EMP123",
                "department", "CS",
                "companyName", "Tech Corp",
                "designation", "HR Manager");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("newofficer"));

        User saved = userRepository.findByUsername("newofficer").orElseThrow();
        org.junit.jupiter.api.Assertions.assertEquals(Role.PLACEMENT_OFFICER, saved.getRole());
        org.junit.jupiter.api.Assertions.assertEquals("EMP123", saved.getEmployeeId());
        org.junit.jupiter.api.Assertions.assertEquals("CS", saved.getDepartment());
    }

    @Test
    void duplicateUsernameIsRejected() throws Exception {
        Map<String, String> body = Map.of(
                "username", "officer1",
                "email", "other@college.edu",
                "fullName", "Duplicate",
                "password", "Strong@Pass1",
                "role", "PLACEMENT_OFFICER");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(body)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Username is already taken"));
    }

    @Test
    void protectedEndpointRequiresLogin() throws Exception {
        mockMvc.perform(get("/api/students/profile"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void studentCannotAccessAdminEndpoints() throws Exception {
        mockMvc.perform(get("/api/admin/users").with(user("stu").roles("STUDENT")))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminCanRemovePlacementOfficer() throws Exception {
        User removable = userRepository.save(new User("remove-me", "remove-me@college.edu",
                passwordEncoder.encode("Officer@123"), "Remove Me", Role.PLACEMENT_OFFICER));

        mockMvc.perform(delete("/api/admin/users/{id}", removable.getId())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isNoContent());

        User removed = userRepository.findById(removable.getId()).orElseThrow();
        org.junit.jupiter.api.Assertions.assertTrue(removed.isRemoved());
    }

    @Test
    void adminCannotRemoveAdminAccount() throws Exception {
        User admin = userRepository.save(new User("another-admin", "another-admin@college.edu",
                passwordEncoder.encode("Admin@123"), "Another Admin", Role.ADMIN));

        mockMvc.perform(delete("/api/admin/users/{id}", admin.getId())
                        .with(user("admin").roles("ADMIN")))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Admin accounts cannot be deleted"));
    }

    @Test
    void removedUserReceivesRemovalMessageOnLogin() throws Exception {
        User removed = userRepository.save(new User("removed-user", "removed-user@college.edu",
                passwordEncoder.encode("Student@123"), "Removed User", Role.STUDENT));
        removed.setRemoved(true);
        removed.setEnabled(false);
        userRepository.save(removed);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of(
                                "username", "removed-user",
                                "password", "Student@123"))))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.message").value(
                        "Your account has been removed by an administrator. Please contact the administrator."));
    }

    @Test
    void officerCanAccessOfficerDashboard() throws Exception {
        mockMvc.perform(get("/api/officer/dashboard").with(user("officer1").roles("PLACEMENT_OFFICER")))
                .andExpect(status().isOk());
    }

    @Test
    void studentCannotCreateCompany() throws Exception {
        String body = """
                {
                  "name": "Hacker Corp",
                  "jobRole": "Dev",
                  "minCgpa": 5,
                  "applicationDeadline": "2099-01-01"
                }
                """;
        mockMvc.perform(post("/api/companies").with(user("stu").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isForbidden());
    }

    @Test
    void officerCanCreateAndDeleteCompany() throws Exception {
        String body = """
                {
                  "name": "Integration Corp",
                  "jobRole": "QA Engineer",
                  "packageCtc": 5.5,
                  "minCgpa": 6.5,
                  "requiredDepartments": "CSE, IT",
                  "requiredSkills": "Java",
                  "minGraduationYear": 2026,
                  "applicationDeadline": "2099-01-01",
                  "description": "Created by integration test"
                }
                """;
        String response = mockMvc.perform(post("/api/companies")
                        .with(user("officer1").roles("PLACEMENT_OFFICER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Integration Corp"))
                .andReturn().getResponse().getContentAsString();

        String id = objectMapper.readTree(response).get("id").asText();

        mockMvc.perform(put("/api/companies/" + id)
                        .with(user("officer1").roles("PLACEMENT_OFFICER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body.replace("QA Engineer", "Senior QA Engineer")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.jobRole").value("Senior QA Engineer"));

        mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .delete("/api/companies/" + id)
                        .with(user("officer1").roles("PLACEMENT_OFFICER")))
                .andExpect(status().isNoContent());
    }

    @Test
    void studentEndpointsRejectMissingProfileGracefully() throws Exception {
        mockMvc.perform(get("/api/students/dashboard").with(user("onlyuser").roles("STUDENT")))
                .andExpect(status().is4xxClientError());
    }
}
