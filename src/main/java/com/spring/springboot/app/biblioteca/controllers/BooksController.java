package com.spring.springboot.app.biblioteca.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.spring.springboot.app.biblioteca.models.entity.Book;
import com.spring.springboot.app.biblioteca.models.service.IBooksService;

//Controlador para las operaciones relacionadas con los libros
@RestController
@RequestMapping("/books")
public class BooksController {

	 // Servicio de gestión de libros
	 private IBooksService booksService;
	
	 // Constructor para la inyección de dependencias del servicio de libros
	 public BooksController(IBooksService booksService) {
	     this.booksService = booksService;
	 }
	
	 // Endpoint para agregar un nuevo libro
	 @PostMapping("/add")
	 public void addBook(@RequestBody Book book) {
	     booksService.addBook(book);
	 }
	
	 // Endpoint para eliminar un libro por su ID
	 @DeleteMapping("/remove/{bookId}")
	 public void removeBook(@PathVariable Long bookId) {
	     booksService.removeBook(bookId);
	 }
	
	 // Endpoint para obtener un libro por su ID
	 @GetMapping("/{bookId}")
	 public Book getBookById(@PathVariable Long bookId) {
	     return booksService.getBookById(bookId);
	 }
	
	 // Endpoint para obtener todos los libros
	 @GetMapping("/all")
	 public List<Book> getAllBooks() {
	     return booksService.getAllBooks();
	 }
	
	 // Endpoint para buscar libros por palabra clave en el título, autor o ISBN
	 @GetMapping("/search")
	 public List<Book> searchBooks(@RequestParam String keyword) {
	     return booksService.searchBooks(keyword);
	 }
	
	 // Endpoint para realizar el préstamo de un libro
	 @PostMapping("/checkout/{bookId}")
	 public boolean checkoutBook(@PathVariable Long bookId) {
	     return booksService.checkoutBook(bookId);
	 }
	
	 // Endpoint para devolver un libro prestado
	 @PostMapping("/return/{bookId}")
	 public boolean returnBook(@PathVariable Long bookId) {
	     return booksService.returnBook(bookId);
	 }
	 
	 @PostMapping("/validate")
	    public boolean validateBook(@RequestBody Book book) {
	        boolean isTitleAuthorMatch = booksService.isTitleAuthorMatch(book.getTitle(), book.getAuthor());
	        boolean isIsbnMatch = booksService.isIsbnMatch(book.getIsbn());

	        return !(isTitleAuthorMatch || isIsbnMatch);
	    }
}
