package ru.hogwarts.school.MvcTests;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;
import ru.hogwarts.school.service.FacultyService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FacultyController.class)
public class FacultyControllerMVCTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FacultyRepository facultyRepository;

    @MockBean
    private FacultyService facultyService;

    @InjectMocks
    private FacultyController facultyController;

    @Test
    public void testAddFaculty() throws Exception {
        // Создаем тестовый объект Faculty
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("Red");

        // Мокируем поведение сервиса
        Mockito.when(facultyService.addFaculty(Mockito.any(Faculty.class))).thenReturn(faculty);

        // Выполняем POST-запрос
        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.post("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)));

        perform
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(faculty.getId())) // Проверяем поле id
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(faculty.getName())) // Проверяем поле name
                .andExpect(MockMvcResultMatchers.jsonPath("$.color").value(faculty.getColor())); // Проверяем поле color

    }


    @Test
    void findFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        Mockito.when(facultyService.findFaculty(1L)).thenReturn(faculty);

        ResultActions perform =
                mockMvc.perform(MockMvcRequestBuilders.get("/faculty/{id}", 1L))
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.id").value(1L))
                        .andExpect(jsonPath("$.color").value("red"));

    }

    @Test
    void changeFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        when(facultyService.editFaculty(any())).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.put("/faculty")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(faculty)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gryffindor"))
                .andExpect(jsonPath("$.color").value("red"));
    }

    @Test
    void deleteFacultyTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        when(facultyService.findFaculty(1L)).thenReturn(faculty);
        doNothing().when(facultyService).deleteFaculty(1L);

        mockMvc.perform(MockMvcRequestBuilders.delete("/faculty/{id}", 1L))
                .andExpect(status().isOk());

        verify(facultyService, times(1)).deleteFaculty(1L);
    }

    @Test
    void getFacultiesByColorTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");

        when(facultyService.getFacultiesByColor("red")).thenReturn(List.of(faculty));

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/color/{color}", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Gryffindor"))
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void getFacultyByColorOrNameTest() throws Exception {
        Faculty faculty = new Faculty();
        faculty.setId(1L);
        faculty.setName("Gryffindor");
        faculty.setColor("red");


        when(facultyService.findFacultyByColorOrName("red", null)).thenReturn(faculty);

        mockMvc.perform(MockMvcRequestBuilders.get("/faculty/colorOrName")
                        .param("color", "red"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(facultyService).findFacultyByColorOrName("red", null);
    }
}