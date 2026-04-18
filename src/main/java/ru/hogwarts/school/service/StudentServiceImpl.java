package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Student;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StudentServiceImpl {

    private final HashMap<Long, Student> studentMap = new HashMap<>();
    private Long COUNTER = 0L;

    //Create
    public Student addStudent(Student student) {
        student.setId(++COUNTER);
        studentMap.put(student.getId(), student);
        return student;
    }

    //Read
    public Student getStudent(Long id) {
        return studentMap.get(id);
    }

    //Update
    public Student editStudent(Student student) {
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    //Delete
    public void deleteStudent(Long id) {
        studentMap.remove(id);
    }

    //Sorting by age
    public List<Student> getStudentsByAge(int age) {
        return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

}
