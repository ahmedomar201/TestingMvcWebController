package com.luv2code.springmvc;

import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

@TestPropertySource("/application.properties")
@AutoConfigureMockMvc
@SpringBootTest
public class GradeBookControllerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private MockMvc mockMvc;

    @Mock
    private StudentAndGradeService studentService;

    @BeforeEach
    public void beforeEach(){
        jdbcTemplate.execute("insert into student (id, firstname, lastname, email_address)"+
                "values(1, 'ahmed1', 'omar1', 'ahmedomar1997.ao@gmail.com')");
    }

    @AfterEach
    public  void  setupAfterTransaction(){
        jdbcTemplate.execute("DELETE FROM student");
    }
}
