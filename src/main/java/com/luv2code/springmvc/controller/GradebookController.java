package com.luv2code.springmvc.controller;

import com.luv2code.springmvc.models.*;
import com.luv2code.springmvc.service.StudentAndGradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class GradebookController {

    @Autowired
    private Gradebook gradebook;

    @Autowired
    private StudentAndGradeService studentService;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String getStudents(Model m) {
        Iterable<CollegeStudent> CollegeStudents = studentService.getGradeBook();
        m.addAttribute("students",CollegeStudents);
        return "index";
    }

    @PostMapping(value = "/")
    public  String createStudent(@ModelAttribute("student")CollegeStudent student,Model model){

        studentService.createStudent(student.getFirstname(),
                student.getLastname(),
                student.getEmailAddress());

        Iterable<CollegeStudent> CollegeStudents = studentService.getGradeBook();
        model.addAttribute("students",CollegeStudents);
        return "index";
    }

    @GetMapping("/studentInformation/{id}")
    public String studentInformation(@PathVariable int id, Model m) {
        return "studentInformation";
    }

}
