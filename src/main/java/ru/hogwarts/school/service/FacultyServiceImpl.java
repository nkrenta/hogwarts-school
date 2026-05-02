package ru.hogwarts.school.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.hogwarts.school.model.Faculty;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class FacultyServiceImpl implements FacultyService {

    private final HashMap<Long, Faculty> facultyMap = new HashMap<>();
    private Long COUNTER = 0L;

    Logger logger = LoggerFactory.getLogger(FacultyServiceImpl.class);

    @Override
    public Faculty addFaculty(Faculty faculty) {
        faculty.setId(++COUNTER);
        facultyMap.put(faculty.getId(), faculty);
        logger.info("was invoked method for create new faculty");
        return faculty;
    }

    @Override
    public Faculty findFaculty(Long id) {
        logger.info("was invoked method for find faculty by id");
        return facultyMap.get(id);
    }

    @Override
    public Faculty editFaculty(Faculty faculty) {
        logger.info("was invoked method for edit faculty");
        if (!facultyMap.containsKey(faculty.getId())) {
            return null;
        }
        facultyMap.put(faculty.getId(), faculty);
        return faculty;
    }

    @Override
    public void deleteFaculty(Long id) {
        logger.info("was invoked method for delete faculty by id");
        facultyMap.remove(id);
    }

    @Override
    public List<Faculty> getFacultiesByColor(String color) {
        logger.info("was invoked method for get faculties by color");
        return facultyMap.values().stream()
                .filter(faculty -> faculty.getColor().equals(color))
                .collect(Collectors.toList());
    }

    @Override
    public Map<Long, List<Faculty>> getAllFaculties() {
        logger.info("was invoked method for get all faculties");
        Map<Long, List<Faculty>> result = new HashMap<>();
        for (Faculty faculty : facultyMap.values()) {
            result.computeIfAbsent(faculty.getId(), k -> new java.util.ArrayList<>()).add(faculty);
        }
        return result;
    }

    //Stream-API
    @Override
    public String getLongestFacultyName() {
        logger.info("was invoked method for get longest faculty name");
        return facultyMap.values()
                .stream()
                .map(Faculty::getName)
                .filter(Objects::nonNull)
                .max(Comparator.comparingInt(String::length))
                .orElse(null);
    }

    //Stream-API
    @Override
    public Integer getStreamParallelAmount(){
        logger.info("was invoked method for get stream parallel amount");
        long startTime = System.nanoTime();
        int sum = IntStream.iterate(1, a->a+1)
                .parallel()
                .limit(1_000_000)
                .reduce(0, Integer::sum);
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println("Метод выполнился за" +(duration/1_000_000)+ "мс");
        return sum;
    }

    @Override
    public Faculty findFacultyByColorOrName(String color, String name) {
        logger.info("was invoked method for find faculty by color or name");
        return facultyMap.values().stream()
                .filter(f -> f.getColor().equalsIgnoreCase(color) || f.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }
}
