package com.example.week5.controller;

import com.example.week5.entity.Student;
import com.example.week5.repository.StudentRepository;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import redis.clients.jedis.Jedis;

import java.util.*;

@RestController
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;
    private Jedis jedis = new Jedis();
    RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/students")
    public List<Student> getAllStudents() {
       List<Student> list = studentRepository.findAll();
       return list;
    }

    @GetMapping("/{id}")
    public Student getStudentById(@PathVariable(value = "id") Long id, HttpSession session) {
        if (session.getAttribute("token") == null) {
            // Nếu không có token, in ra lỗi và kết thúc hàm
            System.out.println("Token not found. Please login first.");
            // Sử dụng Scanner để nhập username và password từ người dùng
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject("http://localhost:9090/login", requestEntity, String.class);
                // Lưu token vào session
                session.setAttribute("token", response);
                // Thực hiện tìm sinh viên sau khi xác thực thành công
                String key = String.valueOf(id);
                if (jedis.exists(key)) {
                    Student studentCash = new Student();
                    studentCash.setId(id);

                    // Lấy từ Redis dưới dạng hash
                    Map<String, String> studentData = jedis.hgetAll(key);

                    // Set các thuộc tính cho đối tượng Student từ giá trị lấy từ Redis
                    studentCash.setName(studentData.get("name"));
                    studentCash.setAge(Integer.parseInt(studentData.get("age")));
                    studentCash.setCourse(studentData.get("course"));

                    return studentCash;
                } else {
                    Student product = studentRepository.findById(id)
                            .orElseThrow(() -> new RuntimeException("Student_id " + id + " not found"));

                    // Lưu từng trường và giá trị vào Redis dưới dạng hash
                    jedis.hset(key, "name", product.getName());
                    jedis.hset(key, "age", String.valueOf(product.getAge()));
                    jedis.hset(key, "course", product.getCourse());

                    System.out.println("Saved in cache");
                    return product;
                }
            } catch (HttpClientErrorException.BadRequest ex) {
                System.out.println("Login failed: Bad Request");
                return null;
            }
        }

        // Nếu có token trong session, tiếp tục thêm sinh viên
        String token = (String) session.getAttribute("token");

        // Tạo một HttpHeaders object và thêm token vào header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Thêm sinh viên sau khi đã xác thực thành công
        String key = String.valueOf(id);
        if (jedis.exists(key)) {
            Student studentCash = new Student();
            studentCash.setId(id);

            // Lấy từ Redis dưới dạng hash
            Map<String, String> studentData = jedis.hgetAll(key);

            // Set các thuộc tính cho đối tượng Student từ giá trị lấy từ Redis
            studentCash.setName(studentData.get("name"));
            studentCash.setAge(Integer.parseInt(studentData.get("age")));
            studentCash.setCourse(studentData.get("course"));

            return studentCash;
        } else {
            Student product = studentRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Student_id " + id + " not found"));

            // Lưu từng trường và giá trị vào Redis dưới dạng hash
            jedis.hset(key, "name", product.getName());
            jedis.hset(key, "age", String.valueOf(product.getAge()));
            jedis.hset(key, "course", product.getCourse());

            System.out.println("Saved in cache");
            return product;
        }
    }





    @PostMapping("/students")
    public Student saveStudent(@RequestBody Student student, HttpSession session) {
        if (session.getAttribute("token") == null) {
            // Nếu không có token, in ra lỗi và kết thúc hàm
            System.out.println("Token not found. Please login first.");
            // Sử dụng Scanner để nhập username và password từ người dùng
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject("http://localhost:9090/login", requestEntity, String.class);
                // Lưu token vào session
                session.setAttribute("token", response);
                // Thực hiện thêm sinh viên sau khi xác thực thành công
                Student savedStudent = studentRepository.save(student);
                System.out.println("save thành công");
                return studentRepository.save(student);
            } catch (HttpClientErrorException.BadRequest ex) {
                System.out.println("Login failed: Bad Request");
                return null;
            }
        }

        // Nếu có token trong session, tiếp tục thêm sinh viên
        String token = (String) session.getAttribute("token");

        // Tạo một HttpHeaders object và thêm token vào header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Thêm sinh viên sau khi đã xác thực thành công
        Student savedStudent = studentRepository.save(student);
        return studentRepository.save(student);
    }



    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable(value = "id") Long id, @RequestBody Student student, HttpSession session) {
        if (session.getAttribute("token") == null) {
            // Nếu không có token, in ra lỗi và kết thúc hàm
            System.out.println("Token not found. Please login first.");
            // Sử dụng Scanner để nhập username và password từ người dùng
            Scanner scanner = new Scanner(System.in);

            System.out.print("Enter username: ");
            String username = scanner.nextLine();

            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String requestBody = "{\"username\": \"" + username + "\", \"password\": \"" + password + "\"}";
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            try {
                String response = restTemplate.postForObject("http://localhost:9090/login", requestEntity, String.class);
                // Lưu token vào session
                session.setAttribute("token", response);
                Student studentUpdate = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
                studentUpdate.setName(student.getName());
                studentUpdate.setAge(student.getAge());
                studentUpdate.setCourse(student.getCourse());
                studentRepository.save(studentUpdate);
                System.out.println("Update thành công");
            } catch (HttpClientErrorException.BadRequest ex) {
                System.out.println("Login failed: Bad Request");
                return null;
            }
        }

        // Nếu có token trong session, tiếp tục cập nhật sinh viên
        String token = (String) session.getAttribute("token");

        // Tạo một HttpHeaders object và thêm token vào header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + token);

        // Cập nhật sinh viên sau khi đã xác thực thành công
        Student studentUpdate = studentRepository.findById(id).orElseThrow(() -> new RuntimeException("Student not found"));
        studentUpdate.setName(student.getName());
        studentUpdate.setAge(student.getAge());
        studentUpdate.setCourse(student.getCourse());
        studentRepository.save(studentUpdate);

        return studentRepository.save(studentUpdate);
    }

    @DeleteMapping("/{id}")
    public void deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
    }
}
