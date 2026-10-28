package com.college.placement.api;

import com.college.placement.entity.Role;
import com.college.placement.entity.User;
import com.college.placement.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Full student journey: profile -> preparation -> readiness score -> apply.
 * Proves the demo flow works end to end.
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class StudentJourneyIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String PROFILE = """
            {
              "registrationNumber": "JT2026001",
              "phone": "9876543210",
              "department": "CSE",
              "program": "B.Tech",
              "graduationYear": 2026,
              "cgpa": 8.8,
              "certifications": "AWS CCP",
              "projects": "Placement portal project",
              "internshipExperience": "Backend intern at Acme Labs",
              "extracurricularActivities": "Coding club lead",
              "resumeSummary": "Java backend enthusiast",
              "skills": ["Java", "SQL", "java"]
            }
            """;

    @Test
    void fullStudentJourneyFromProfileToApplication() throws Exception {
        User student = userRepository.save(new User("journeystu", "journeystu@college.edu",
                passwordEncoder.encode("Strong@Pass1"), "Journey Student", Role.STUDENT));

        // 1. Save the profile
        mockMvc.perform(put("/api/students/profile").with(user("journeystu").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON).content(PROFILE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cgpa").value(8.8))
                .andExpect(jsonPath("$.internshipExperience").value("Backend intern at Acme Labs"))
                .andExpect(jsonPath("$.extracurricularActivities").value("Coding club lead"))
                .andExpect(jsonPath("$.skills.length()").value(2));

        // 2. Save preparation scores and read the readiness score
        mockMvc.perform(put("/api/students/preparation").with(user("journeystu").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"aptitude": 80, "coding": 70, "technical": 90, "projects": 60, "communication": 50}
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.readinessScore").value(72.0));

        // 3. An officer creates a company the student is eligible for
        MvcResult companyResult = mockMvc.perform(post("/api/companies")
                        .with(user("officer_j").roles("PLACEMENT_OFFICER"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Journey Corp",
                                  "jobRole": "Java Developer",
                                  "packageCtc": 7.0,
                                  "minCgpa": 7.5,
                                  "requiredDepartments": "CSE",
                                  "requiredSkills": "Java, SQL",
                                  "minGraduationYear": 2026,
                                  "applicationDeadline": "2099-01-01"
                                }
                                """))
                .andExpect(status().isCreated())
                .andReturn();
        String companyId = objectMapper.readTree(companyResult.getResponse().getContentAsString())
                .get("id").asText();

        // 4. The student applies successfully
        mockMvc.perform(post("/api/applications?companyId=" + companyId)
                        .with(user("journeystu").roles("STUDENT")))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("APPLIED"))
                .andExpect(jsonPath("$.companyName").value("Journey Corp"));

        // 5. The application appears in the student's list
        mockMvc.perform(get("/api/students/applications").with(user("journeystu").roles("STUDENT")))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("Journey Corp"));
    }

    @Test
    void preparationScoresOutsideRangeAreRejected() throws Exception {
        userRepository.save(new User("rangestu", "rangestu@college.edu",
                passwordEncoder.encode("Strong@Pass1"), "Range Student", Role.STUDENT));

        mockMvc.perform(put("/api/students/preparation").with(user("rangestu").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {"aptitude": 150, "coding": 70, "technical": 90, "projects": 60, "communication": 50}
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("aptitude"));
    }

    @Test
    void invalidCgpaIsRejected() throws Exception {
        userRepository.save(new User("cgpastu", "cgpastu@college.edu",
                passwordEncoder.encode("Strong@Pass1"), "Cgpa Student", Role.STUDENT));

        String invalidProfile = PROFILE.replace("\"cgpa\": 8.8", "\"cgpa\": 12.5");
        mockMvc.perform(put("/api/students/profile").with(user("cgpastu").roles("STUDENT"))
                        .contentType(MediaType.APPLICATION_JSON).content(invalidProfile))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors[0].message").value("CGPA must be between 0 and 10"));
    }
}
