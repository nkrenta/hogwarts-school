package ru.hogwarts.school.service;

import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class FacultyServiceImpl implements  FacultyService{

    private final HashMap<Long, Faculty> facultyMap = new HashMap<>();
    private Long COUNTER = 0L;

    @Override
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++COUNTER);
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public Faculty findFaculty(Long id) {
        return facultyMap.get(id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(Long id) {
        facultyMap.remove(id);
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Faculty>> getAllFaculties() {
        Map<Long, List<Faculty>> result = new HashMap<>();
        for (Faculty faculty : facultyMap.values()) {
            result.computeIfAbsent(faculty.getId(), k -> new java.util.ArrayList<>()).add(faculty);
        }
        return result;
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        return facultyMap.values().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color) || f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
