package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Faculty;

public interface FacultyRepository extends CrudRepository<Faculty, String> {
    public Faculty findByDepartmentName(String departmentName);
    
}
