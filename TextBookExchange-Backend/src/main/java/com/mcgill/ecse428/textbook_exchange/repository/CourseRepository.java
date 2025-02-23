package com.mcgill.ecse428.textbook_exchange.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;



import com.mcgill.ecse428.textbook_exchange.model.Course;
import com.mcgill.ecse428.textbook_exchange.model.Faculty;

public interface CourseRepository extends CrudRepository<Course, String> {
    public Course findByCourseId(String courseId);

    public List<Course> findByFaculty(Faculty faculty);
}
