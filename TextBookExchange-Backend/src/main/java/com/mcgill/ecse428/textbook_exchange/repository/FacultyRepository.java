package com.mcgill.ecse428.textbook_exchange.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Faculty;
import com.mcgill.ecse428.textbook_exchange.model.Institution;


public interface FacultyRepository extends CrudRepository<Faculty, String> {
    public Faculty findByDepartmentName(String departmentName);

    public List<Faculty> findByInstitution(Institution institution);
    
}
