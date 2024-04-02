package com.spring.springboot.app.biblioteca;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.test.context.SpringBootTest;

import com.spring.springboot.app.biblioteca.controllers.BooksController;
import com.spring.springboot.app.biblioteca.models.entity.Book;
import com.spring.springboot.app.biblioteca.models.service.IBooksService;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.Mockito.when;

@SpringBootTest
class BibliotecaApplicationTests {

    // Prueba para verificar la funcionalidad de eliminar un libro
    @Test
    public void testRemoveBook() throws Exception {
        // Arrange
        Long bookIdToRemove = 1L;
        IBooksService booksServiceMock = mock(IBooksService.class);
        BooksController controller = new BooksController(booksServiceMock);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.delete("/books/remove/{bookId}", bookIdToRemove))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    // Prueba para verificar la funcionalidad de obtener un libro por su ID
    @Test
    public void testGetBookById() throws Exception {
        // Arrange
        Long bookIdToRetrieve = 1L;
        Book bookToRetrieve = new Book("Harry Potter", "J.K. Rowling", "9780545582889", true);
        IBooksService booksServiceMock = mock(IBooksService.class);
        when(booksServiceMock.getBookById(bookIdToRetrieve)).thenReturn(bookToRetrieve);
        BooksController controller = new BooksController(booksServiceMock);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books/{bookId}", bookIdToRetrieve))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Harry Potter"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.author").value("J.K. Rowling"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.isbn").value("9780545582889"));
    }

    // Prueba para verificar la funcionalidad de obtener todos los libros
    @Test
    public void testGetAllBooks() throws Exception {
        // Arrange
        List<Book> allBooks = Arrays.asList(
            new Book("Book 1", "Author 1", "1234567890", true),
            new Book("Book 2", "Author 2", "0987654321", true)
        );
        IBooksService booksServiceMock = mock(IBooksService.class);
        when(booksServiceMock.getAllBooks()).thenReturn(allBooks);
        BooksController controller = new BooksController(booksServiceMock);
        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

        // Act & Assert
        mockMvc.perform(MockMvcRequestBuilders.get("/books/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Book 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].author").value("Author 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].isbn").value("1234567890"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].title").value("Book 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].author").value("Author 2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].isbn").value("0987654321"));
    }

}
