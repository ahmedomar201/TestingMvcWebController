package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.ModelAndViewAssert;
import org.springframework.test.web.servlet.MockMvc;
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

    private static MockHttpServletRequest request;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private StudentDao studentDao;


    //inject the mock mvc
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentCreateServiceMock;

    @BeforeAll
    public static void setup() {
        request = new MockHttpServletRequest();
        request.setParameter("firstname", "ahmed");
        request.setParameter("lastname", "omar");
        request.setParameter("emailAddress", "ahmedomar1997.aoo@gmail.com");
    }

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
        when(studentCreateServiceMock.getGradeBook()).thenReturn(collegeStudentList);
        //assert result
        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradeBook());

        //perform web request expectation for status ok
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/")).
                andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

    }


    @Test
    //perform we request http
    public void createStudentsHttpRequest() throws Exception {

        CollegeStudent studentOne = new CollegeStudent("osama", "mohamed",
                "ahmedomar1999.aoo@gmail.com");

        List<CollegeStudent> collegeStudentList = new ArrayList<>(Arrays.asList(studentOne, studentOne));

        when(studentCreateServiceMock.getGradeBook()).thenReturn(collegeStudentList);

        assertIterableEquals(collegeStudentList, studentCreateServiceMock.getGradeBook());

        MvcResult mvcResult = this.mockMvc.perform(MockMvcRequestBuilders.post("/")
                .contentType(MediaType.APPLICATION_JSON)
                .param("firstname", request.getParameterValues("firstname"))
                .param("lastname", request.getParameterValues("lastname"))
                .param("emailAddress", request.getParameterValues("emailAddress")))
                .andExpect(status().isOk()).andReturn();

        ModelAndView mav = mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav, "index");

        CollegeStudent verifyStudent=studentDao.findByEmailAddress("ahmedomar1997.aoo@gmail.com");

        assertNotNull(verifyStudent,"Student should be found");
    }

    @Test
    public void deleteStudentHttpRequest()throws Exception{

        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
                .get("/delete/student/{id}",0)).
                andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"error");


    }


    //ده الكزد لما ادوس علي delete بيوديني لصفحة delete
    @Test
    public void deleteStudentHttpRequestErrorPage()throws Exception{

        assertTrue(studentDao.findById(1).isPresent());
        MvcResult mvcResult=mockMvc.perform(MockMvcRequestBuilders
                        .get("/delete/student/{id}",1)).
                andExpect(status().isOk()).andReturn();

        ModelAndView mav=mvcResult.getModelAndView();

        ModelAndViewAssert.assertViewName(mav,"index");

        assertFalse(studentDao.findById(1).isPresent());

    }


    @AfterEach
    public void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
    }
}
