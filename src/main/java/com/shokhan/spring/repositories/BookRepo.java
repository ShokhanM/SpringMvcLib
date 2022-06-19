package com.shokhan.spring.repositories;


import com.shokhan.spring.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepo extends JpaRepository<Book, Integer> {

    List<Book> findByTitleStartingWith(String title);

}
