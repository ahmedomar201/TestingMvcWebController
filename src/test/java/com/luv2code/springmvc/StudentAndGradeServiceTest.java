package com.luv2code.springmvc;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


//@TestPropertySource   propertiesبتحمل الخضائص اللي موجود في
@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private StudentAndGradeService studentService;

    @Autowired
    private StudentDao studentDao;

    @BeforeEach
    public void setupDb() {
        jdbcTemplate.execute("insert into student (id, firstname, lastname, email_address)" +
                "values(1, 'ahmed1', 'omar1', 'ahmedomar1997.ao@gmail.com')");
    }

    @Test
    public void createStudentService() {

        studentService.createStudent("ahmed2", "omar2", "ahmedomar1997.aoo@gmail.com");

        CollegeStudent student = studentDao.findByEmailAddress("ahmedomar1997.aoo@gmail.com");

        assertEquals("ahmedomar1997.aoo@gmail.com", student.getEmailAddress(), "find email");

    }

    @Test
    public void isStudentNullCheck() {
        assertTrue(studentService.checkIfStudentNull(1));
        assertFalse(studentService.checkIfStudentNull(0));
    }

    @Test
    public void deleteStudentService() {

        Optional<CollegeStudent> deleteCollegeStudent = studentDao.findById(1);

        assertTrue(deleteCollegeStudent.isPresent(), "return true");
        studentService.deleteStudent(1);

        deleteCollegeStudent = studentDao.findById(1);

        assertFalse(deleteCollegeStudent.isPresent(), "return false");
    }

    @Sql("/insertData.sql")
    @Test
    public void getGradeBookService() {

        Iterable<CollegeStudent> iterableCollegeStudents = studentService.getGradeBook();

        List<CollegeStudent> collegeStudents = new ArrayList<>();

        for (CollegeStudent collegeStudent : iterableCollegeStudents) {
            collegeStudents.add(collegeStudent);
        }

        assertEquals(5, collegeStudents.size());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbcTemplate.execute("DELETE FROM student");
    }


}
