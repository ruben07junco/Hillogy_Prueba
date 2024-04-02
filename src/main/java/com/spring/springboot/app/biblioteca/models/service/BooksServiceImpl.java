package com.spring.springboot.app.biblioteca.models.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.spring.springboot.app.biblioteca.models.dao.BookDao;
import com.spring.springboot.app.biblioteca.models.entity.Book;

//Clase de implementación del servicio de gestión de libros
@Service
public class BooksServiceImpl implements IBooksService {

    // Inyección de dependencias del DAO de libros
    private BookDao bookDao;

    // Constructor
    public BooksServiceImpl(BookDao bookDao) {
        this.bookDao = bookDao;
    }

 // Agrega un nuevo libro a la biblioteca
    @Override
    public void addBook(Book book) {
        if (book == null) {
            throw new IllegalArgumentException("El libro proporcionado es nulo.");
        }
        if (book.getId() != null) {
            throw new IllegalArgumentException("El ID del libro debe ser nulo para agregar un nuevo libro.");
        }

        // Verificar si ya existe un libro con el mismo título o autor
        List<Book> existingBooksByTitle = bookDao.findByTitleContainingIgnoreCase(book.getTitle());
        List<Book> existingBooksByAuthor = bookDao.findByAuthorContainingIgnoreCase(book.getAuthor());
        if (!existingBooksByTitle.isEmpty() && !existingBooksByAuthor.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un libro con el mismo título y autor.");
        }

        // Verificar si ya existe un libro con el mismo ISBN
        List<Book> existingBooksByIsbn = bookDao.findByIsbnContainingIgnoreCase(book.getIsbn());
        if (!existingBooksByIsbn.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un libro con el mismo ISBN.");
        }
        book.setAvailable(true);

        // Si pasa todas las validaciones, guardar el nuevo libro
        bookDao.save(book);
    }

    // Elimina un libro de la biblioteca
    @Override
    public void removeBook(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("El ID del libro proporcionado es nulo.");
        }
        if (!bookDao.existsById(bookId)) {
            throw new IllegalArgumentException("No se encontró ningún libro con el ID proporcionado.");
        }
        bookDao.deleteById(bookId);
    }

    // Obtiene un libro por su ID
    @Override
    @Transactional(readOnly = true)
    public Book getBookById(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("El ID del libro proporcionado es nulo.");
        }
        Optional<Book> optionalBook = bookDao.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ningún libro con el ID proporcionado.");
        }
        return optionalBook.get();
    }

    // Obtiene todos los libros de la biblioteca
    @Override
    @Transactional(readOnly = true)
    public List<Book> getAllBooks() {
        return (List<Book>) bookDao.findAll();
    }

    // Realiza el préstamo de un libro
    @Override
    public boolean checkoutBook(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("El ID del libro proporcionado es nulo.");
        }
        Optional<Book> optionalBook = bookDao.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ningún libro con el ID proporcionado.");
        }
        Book book = optionalBook.get();
        if (!book.isAvailable()) {
            throw new IllegalStateException("El libro ya está prestado.");
        }
        book.setAvailable(false);
        bookDao.save(book);
        return true;
    }

    // Devuelve un libro prestado
    @Override
    public boolean returnBook(Long bookId) {
        if (bookId == null) {
            throw new IllegalArgumentException("El ID del libro proporcionado es nulo.");
        }
        Optional<Book> optionalBook = bookDao.findById(bookId);
        if (optionalBook.isEmpty()) {
            throw new IllegalArgumentException("No se encontró ningún libro con el ID proporcionado.");
        }
        Book book = optionalBook.get();
        if (book.isAvailable()) {
            throw new IllegalStateException("El libro no está prestado.");
        }
        book.setAvailable(true);
        bookDao.save(book);
        return true;
    }

    // Busca libros por una palabra clave en el título, autor o ISBN
    @Override
    public List<Book> searchBooks(String keyword) {
        // Buscar libros por título, autor o ISBN que coincidan con el término de búsqueda
        List<Book> booksByTitle = bookDao.findByTitleContainingIgnoreCase(keyword);
        List<Book> booksByAuthor = bookDao.findByAuthorContainingIgnoreCase(keyword);
        List<Book> booksByIsbn = bookDao.findByIsbnContainingIgnoreCase(keyword);

        // Combinar resultados y eliminar duplicados
        return Stream.of(booksByTitle, booksByAuthor, booksByIsbn)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }
    
    // Verifica si existe un libro con el mismo título y autor en la base de datos.
    @Override
    public boolean isTitleAuthorMatch(String title, String author) {
        return !bookDao.findByTitleAndAuthor(title, author).isEmpty();
    }

    
    //Verifica si existe un libro con el mismo ISBN en la base de datos.   
    @Override
    public boolean isIsbnMatch(String isbn) {
        return bookDao.findByIsbn(isbn).isPresent();
    }
}