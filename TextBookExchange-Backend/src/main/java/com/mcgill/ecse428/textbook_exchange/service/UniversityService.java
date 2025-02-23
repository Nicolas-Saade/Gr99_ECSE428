package com.mcgill.ecse428.textbook_exchange.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.mcgill.ecse428.textbook_exchange.exception.TextBookExchangeException;
import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;
import com.mcgill.ecse428.textbook_exchange.repository.CourseRepository;
import com.mcgill.ecse428.textbook_exchange.repository.FacultyRepository;
import com.mcgill.ecse428.textbook_exchange.repository.InstitutionRepository;

@Service
public class UniversityService {
   @Autowired
    private InstitutionRepository institutionRepository;
    @Autowired
    private FacultyRepository facultyRepository;
    @Autowired
    private CourseRepository courseRepository;


    @Transactional
    public Institution createInstitution(String institutionName) {
        if (institutionName == null || institutionName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Institution name cannot be empty");
        }
        if (institutionRepository.findByInstitutionName(institutionName) != null) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Institution already exists");
        }
        Institution institution = new Institution(institutionName);
        return institutionRepository.save(institution);
    }

    @Transactional
    public Institution getInstitution(String institutionName) { 
        if (institutionName == null || institutionName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Institution name cannot be empty");
        }
        Institution institution = institutionRepository.findByInstitutionName(institutionName);
        if (institution == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Institution not found");
        }
        return institution;}


    @Transactional
    public Institution updateInstitution(String institutionName, String newInstitutionName) {
        Institution institution = getInstitution(institutionName);
        if (newInstitutionName == null || newInstitutionName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Institution name cannot be empty");
        }
        institution.setInstitutionName(newInstitutionName);
        return institutionRepository.save(institution);
    }


    @Transactional
    public void deleteInstitution(String institutionName) {
        Institution institution = getInstitution(institutionName);
        institutionRepository.delete(institution);
    }


    @Transactional
    public Faculty createFaculty(String departmentName, String institutionName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Department name cannot be empty");
        }
        Institution institution = getInstitution(institutionName);
        Faculty faculty = new Faculty(departmentName, institution);
        return facultyRepository.save(faculty);
    }


    @Transactional
    public Faculty getFaculty(String departmentName) {
        if (departmentName == null || departmentName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Department name cannot be empty");
        }
        Faculty faculty =  facultyRepository.findByDepartmentName(departmentName);
        if (faculty == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Faculty not found");
        }
        return faculty;
    }
     

    @Transactional
    public Faculty updateFaculty(String departmentName, String newDepartmentName) {
        Faculty faculty = getFaculty(departmentName);
        if (newDepartmentName == null || newDepartmentName.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Department name cannot be empty");
        }
        faculty.setDepartmentName(newDepartmentName);
        return facultyRepository.save(faculty);
    }

    @Transactional
    public void deleteFaculty(String departmentName) {
        Faculty faculty = getFaculty(departmentName);
        facultyRepository.delete(faculty);
    }

   
    @Transactional
    public Course createCourse(String courseId, String departmentName) {
        Faculty faculty = getFaculty(departmentName);
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Course ID cannot be empty");
            
        }
        Course course = new Course(courseId,faculty);

        return courseRepository.save(course);
    }


    @Transactional
    public Course getCourse(String courseId) {
        if (courseId == null || courseId.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Course ID cannot be empty");
        }
        Course course = courseRepository.findByCourseId(courseId);
        if (course == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"Course not found");
        }
        return course;
    }


    @Transactional
    public Course updateCourse(String courseId, String newCourseId) {
        Course course = getCourse(courseId);
        if (newCourseId == null || newCourseId.trim().isEmpty()) {
            throw new TextBookExchangeException(HttpStatus.BAD_REQUEST,"Course ID cannot be empty");
        }
        course.setCourseId(newCourseId);
        return courseRepository.save(course);
    }


    @Transactional
    public void deleteCourse(String courseId) {
        Course course = getCourse(courseId);
        courseRepository.delete(course);
    }

    public List<Faculty> getAllFaculties(String validInstitution) {
        Institution institution = getInstitution(validInstitution);
        List<Faculty> faculties = facultyRepository.findByInstitution(institution);
        if (faculties == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"No faculties found");
        }
        return faculties;
    }

    public List<Course> getAllCourses(String validDepartment) {
        Faculty faculty = getFaculty(validDepartment);
        List<Course> courses = courseRepository.findByFaculty(faculty);
        if (courses == null) {
            throw new TextBookExchangeException(HttpStatus.NOT_FOUND,"No courses found");
        }
        return courses;
    }
       
}

    
