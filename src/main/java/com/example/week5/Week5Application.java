package com.example.week5;

import com.example.week5.entity.Student;
import com.example.week5.repository.StudentRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Week5Application {

    public static void main(String[] args) {
        SpringApplication.run(Week5Application.class, args);
    }

//    @Bean
//    public CommandLineRunner dataLoader(StudentRepository studentRepository) {
//        return args -> {
//            // Thêm dữ liệu ban đầu vào cơ sở dữ liệu
//            Student student1 = new Student();
//            student1.setName("John Doe");
//            student1.setAge(20);
//            student1.setCourse("Computer Science");
//            studentRepository.save(student1);
//
//            Student student2 = new Student();
//            student2.setName("Jane Smith");
//            student2.setAge(22);
//            student2.setCourse("Mathematics");
//            studentRepository.save(student2);
//        };
//    }
}
