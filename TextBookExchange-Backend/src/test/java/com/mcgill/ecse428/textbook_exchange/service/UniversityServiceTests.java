package com.mcgill.ecse428.textbook_exchange.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.FacultyRepository;
import com.mcgill.ecse428.textbook_exchange.repository.InstitutionRepository;

@SpringBootTest
@MockitoSettings(strictness = Strictness.STRICT_STUBS)
public class UniversityServiceTests {

    // Test constants for Institution
    private static final String VALID_INSTITUTION = "McGill University";
    private static final String VALID_INSTITUTION_NEW = "Concordia University";
    private static final String INVALID_EMPTY = "";
    private static final String INVALID_NULL = null;
    
    // Test constants for Faculty
    private static final String VALID_DEPARTMENT = "Computer Science";
    private static final String VALID_DEPARTMENT_NEW = "Software Engineering";
    
    // Test constants for Course
    private static final String VALID_COURSE = "ECSE428";
    private static final String VALID_COURSE_NEW = "ECSE429";
    
    @Mock
    private InstitutionRepository mockInstitutionRepository;
    
    @Mock
    private FacultyRepository mockFacultyRepository;
    
    @Mock
    private CourseRepository mockCourseRepository;
    
    @InjectMocks
    private UniversityService universityService;
    
    
    //====================== Institution Tests ======================
    
    @Test
    public void testCreateValidInstitution() {
        // Arrange: No institution exists with this name.
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(null);
        when(mockInstitutionRepository.save(any(Institution.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act:
        Institution institution = universityService.createInstitution(VALID_INSTITUTION);
        
        // Assert:
        assertNotNull(institution);
        assertEquals(VALID_INSTITUTION, institution.getInstitutionName());
        verify(mockInstitutionRepository, times(1)).save(institution);
    }
    
    @Test
    public void testCreateInstitutionWithEmptyName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createInstitution(INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockInstitutionRepository, never()).save(any(Institution.class));
    }
    
    @Test
    public void testCreateInstitutionWithNullName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createInstitution(INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockInstitutionRepository, never()).save(any(Institution.class));
    }
    
    @Test
    public void testCreateInstitutionWithDuplicateName() {
        // Arrange: An institution with this name already exists.
        Institution existing = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(existing);
        
        // Act & Assert:
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createInstitution(VALID_INSTITUTION));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution already exists", exception.getMessage());
        verify(mockInstitutionRepository, never()).save(any(Institution.class));
    }
    
    @Test
    public void testGetInstitutionSuccess() {
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        
        Institution result = universityService.getInstitution(VALID_INSTITUTION);
        assertNotNull(result);
        assertEquals(VALID_INSTITUTION, result.getInstitutionName());
        verify(mockInstitutionRepository, times(1)).findByInstitutionName(VALID_INSTITUTION);
    }
    
    @Test
    public void testGetInstitutionWithEmptyName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getInstitution(INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
    }
    @Test
    public void testGetInstitutionWithNullName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getInstitution(INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
    }
    
    @Test
    public void testGetInstitutionNotFound() {
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getInstitution(VALID_INSTITUTION));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Institution not found", exception.getMessage());
        verify(mockInstitutionRepository, times(1)).findByInstitutionName(VALID_INSTITUTION);
    }
    
    @Test
    public void testUpdateInstitutionSuccess() {
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        when(mockInstitutionRepository.save(any(Institution.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        Institution updated = universityService.updateInstitution(VALID_INSTITUTION, VALID_INSTITUTION_NEW);
        assertNotNull(updated);
        assertEquals(VALID_INSTITUTION_NEW, updated.getInstitutionName());
    }
    
    @Test
    public void testUpdateInstitutionWithEmptyNewName() {
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.updateInstitution(VALID_INSTITUTION, INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockInstitutionRepository, never()).save(any(Institution.class));
    }
    @Test
    public void testUpdateInstitutionWithNullNewName() {
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.updateInstitution(VALID_INSTITUTION, INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockInstitutionRepository, never()).save(any(Institution.class));
    }
    
    @Test
    public void testDeleteInstitutionSuccess() {
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        
        universityService.deleteInstitution(VALID_INSTITUTION);
        verify(mockInstitutionRepository, times(1)).delete(institution);
    }
    
    
    //====================== Faculty Tests ======================
    
    @Test
    public void testCreateValidFaculty() {
        // Arrange:
        Institution institution = new Institution(VALID_INSTITUTION);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
        when(mockFacultyRepository.save(any(Faculty.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act:
        Faculty faculty = universityService.createFaculty(VALID_DEPARTMENT, VALID_INSTITUTION);
        
        // Assert:
        assertNotNull(faculty);
        assertEquals(VALID_DEPARTMENT, faculty.getDepartmentName());
        assertEquals(institution, faculty.getInstitution());
        verify(mockFacultyRepository, times(1)).save(faculty);
    }
    
    @Test
    public void testCreateFacultyWithEmptyDepartmentName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createFaculty(INVALID_EMPTY, VALID_INSTITUTION));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Department name cannot be empty", exception.getMessage());
        verify(mockFacultyRepository, never()).save(any(Faculty.class));
    }
    @Test
    public void testCreateFacultyWithNullDepartmentName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createFaculty(INVALID_NULL, VALID_INSTITUTION));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Department name cannot be empty", exception.getMessage());
        verify(mockFacultyRepository, never()).save(any(Faculty.class));
    }
    @Test
    public void testCreateFacultyWithEmptyInstitutionName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createFaculty(VALID_DEPARTMENT, INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockFacultyRepository, never()).save(any(Faculty.class));
    }
    @Test
    public void testCreateFacultyWithNullInstitutionName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createFaculty(VALID_DEPARTMENT, INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Institution name cannot be empty", exception.getMessage());
        verify(mockFacultyRepository, never()).save(any(Faculty.class));
    }
    
    @Test
    public void testGetFacultySuccess() {
        Faculty faculty = new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION));
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        
        Faculty result = universityService.getFaculty(VALID_DEPARTMENT);
        assertNotNull(result);
        assertEquals(VALID_DEPARTMENT, result.getDepartmentName());
        verify(mockFacultyRepository, times(1)).findByDepartmentName(VALID_DEPARTMENT);
    }
    
    @Test
    public void testGetFacultyWithEmptyDepartmentName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getFaculty(INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Department name cannot be empty", exception.getMessage());
    }
    @Test
    public void testGetFacultyWithNullDepartmentName() {
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getFaculty(INVALID_NULL));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Department name cannot be empty", exception.getMessage());
    }
    
    @Test
    public void testGetFacultyNotFound() {
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getFaculty(VALID_DEPARTMENT));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Faculty not found", exception.getMessage());
        verify(mockFacultyRepository, times(1)).findByDepartmentName(VALID_DEPARTMENT);
    }
    @Test 
    public void testGetAllFaculties() {
        Institution institution = new Institution(VALID_INSTITUTION);
        Faculty faculty1 = new Faculty("Computer Science",institution);
        Faculty faculty2 = new Faculty("Software Engineering",institution);
        when(mockInstitutionRepository.findByInstitutionName(VALID_INSTITUTION)).thenReturn(institution);
       
        when(mockFacultyRepository.findByInstitution(institution)).thenReturn(Arrays.asList(faculty1, faculty2));

        List<Faculty> faculties = universityService.getAllFaculties(VALID_INSTITUTION);
        assertNotNull(faculties);
        assertEquals(2, faculties.size());
        assertTrue(faculties.contains(faculty1));
        assertTrue(faculties.contains(faculty2));

    }
    
    @Test
    public void testUpdateFacultySuccess() {
        Faculty faculty = new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION));
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        when(mockFacultyRepository.save(any(Faculty.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        Faculty updated = universityService.updateFaculty(VALID_DEPARTMENT, VALID_DEPARTMENT_NEW);
        assertNotNull(updated);
        assertEquals(VALID_DEPARTMENT_NEW, updated.getDepartmentName());
    }
    
    @Test
    public void testUpdateFacultyWithEmptyNewName() {
        Faculty faculty = new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION));
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.updateFaculty(VALID_DEPARTMENT, INVALID_EMPTY));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Department name cannot be empty", exception.getMessage());
        verify(mockFacultyRepository, never()).save(any(Faculty.class));
    }
    
    @Test
    public void testDeleteFacultySuccess() {
        Faculty faculty = new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION));
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        
        universityService.deleteFaculty(VALID_DEPARTMENT);
        verify(mockFacultyRepository, times(1)).delete(faculty);
    }
    
    
    //====================== Course Tests ======================
    
    @Test
    public void testCreateValidCourse() {
        // Arrange:
        Institution institution = new Institution(VALID_INSTITUTION);
        Faculty faculty = new Faculty(VALID_DEPARTMENT, institution);
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        when(mockCourseRepository.save(any(Course.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        // Act:
        Course course = universityService.createCourse(VALID_COURSE, VALID_DEPARTMENT);
        
        // Assert:
        assertNotNull(course);
        assertEquals(VALID_COURSE, course.getCourseId());
        assertEquals(faculty, course.getFaculty());
        verify(mockCourseRepository, times(1)).save(course);
    }
    
    @Test
    public void testCreateCourseWithEmptyCourseId() {
        Faculty faculty = new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION));
        when(mockFacultyRepository.findByDepartmentName(VALID_DEPARTMENT)).thenReturn(faculty);
        
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.createCourse(INVALID_EMPTY, VALID_DEPARTMENT));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Course ID cannot be empty", exception.getMessage());
        verify(mockCourseRepository, never()).save(any(Course.class));
    }
    
    @Test
    public void testGetCourseSuccess() {
        Course course = new Course(VALID_COURSE, new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION)));
        when(mockCourseRepository.findByCourseId(VALID_COURSE)).thenReturn(course);
        
        Course result = universityService.getCourse(VALID_COURSE);
        assertNotNull(result);
        assertEquals(VALID_COURSE, result.getCourseId());
        verify(mockCourseRepository, times(1)).findByCourseId(VALID_COURSE);
    }
    
    @Test
    public void testGetCourseNotFound() {
        when(mockCourseRepository.findByCourseId(VALID_COURSE)).thenReturn(null);
        TextBookExchangeException exception = assertThrows(TextBookExchangeException.class,
            () -> universityService.getCourse(VALID_COURSE));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Course not found", exception.getMessage());
        verify(mockCourseRepository, times(1)).findByCourseId(VALID_COURSE);
    }
    
    @Test
    public void testUpdateCourseSuccess() {
        Course course = new Course(VALID_COURSE, new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION)));
        when(mockCourseRepository.findByCourseId(VALID_COURSE)).thenReturn(course);
        when(mockCourseRepository.save(any(Course.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));
        
        Course updated = universityService.updateCourse(VALID_COURSE, VALID_COURSE_NEW);
        assertNotNull(updated);
        assertEquals(VALID_COURSE_NEW, updated.getCourseId());
    }
    
    @Test
    public void testDeleteCourseSuccess() {
        Course course = new Course(VALID_COURSE, new Faculty(VALID_DEPARTMENT, new Institution(VALID_INSTITUTION)));
        when(mockCourseRepository.findByCourseId(VALID_COURSE)).thenReturn(course);
        
        universityService.deleteCourse(VALID_COURSE);
        verify(mockCourseRepository, times(1)).delete(course);
    }
}