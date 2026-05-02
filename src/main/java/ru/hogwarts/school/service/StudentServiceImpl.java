package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    Logger logger = LoggerFactory.getLogger(StudentServiceImpl.class);

    @Override
    public Student addStudent(Student student) {
        logger.info("was invoked method for create new student");
        student.setId(++COUNTER);
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public Student getStudent(Long id) {
        logger.info("was invoked method for get student by id");
        return studentMap.get(id);
    }

    @Override
    public Student editStudent(Student student) {
        logger.info("was invoked method for edit student");
        if (!studentMap.containsKey(student.getId())) {
            return null;
        }
        studentMap.put(student.getId(), student);
        return student;
    }

    @Override
    public void deleteStudent(Long id) {
        logger.info("was invoked method for delete student by id");
        studentMap.remove(id);
    }

    @Override
    public List<Student> getStudentsByAge(int age) {
        logger.info("was invoked method for get students by age");
        return studentMap.values().stream()
                .filter(student -> student.getAge() == age)
                .collect(Collectors.toList());
    }

    @Override
    public List<Student> findByAgeBetween(int min, int max) {
        logger.info("was invoked method for get students by age between");
        return studentMap.values().stream()
                .filter(student -> student.getAge() >= min && student.getAge() <= max)
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Student>> getAllStudents() {
        logger.info("was invoked method for get all students");
        Map<Long, List<Student>> result = new HashMap<>();
        for (Student student : studentMap.values()) {
            result.computeIfAbsent(student.getId(), k -> new java.util.ArrayList<>()).add(student);
        }
        return result;
    }

    //Stream-API
    @Override
    public List<String> findAllStudentsWhichNameStarts(String letter) {
        logger.info("was invoked method for get all students which name starts");
        return studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .filter(name -> name.toUpperCase().startsWith(letter.toUpperCase()))
                .sorted()
                .map(String::toUpperCase)
                .collect(Collectors.toList());
    }

    //Stream-API
    @Override
    public Integer getAverageAgeStudents() {
        logger.info("was invoked method for get average age students");
        List<Student> students = studentRepository.findAll();
        if (students.isEmpty()) {
            return 0;
        }
        return students.stream()
                .mapToInt(Student::getAge)
                .sum() / students.size();
    }

    //Threads
    @Override
    public void getStudentsPrintParallel(){
        studentRepository.findAll()
                .stream()
                .map(Student::getName)
                .limit(3)
                .forEach(System.out::println);

        new Thread(()-> {
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            studentRepository.findAll()
                    .stream()
                    .map(Student::getName)
                    .skip(3)
                    .limit(3)
                    .forEach(System.out::println);
        }).start();

        new Thread (()-> {
            try {
                Thread.sleep(2000);
            }catch (InterruptedException e){
                throw new RuntimeException(e);
            }
            studentRepository.findAll()
                    .stream()
                    .map(Student::getName)
                    .skip(6)
                    .forEach(System.out::println);
        }).start();

    }

    //Threads
    @Override
    public void getStudentsPrintSynchronized(){
       synchronized (StudentServiceImpl.class){
        getStudentsPrintParallel();
       }
    }

    @Override
    public Long getStudentsCount() {
        logger.info("was invoked method for get students count");
        return studentRepository.countAllStudents();
    }

    @Override
    public Double getStudentsAverageAge() {
        logger.info("was invoked method for get students average age");
        return studentRepository.getAverageAge();
    }

    @Override
    public List<Student> getLastFiveStudents() {
        logger.info("was invoked method for get last five students");
        return studentRepository.findLastFiveStudents();
    }

}
