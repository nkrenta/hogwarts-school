package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl {

    private final HashMap<Long, Faculty> facultyMap = new HashMap<>();
    private Long COUNTER = 0L;

    //Create
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++COUNTER);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    //Read
    public Faculty findFaculty(Long id) {
        return facultyMap.get(id);
    }

    //Update
    public Faculty editFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    //Delete
    public void deleteFaculty(Long id) {
        facultyMap.remove(id);
    }

    //Sorting by color
    public List<Faculty> getFacultiesByColor(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }

}
