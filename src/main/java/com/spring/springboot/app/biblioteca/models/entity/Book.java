package com.spring.springboot.app.biblioteca.models.entity;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "books")
public class Book implements Serializable{
	
		private static final long serialVersionUID = 1L;

		@Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "id") // Corresponde al campo 'id' de la tabla 'books'
	    private Long id;

	    @Column(name = "title") // Corresponde al campo 'title' de la tabla 'books'
	    private String title;

	    @Column(name = "author") // Corresponde al campo 'author' de la tabla 'books'
	    private String author;

	    @Column(name = "isbn") // Corresponde al campo 'isbn' de la tabla 'books'
	    private String isbn;

	    @Column(name = "available") // Corresponde al campo 'available' de la tabla 'books'
	    private boolean available;

	    // Constructor
	    public Book() {}

	    public Book(String title, String author, String isbn, boolean available) {
	        this.title = title;
	        this.author = author;
	        this.isbn = isbn;
	        this.available = available;
	    }

	    // Getters y setters
	    public Long getId() {
	        return id;
	    }

	    public void setId(Long id) {
	        this.id = id;
	    }

	    public String getTitle() {
	        return title;
	    }

	    public void setTitle(String title) {
	        this.title = title;
	    }

	    public String getAuthor() {
	        return author;
	    }

	    public void setAuthor(String author) {
	        this.author = author;
	    }

	    public String getIsbn() {
	        return isbn;
	    }

	    public void setIsbn(String isbn) {
	        this.isbn = isbn;
	    }

	    public boolean isAvailable() {
	        return available;
	    }

	    public void setAvailable(boolean available) {
	        this.available = available;
	    }

	    // Método toString para representación textual del libro
	    @Override
	    public String toString() {
	        return "Book{" +
	                "id=" + id +
	                ", title='" + title + '\'' +
	                ", author='" + author + '\'' +
	                ", isbn='" + isbn + '\'' +
	                ", available=" + available +
	                '}';
	    }
	}