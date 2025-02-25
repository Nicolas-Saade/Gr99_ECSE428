package com.mcgill.ecse428.textbook_exchange.repository;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;

@SpringBootTest
public class FacultyRepositoryTest {

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        facultyRepository.deleteAll();
        institutionRepository.deleteAll();
    }

    @Test
    public void testSaveFaculty() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        String departmentName = "Computer Science";
        Faculty faculty = new Faculty(departmentName, institution);
        faculty = facultyRepository.save(faculty);

        Faculty retrievedFaculty = facultyRepository.findByDepartmentName(departmentName);
        assertNotNull(retrievedFaculty);
        assertEquals(departmentName, retrievedFaculty.getDepartmentName());
        assertEquals(institution.getInstitutionName(), retrievedFaculty.getInstitution().getInstitutionName());
    }

    @Test
    public void testFindFacultyByDepartmentName() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        String departmentName = "Computer Science";
        Faculty faculty = new Faculty(departmentName, institution);
        facultyRepository.save(faculty);

        Faculty retrievedFaculty = facultyRepository.findByDepartmentName(departmentName);
        assertNotNull(retrievedFaculty);
        assertEquals(departmentName, retrievedFaculty.getDepartmentName());
    }

    @Test
    public void testFindAllFaculties() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        // Create and save multiple faculties
        Faculty faculty1 = new Faculty("Computer Science", institution);
        Faculty faculty2 = new Faculty("Mathematics", institution);
        Faculty faculty3 = new Faculty("Physics", institution);
        
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);
        facultyRepository.save(faculty3);

        Iterable<Faculty> faculties = facultyRepository.findAll();
        ArrayList<Faculty> facultiesList = StreamSupport.stream(faculties.spliterator(), false)
                                                      .collect(Collectors.toCollection(ArrayList::new));

        System.out.println("Facultities: " + facultiesList);

        assertNotNull(facultiesList);
        assertEquals(3, facultiesList.size());
    }

    @Test
    public void testFindFacultiesByInstitution() {
        // Create and save two different institutions
        Institution mcgill = new Institution("McGill University");
        Institution concordia = new Institution("Concordia University");
        mcgill = institutionRepository.save(mcgill);
        concordia = institutionRepository.save(concordia);

        // Create faculties for McGill
        Faculty faculty1 = new Faculty("Computer Science", mcgill);
        Faculty faculty2 = new Faculty("Mathematics", mcgill);
        facultyRepository.save(faculty1);
        facultyRepository.save(faculty2);

        // Create faculty for Concordia
        Faculty faculty3 = new Faculty("Engineering", concordia);
        facultyRepository.save(faculty3);

        // Test finding faculties by institution
        ArrayList<Faculty> facultiesList = new ArrayList<>(facultyRepository.findByInstitution(mcgill));
        assertNotNull(facultiesList);
        assertEquals(2, facultiesList.size());
    }
}
