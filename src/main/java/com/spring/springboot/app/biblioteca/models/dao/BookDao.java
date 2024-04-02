package com.spring.springboot.app.biblioteca.models.dao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.spring.springboot.app.biblioteca.models.entity.Book;

public interface BookDao extends CrudRepository<Book, Long>{

	List<Book> findByTitleContainingIgnoreCase(String keyword);
    List<Book> findByAuthorContainingIgnoreCase(String keyword);
    List<Book> findByIsbnContainingIgnoreCase(String keyword);
    List<Book> findByTitleAndAuthor(String title, String author);
    Optional<Book> findByIsbn(String isbn);
}
