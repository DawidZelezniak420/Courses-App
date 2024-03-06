package com.zelezniak.project.user;


import com.zelezniak.project.author.AuthorService;
import com.zelezniak.project.author.CourseAuthor;
import com.zelezniak.project.student.Student;
import com.zelezniak.project.student.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

import static com.zelezniak.project.controller.AttributesAndTemplatesNames.*;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final StudentService studentService;
    private final AuthorService authorService;

    @GetMapping("/all/students")
    public ModelAndView allStudents() {
        ModelAndView modelAndView = new ModelAndView(STUDENTS_LIST_VIEW);
        List<Student> allStudents = studentService.getAllStudents();
        modelAndView.addObject(STUDENTS_ATTRIBUTE, allStudents);
        return modelAndView;
    }

    @GetMapping("/all/authors")
    public ModelAndView allAuthors() {
        ModelAndView modelAndView = new ModelAndView(AUTHORS_LIST_VIEW);
        List<CourseAuthor> allAuthors = authorService.getAllAuthors();
        modelAndView.addObject(AUTHORS_ATTRIBUTE, allAuthors);
        return modelAndView;
    }
}