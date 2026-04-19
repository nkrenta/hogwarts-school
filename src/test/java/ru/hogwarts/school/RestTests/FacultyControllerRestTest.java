package ru.hogwarts.school.RestTests;

import org.junit.jupiter.api.BeforeEach;
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
import org.springframework.test.annotation.DirtiesContext;
import ru.hogwarts.school.controller.FacultyController;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.repository.FacultyRepository;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class FacultyControllerRestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private FacultyController facultyController;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FacultyRepository facultyRepository;

    private String baseUrl;

    @BeforeEach
    public void setUp() {
        baseUrl = "http://localhost:" + port + "/faculty";
    }

    @Test
    void contextLoads() {
        assertThat(facultyController).isNotNull();
    }

    // Вспомогательный метод для создания факультета
    private Faculty createFaculty(String name, String color) {
        Faculty faculty = new Faculty();
        faculty.setName(name);
        faculty.setColor(color);
        return faculty;
    }

    @Test
    public void testAddFaculty() {
        // Создаём факультет
        Faculty faculty = createFaculty("Gryffindor", "red");

        // Отправляем POST запрос
        ResponseEntity<Faculty> response = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor");
        assertThat(response.getBody().getColor()).isEqualTo("red");
    }

    @Test
    public void testGetFacultyById() {
        // Создаём факультет
        Faculty faculty = createFaculty("Slytherin", "green");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
        Long facultyId = createResponse.getBody().getId();

        // Получаем факультет по ID
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/" + facultyId, Faculty.class);

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
        assertThat(response.getBody().getName()).isEqualTo("Slytherin");
        assertThat(response.getBody().getColor()).isEqualTo("green");
    }

    @Test
    public void testGetFacultyByIdNotFound() {
        // Пытаемся получить несуществующий факультет
        ResponseEntity<Faculty> response = restTemplate.getForEntity(baseUrl + "/999999", Faculty.class);

        // Проверяем что возвращается 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetFacultiesByColor() {
        // Создаём факультеты с одинаковым цветом
        Faculty faculty1 = createFaculty("Gryffindor", "red");
        Faculty faculty2 = createFaculty("Hufflepuff", "yellow");
        restTemplate.postForEntity(baseUrl, faculty1, Faculty.class);
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        // Получаем факультеты по цвету
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/color/red",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).getName()).isEqualTo("Gryffindor");
    }

    @Test
    public void testGetFacultiesByColorNotFound() {
        // Пытаемся получить факультеты по несуществующему цвету
        ResponseEntity<List<Faculty>> response = restTemplate.exchange(
                baseUrl + "/color/nonexistent",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Проверяем что возвращается 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testGetAllFaculties() {
        // Создаём несколько факультетов
        Faculty faculty1 = createFaculty("Gryffindor", "red");
        Faculty faculty2 = createFaculty("Slytherin", "green");
        restTemplate.postForEntity(baseUrl, faculty1, Faculty.class);
        restTemplate.postForEntity(baseUrl, faculty2, Faculty.class);

        // Получаем все факультеты
        ResponseEntity<Map<Long, List<Faculty>>> response = restTemplate.exchange(
                baseUrl + "/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {}
        );

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void testFindFacultyByColor() {
        // Создаём факультет
        Faculty faculty = createFaculty("Ravenclaw", "blue");
        restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        // Ищем факультет по цвету
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/colorOrName?color=blue",
                Faculty.class
        );

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Ravenclaw");
        assertThat(response.getBody().getColor()).isEqualTo("blue");
    }

    @Test
    public void testFindFacultyByName() {
        // Создаём факультет
        Faculty faculty = createFaculty("Hufflepuff", "yellow");
        restTemplate.postForEntity(baseUrl, faculty, Faculty.class);

        // Ищем факультет по имени
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/colorOrName?name=Hufflepuff",
                Faculty.class
        );

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Hufflepuff");
        assertThat(response.getBody().getColor()).isEqualTo("yellow");
    }

    @Test
    public void testFindFacultyByColorOrNameNotFound() {
        // Ищем несуществующий факультет
        ResponseEntity<Faculty> response = restTemplate.getForEntity(
                baseUrl + "/colorOrName?color=nonexistent",
                Faculty.class
        );

        // Проверяем что возвращается 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testEditFaculty() {
        // Создаём факультет
        Faculty faculty = createFaculty("Gryffindor", "red");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
        Long facultyId = createResponse.getBody().getId();

        // Обновляем факультет
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(facultyId);
        updatedFaculty.setName("Gryffindor Updated");
        updatedFaculty.setColor("scarlet");
        HttpEntity<Faculty> request = new HttpEntity<>(updatedFaculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        // Проверяем результат
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(facultyId);
        assertThat(response.getBody().getName()).isEqualTo("Gryffindor Updated");
        assertThat(response.getBody().getColor()).isEqualTo("scarlet");
    }

    @Test
    public void testEditFacultyNotFound() {
        // Пытаемся обновить несуществующий факультет
        Faculty updatedFaculty = new Faculty();
        updatedFaculty.setId(999999L);
        updatedFaculty.setName("Nonexistent");
        updatedFaculty.setColor("black");
        HttpEntity<Faculty> request = new HttpEntity<>(updatedFaculty);

        ResponseEntity<Faculty> response = restTemplate.exchange(
                baseUrl,
                HttpMethod.PUT,
                request,
                Faculty.class
        );

        // Проверяем что возвращается 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteFaculty() {
        // Создаём факультет
        Faculty faculty = createFaculty("Gryffindor", "red");
        ResponseEntity<Faculty> createResponse = restTemplate.postForEntity(baseUrl, faculty, Faculty.class);
        Long facultyId = createResponse.getBody().getId();

        // Удаляем факультет
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + facultyId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Проверяем что удаление успешно (возвращается 200 OK)
        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Проверяем что факультет действительно удалён
        ResponseEntity<Faculty> getResponse = restTemplate.getForEntity(baseUrl + "/" + facultyId, Faculty.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void testDeleteFacultyNotFound() {
        // Пытаемся удалить несуществующий факультет
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/999999",
                HttpMethod.DELETE,
                null,
                Void.class
        );

        // Проверяем что возвращается 404
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
