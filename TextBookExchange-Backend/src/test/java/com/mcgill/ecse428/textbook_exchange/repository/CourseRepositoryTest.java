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

import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;

@SpringBootTest
public class CourseRepositoryTest {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private FacultyRepository facultyRepository;

    @Autowired
    private InstitutionRepository institutionRepository;

    @BeforeEach
    @AfterEach
    public void clearDatabase() {
        courseRepository.deleteAll();
        facultyRepository.deleteAll();
        institutionRepository.deleteAll();
    }

    @Test
    public void testSaveCourse() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        // Create and save faculty
        String departmentName = "Computer Science";
        Faculty faculty = new Faculty(departmentName, institution);
        faculty = facultyRepository.save(faculty);

        // Create and save the course
        String courseId = "ECSE428";
        Course course = new Course(courseId, faculty);
        course = courseRepository.save(course);

        Course retrievedCourse = courseRepository.findById(course.getCourseId()).orElse(null);

        assertNotNull(retrievedCourse);
        assertEquals(courseId, retrievedCourse.getCourseId());
        assertEquals(faculty.getDepartmentName(), retrievedCourse.getFaculty().getDepartmentName());
    }

    @Test
    public void testFindCourseByCourseId() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        // Create and save faculty
        String departmentName = "Computer Science";
        Faculty faculty = new Faculty(departmentName, institution);
        faculty = facultyRepository.save(faculty);

        // Create and save the course
        String courseId = "ECSE428";
        Course course = new Course(courseId, faculty);
        courseRepository.save(course);

        Course retrievedCourse = courseRepository.findByCourseId(courseId);
        assertNotNull(retrievedCourse);
        assertEquals(courseId, retrievedCourse.getCourseId());
    }

    @Test
    public void testFindCoursesByFaculty() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        // Create and save two faculties
        Faculty cseFaculty = new Faculty("Computer Science", institution);
        Faculty mathFaculty = new Faculty("Mathematics", institution);
        cseFaculty = facultyRepository.save(cseFaculty);
        mathFaculty = facultyRepository.save(mathFaculty);

        // Create and save courses for CSE faculty
        Course course1 = new Course("ECSE428", cseFaculty);
        Course course2 = new Course("ECSE429", cseFaculty);
        courseRepository.save(course1);
        courseRepository.save(course2);

        // Create and save course for Math faculty
        Course course3 = new Course("MATH240", mathFaculty);
        courseRepository.save(course3);

        // Test finding courses by faculty
        ArrayList<Course> cseCourses = new ArrayList<>(courseRepository.findByFaculty(cseFaculty));
        assertNotNull(cseCourses);
        assertEquals(2, cseCourses.size());

        ArrayList<Course> mathCourses = new ArrayList<>(courseRepository.findByFaculty(mathFaculty));
        assertNotNull(mathCourses);
        assertEquals(1, mathCourses.size());
    }

    @Test
    public void testFindAllCourses() {
        // Create and save institution first
        Institution institution = new Institution("McGill University");
        institution = institutionRepository.save(institution);

        // Create and save faculty
        String departmentName = "Computer Science";
        Faculty faculty = new Faculty(departmentName, institution);
        faculty = facultyRepository.save(faculty);

        // Create and save multiple courses
        Course course1 = new Course("ECSE428", faculty);
        Course course2 = new Course("ECSE429", faculty);
        Course course3 = new Course("ECSE415", faculty);
        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        // Test finding all courses
        Iterable<Course> courses = courseRepository.findAll();
        ArrayList<Course> coursesList = StreamSupport.stream(courses.spliterator(), false)
                                                   .collect(Collectors.toCollection(ArrayList::new));

        assertNotNull(coursesList);
        assertEquals(3, coursesList.size());
    }
}
