package com.example.week5.controller;

import com.example.week5.entity.Student;
import com.example.week5.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
    @RestController
    @RequestMapping("/api/students")
    public class StudentController {

        @Autowired
        private StudentRepository studentRepository;

        @GetMapping()
        public List<Student> getAllStudents() {
            return studentRepository.findAll();
        }

        @GetMapping("/{id}")
        public Student getStudentById(@PathVariable Long id) {
            return studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        }

        @PostMapping
        public Student addStudent(@RequestBody Student student) {
            return studentRepository.save(student);
        }

        @PutMapping("/{id}")
        public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
            return studentRepository.findById(id)
                    .map(student -> {
                        student.setName(updatedStudent.getName());
                        student.setAge(updatedStudent.getAge());
                        student.setCourse(updatedStudent.getCourse());
                        return studentRepository.save(student);
                    })
                    .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
        }

        @DeleteMapping("/{id}")
        public void deleteStudent(@PathVariable Long id) {
            studentRepository.deleteById(id);
        }
}
