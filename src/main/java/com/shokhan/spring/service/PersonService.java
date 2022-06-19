package com.shokhan.spring.service;


import com.shokhan.spring.models.Book;
import com.shokhan.spring.models.Person;
import com.shokhan.spring.repositories.PersonRepo;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private final PersonRepo personRepo;

    @Autowired
    public PersonService(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    public Person findById(int id){
        Optional<Person> person = personRepo.findById(id);
        return person.orElse(null);
    }

    public List<Person> findAll(){
        return personRepo.findAll();
    }

    public List<Book> findBooks(int id){
        return null;
    }

    public Optional<Person> getPersonByFullName(String name){
        return personRepo.findByFullName(name);
    }

    @Transactional
    public void save(Person person){
        personRepo.save(person);
    }

    @Transactional
    public void delete(int id){
        personRepo.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person){
        person.setId(id);
        personRepo.save(person);
    }

    public List<Book> getBooksByPersonId(int id) {
        Optional<Person> person = personRepo.findById(id);

        if (person.isPresent()) {
            Hibernate.initialize(person.get().getBooks());

            person.get().getBooks().forEach(book -> {
                long diffInMillies = Math.abs(book.getTakenAt().getTime() - new Date().getTime());
                if (diffInMillies > 864000000)
                    book.setExpired(true);
            });

            return person.get().getBooks();
        }
        else {
            return Collections.emptyList();
        }
    }
}
