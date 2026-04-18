package ru.hogwarts.school.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.hogwarts.school.model.Faculty;
import ru.hogwarts.school.service.FacultyServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/faculty")
public class FacultyController {

    private final FacultyServiceImpl facultyServiceImpl;

    public FacultyController(FacultyServiceImpl facultyServiceImpl) {
        this.facultyServiceImpl = facultyServiceImpl;
    }

    @PostMapping
    public Faculty addFaculty(@RequestBody Faculty faculty) {
        return facultyServiceImpl.addFaculty(faculty);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Faculty> getFaculty(@PathVariable Long id) {
        if (facultyServiceImpl.findFaculty(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(facultyServiceImpl.findFaculty(id));
    }

    @GetMapping("/color/{color}")
    public ResponseEntity<List<Faculty>> getFacultiesByColor(@PathVariable String color) {
        List<Faculty> faculties = facultyServiceImpl.getFacultiesByColor(color);
        if (faculties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(faculties);
    }

//    @GetMapping("/all")
//    public Map<Long, List<Faculty>> getAllFaculties() {
//        return facultyRepository.getAllFaculties();
//    }

    @PutMapping
    public ResponseEntity<Faculty> editFaculty(@RequestBody Faculty faculty) {
        Faculty updatedFaculty = facultyServiceImpl.editFaculty(faculty);
        if (updatedFaculty == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(updatedFaculty);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Faculty> deleteFaculty(@PathVariable Long id) {
        if (facultyServiceImpl.findFaculty(id) == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        facultyServiceImpl.deleteFaculty(id);
        return ResponseEntity.ok().build();
    }


}
