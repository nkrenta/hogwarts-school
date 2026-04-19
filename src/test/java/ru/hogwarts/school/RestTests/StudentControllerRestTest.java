package ru.hogwarts.school.RestTests;

import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

//@Nested
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class StudentControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private StudentController studentController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void contextLoads() {
        Assertions.assertThat(studentController).isNotNull();
    }

    private StudentRepository studentRepository;


    @Test
    public void testFindStudent() throws Exception { //тест Read запроса
        Assertions
                .assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/student", String.class))
                .isNotNull();
    }

    //создадим вспомогательный метод для создания новых студентов
    public Student createNewStudent(){
        Student student = new Student();
        student.setId(1L);
        student.setAge(20);
        student.setName("Антон");
        return student;

    }

    @Test
    public void testAddStudent() {//тест Create запроса
        //Добавляем нового студента и задаём поля
        Student student = createNewStudent();// используем вспомогательный метод добавления нового студента
        //проверяем успешность добавления студента
        Assertions
                .assertThat(this.restTemplate.postForObject("http://localhost:" + port + "/student", student, String.class))
                .isNotNull();//проверка на то что что-то приходит
    }

    @Test
    public void testChangeStudent() {//тест Update запроса
        //Добавляем нового студента и задаём поля
        Student student = createNewStudent(); // используем вспомогательный метод добавления нового студента
        ResponseEntity<Student> createResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = createResponse.getBody().getId();
        //Меняем данные студента
        Student updateStudent = new Student();
        updateStudent.setId(studentId);
        updateStudent.setAge(25);
        updateStudent.setName("Игорь");
        HttpEntity<Student> requestEntity = new HttpEntity<>(updateStudent);
        //Выполнение PUT запроса
        ResponseEntity<Student> updateResponse = restTemplate.exchange("/student", HttpMethod.PUT, requestEntity, Student.class);
        Assertions
                .assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);  //проверяем что статус после изменения 200 ОК успешно
    }

    @Test
    public void testDeleteStudent() {//тест Delete запроса
        //Добавляем нового студента
        Student student = new Student(); //создаём экземпляр класса
        student.setId(1L);
        student.setAge(20);
        student.setName("Антон");
        ResponseEntity<Student> createResponse = restTemplate.postForEntity("/student", student, Student.class);
        Long studentId = createResponse.getBody().getId();
        //Выполняем DELETE запрос
        ResponseEntity<Void> deleteResponse = restTemplate.exchange("/student/{id}", HttpMethod.DELETE, null, Void.class, studentId);
        // Проверка статуса ответа, при успешном удалении должен быть 200 OK
        Assertions
                .assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void testGetStudentsByAge() {
        //Указываем минимальный и максимальный возраст
        int minAge =20;
        int maxAge =25;

        ResponseEntity<List<Student>> response = restTemplate.exchange("http://localhost:" + port + "/student/age-between?minAge=" + minAge + "&maxAge=" + maxAge, HttpMethod.GET, null, new ParameterizedTypeReference<List<Student>>() {
        });

        //Проверяем, что статус 200 ОК
        Assertions.assertThat(response.getStatusCode().is2xxSuccessful()).isTrue();//проверяем успешность выполнения get запроса
        List<Student> students = response.getBody();//получаем список студентов
        Assertions.assertThat(students.size()!=0).isTrue();
        //Проверка на то, что все студенты находятся в заданном диапазоне возрастов
        for (Student student: students){
            Assertions.assertThat(student.getAge()>=minAge && student.getAge()<=maxAge).isTrue();
        }
    }

}




