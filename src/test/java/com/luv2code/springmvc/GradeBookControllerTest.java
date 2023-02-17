package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestPropertySource("/application.properties")
// add  @AutoConfigureMockMv
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    //inject the mock mvc
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentService;

    @BeforeEach
    public void beforeEach() {
        jdbcTemplate.execute("insert into student (id, firstname, lastname, email_address)" +
                "values(1, 'ahmed1', 'omar1', 'ahmedomar1997.ao@gmail.com')");
    }

    @Test
    //perform we request http
    public void getStudentsHttpRequest() throws Exception {
        CollegeStudent studentOne = new GradebookCollegeStudent("ahmed20", "omar20",
                "ahmedomar1997.aoo20@gmail.com");

        CollegeStudent studentTwo = new GradebookCollegeStudent("ahmed20", "omar20",
                "ahmedomar1997.aoo20@gmail.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentTwo));
        // define expectation
        when(studentService.getGradeBook()).thenReturn(collegeStudentList);
        //assert result
        assertIterableEquals(collegeStudentList, studentService.getGradeBook());

        //perform web request expectation for status ok
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders.get("/")).
                andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");

    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
    }
}
