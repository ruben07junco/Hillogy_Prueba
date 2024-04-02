package com.spring.springboot.app.biblioteca.models.service;

import java.util.List;

import com.spring.springboot.app.biblioteca.models.entity.Book;

//Interfaz para el servicio de gestión de libros
public interface IBooksService {
 
	 // Agrega un nuevo libro a la biblioteca
	 void addBook(Book book);
	 
	 // Elimina un libro de la biblioteca
	 void removeBook(Long bookId);
	 
	 // Obtiene un libro por su ID
	 Book getBookById(Long bookId);
	 
	 // Obtiene todos los libros de la biblioteca
	 List<Book> getAllBooks();
	 
	 // Busca libros por una palabra clave en el título, autor o ISBN
	 List<Book> searchBooks(String keyword);
	 
	 // Realiza el préstamo de un libro
	 boolean checkoutBook(Long bookId);
	 
	 // Devuelve un libro prestado
	 boolean returnBook(Long bookId);
	 
     //Verifica si existe un libro con el mismo título y autor en la base de datos.
	 boolean isTitleAuthorMatch(String title, String author);
	    
	 //Verifica si existe un libro con el mismo ISBN en la base de datos.	
	 boolean isIsbnMatch(String isbn);
 
}

