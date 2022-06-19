package com.shokhan.spring.repositories;


import com.shokhan.spring.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PersonRepo extends JpaRepository<Person, Integer> {

    Optional<Person> findByFullName(String name);

}
