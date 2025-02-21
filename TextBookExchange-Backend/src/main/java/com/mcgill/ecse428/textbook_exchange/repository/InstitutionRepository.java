package com.mcgill.ecse428.textbook_exchange.repository;


import org.springframework.data.repository.CrudRepository;


import com.mcgill.ecse428.textbook_exchange.model.Institution;

public interface InstitutionRepository extends CrudRepository<Institution, String> {
    public Institution findByInstitutiontName(String institutionName);
    
}
