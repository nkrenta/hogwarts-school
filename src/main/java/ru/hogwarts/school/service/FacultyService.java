package ru.hogwarts.school.service;

import ru.hogwarts.school.model.Faculty;

import java.util.List;
import java.util.Map;

public interface FacultyService {

    Faculty addFaculty(Faculty faculty);

    Faculty findFaculty(Long id);

    Map<Long, List<Faculty>> getAllFaculties();

    List<Faculty> getFacultiesByColor(String color);

    Faculty editFaculty(Faculty faculty);

    void deleteFaculty(Long id);

    Faculty findFacultyByColorOrName(String color, String name);
}
