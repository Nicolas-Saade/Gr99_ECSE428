package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;



import com.mcgill.ecse428.textbook_exchange.model.Course;

public interface CourseRepository extends CrudRepository<Course, String> {
    public Course findByCourseId(String courseId);
}
