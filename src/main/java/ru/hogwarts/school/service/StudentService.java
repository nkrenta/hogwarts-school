package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Student;

import java.util.List;
import java.util.Map;

public interface StudentService {

    Student addStudent(Student student);

    Student getStudent(Long id);

    List<Student> getStudentsByAge(int age);

    List<Student> findByAgeBetween(int minAge, int maxAge);

    Map<Long, List<Student>> getAllStudents();

    void deleteStudent(Long id);

    Student editStudent(Student student);

    Long getStudentsCount();

    Double getStudentsAverageAge();

    List<Student> getLastFiveStudents();

    //Stream-API
    List<String> findAllStudentsWhichNameStarts(String letter);

    //Stream-API
    Integer getAverageAgeStudents();

}
