package com.shokhan.spring.service;


import com.shokhan.spring.models.Book;
import com.shokhan.spring.models.Person;
import com.shokhan.spring.repositories.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepo bookRepo;

    @Autowired
    public BookService(BookRepo bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> findAll(boolean sortByYear) {
        if (sortByYear)
            return bookRepo.findAll(Sort.by("year"));
        else
            return bookRepo.findAll();
    }

    public List<Book> searchByTitle(String query) {
        return bookRepo.findByTitleStartingWith(query);
    }

    public List<Book> findWithPagination(Integer page, Integer booksPerPage, boolean sortByYear) {
        if (sortByYear)
            return bookRepo.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        else
            return bookRepo.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public Book findById(int id){
        Optional<Book> book = bookRepo.findById(id);
        return book.orElse(null);
    }

    @Transactional
    public void save(Book book){
        bookRepo.save(book);
    }


    @Transactional
    public void delete(int id){
        bookRepo.deleteById(id);
    }

    public Person getBookOwner(int id) {
        return bookRepo.findById(id).map(Book::getOwner).orElse(null);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        Book bookToBeUpdated = bookRepo.findById(id).get();

        updatedBook.setId(id);
        updatedBook.setOwner(bookToBeUpdated.getOwner());

        bookRepo.save(updatedBook);
    }

    @Transactional
    public void release(int id) {
        bookRepo.findById(id).ifPresent(
                book -> {
                    book.setOwner(null);
                    book.setTakenAt(null);
                });
    }

    @Transactional
    public void assign(int id, Person selectedPerson) {
        bookRepo.findById(id).ifPresent(
                book -> {
                    book.setOwner(selectedPerson);
                    book.setTakenAt(new Date());
                }
        );
    }

}
