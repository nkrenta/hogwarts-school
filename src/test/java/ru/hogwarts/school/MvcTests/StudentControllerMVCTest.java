package ru.hogwarts.school.MvcTests;

import org.mockito.ArgumentMatchers;
import ru.hogwarts.school.controller.StudentController;
import ru.hogwarts.school.model.Student;
import ru.hogwarts.school.repository.StudentRepository;
import ru.hogwarts.school.service.StudentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = StudentController.class)
public class StudentControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private StudentRepository studentRepository;

    @MockBean
    private StudentService studentService;

    @InjectMocks
    private StudentController studentController;


    @Test
    public void testAddStudent() throws Exception {
        // Создаем тестовый объект Student
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(12);

        // Мокируем поведение сервиса
        when(studentService.addStudent(any(Student.class))).thenReturn(student);

        // Выполняем POST-запрос
        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.post("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(student.getId())) // Проверяем поле id
                .andExpect(jsonPath("$.name").value(student.getName())) // Проверяем поле name
                .andExpect(jsonPath("$.age").value(student.getAge())); // Проверяем поле Age

    }


    @Test
    void findStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(12);

        when(studentService.getStudent(1L)).thenReturn(student);

        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.get("/student/{id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.name").value("Harry Potter"));

    }

    @Test
    void changeStudentTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(12);

        when(studentService.editStudent(ArgumentMatchers.any())).thenReturn(student);

        ResultActions perform =
        mockMvc.perform(MockMvcRequestBuilders.put("/student")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(student)));
        perform
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Harry Potter"))
                .andExpect(jsonPath("$.age").value(12));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        Student student = new Student();
        student.setId(1L);
        student.setName("Harry Potter");
        student.setAge(12);

        when(studentService.getStudent(1L)).thenReturn(student);
        doNothing().when(studentService).deleteStudent(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/student/{id}", 1L))
                .andExpect(status().isOk());

        verify(studentService, times(1)).deleteStudent(1L);
    }

    @Test
    public void testGetStudentsByAgeBetween() throws Exception {
        // Создаем тестовые данные
        int minAge = 18;
        int maxAge = 20;

        Student student1 = new Student();
        student1.setId(1L);
        student1.setName("Harry Potter");
        student1.setAge(18);

        Student student2 = new Student();
        student2.setId(2L);
        student2.setName("Hermione Granger");
        student2.setAge(19);

        List<Student> students = Arrays.asList(student1, student2);

        // Мокируем поведение сервиса
        when(studentService.findByAgeBetween(minAge, maxAge)).thenReturn(students);

        // Выполняем GET-запрос
        mockMvc.perform(MockMvcRequestBuilders.get("/student/age-between")
                        .param("minAge", String.valueOf(minAge)) // Передаем параметр minAge
                        .param("maxAge", String.valueOf(maxAge)) // Передаем параметр maxAge
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Ожидаем статус 200 OK
                .andExpect(jsonPath("$[0].id").value(1)) // Проверяем поле id первого студента
                .andExpect(jsonPath("$[0].name").value("Harry Potter")) // Проверяем поле name первого студента
                .andExpect(jsonPath("$[0].age").value(18)) // Проверяем поле age первого студента
                .andExpect(jsonPath("$[1].id").value(2)) // Проверяем поле id второго студента
                .andExpect(jsonPath("$[1].name").value("Hermione Granger")) // Проверяем поле name второго студента
                .andExpect(jsonPath("$[1].age").value(19)); // Проверяем поле age второго студента

        // Проверяем, что метод сервиса был вызван с правильными параметрами
        verify(studentService, times(1)).findByAgeBetween(minAge, maxAge);
    }
}
