package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl implements StudentService {

    private final HashMap<Long, Student> studentMap = new HashMap<>();
    private Long COUNTER = 0L;
    private final StudentRepository studentRepository;

    public StudentServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Student addStudent(Student student) {
        student.setId(++COUNTER);
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public Student getStudent(Long id) {
        return studentMap.get(id);
    }

    @Override
    public Student editStudent(Student student) {
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        studentMap.remove(id);
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        return studentMap.values().stream()
                .filter(student -> student.getAge() >= min && student.getAge() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Student>> getAllStudents() {
        Map<Long, List<Student>> result = new HashMap<>();
        for (Student student : studentMap.values()) {
            result.computeIfAbsent(student.getId(), k -> new java.util.ArrayList<>()).add(student);
        }
        return result;
    }

    @Override
    public Long getStudentsCount() {
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getStudentsAverageAge() {
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> getLastFiveStudents() {
        return studentRepository.findLastFiveStudents();
    }

}
