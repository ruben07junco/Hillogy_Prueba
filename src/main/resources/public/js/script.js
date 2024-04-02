// Función para cargar todos los libros al cargar la página
        document.addEventListener("DOMContentLoaded", function() {
            loadBooks();
        });

        // Función para cargar todos los libros desde el backend
        function loadBooks() {
            fetch('/books/all')
            .then(response => response.json())
            .then(data => {
                const booksTable = document.getElementById('booksTable');
                const tbody = booksTable.getElementsByTagName('tbody')[0];
                tbody.innerHTML = ''; // Limpiar la tabla antes de agregar libros

                data.forEach(book => {
                    const row = createTableRow(book);
                    tbody.appendChild(row);
                });
            });
        }

        // Función para buscar libros
        document.getElementById('searchBookForm').addEventListener('submit', function(event) {
            event.preventDefault();
            
            const searchKeyword = document.getElementById('searchKeyword').value.trim();
            if (searchKeyword !== '') {
                fetch(`/books/search?keyword=${encodeURIComponent(searchKeyword)}`)
                .then(response => response.json())
                .then(data => {
                    const searchResultsHeader = document.getElementById('searchResultsHeader');
                    const searchResultsTable = document.getElementById('searchResultsTable');
                    const tbody = searchResultsTable.getElementsByTagName('tbody')[0];
                    tbody.innerHTML = ''; // Limpiar la tabla antes de agregar resultados de búsqueda
                    
                    if (data.length > 0) {
                        data.forEach(book => {
                            const row = createTableRow(book);
                            tbody.appendChild(row);
                        });

                        // Mostrar el encabezado y la tabla de resultados de búsqueda
                        searchResultsHeader.style.display = 'block';
                        searchResultsTable.style.display = 'block';
                        document.getElementById('noResultsMessage').style.display = 'none';
                    } else {
                        // Mostrar mensaje de que no se encontraron resultados
                        searchResultsHeader.style.display = 'none';
                        searchResultsTable.style.display = 'none';
                        document.getElementById('noResultsMessage').style.display = 'block';
                    }
                });
            }
        });

        // Función para crear una fila de tabla para un libro
        function createTableRow(book) {
            const row = document.createElement('tr');
            row.innerHTML = `
                <td>${book.title}</td>
                <td>${book.author}</td>
                <td>${book.isbn}</td>
                <td>${book.available ? 'Sí' : 'No'}</td>
                <td>
                    <button ${book.available ? `onclick="checkoutBook(${book.id})"` : 'disabled'}>Prestar</button>
                    <button ${!book.available ? `onclick="returnBook(${book.id})"` : 'disabled'}>Devolver</button>
                    <button onclick="confirmDeleteBook(${book.id})">Eliminar</button>
                </td>
            `;
            return row;
        }

        // Función para agregar un nuevo libro
        document.getElementById('addBookForm').addEventListener('submit', function(event) {
            event.preventDefault();

            const formData = new FormData(this);
            const bookData = {};
            formData.forEach((value, key) => {
                bookData[key] = value;
            });

            fetch('/books/add', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(bookData)
            })
            .then(() => {
                loadBooks(); // Recargar la tabla después de agregar el libro
            });

            // Limpiar el formulario después de agregar el libro
            this.reset();
        });

        // Función para realizar el préstamo de un libro
        function checkoutBook(bookId) {
            fetch(`/books/checkout/${bookId}`, {
                method: 'POST'
            })
            .then(() => {
                loadBooks(); // Recargar la tabla después de realizar el préstamo
            });
        }

        // Función para devolver un libro prestado
        function returnBook(bookId) {
            fetch(`/books/return/${bookId}`, {
                method: 'POST'
            })
            .then(() => {
                loadBooks(); // Recargar la tabla después de devolver el libro
            });
        }

        // Función para eliminar un libro de la lista
        function removeBook(bookId) {
            fetch(`/books/remove/${bookId}`, {
                method: 'DELETE'
            })
            .then(() => {
                loadBooks(); // Recargar la tabla después de eliminar el libro
            });
        }

        // Función para confirmar la eliminación de un libro
        function confirmDeleteBook(bookId) {
            const confirmDelete = confirm("¿Estás seguro de que deseas eliminar este libro?");
            if (confirmDelete) {
                removeBook(bookId);
            }
        }
        
      	function validateBook() {
		    return new Promise((resolve, reject) => {
		        var title = document.getElementById('title').value;
		        var author = document.getElementById('author').value;
		        var isbn = document.getElementById('isbn').value;
		
		        // Realizar petición AJAX para verificar si el título y autor coinciden
		        // o si el ISBN coincide con otro libro
		        var xhr = new XMLHttpRequest();
		        xhr.open('POST', '/books/validate', true);
		        xhr.setRequestHeader('Content-Type', 'application/json');
		        xhr.onreadystatechange = function () {
		            if (xhr.readyState === 4) {
		                if (xhr.status === 200) {
		                    var response = JSON.parse(xhr.responseText);
		                    if (!response) {
		                        // Mostrar mensaje de error si el libro ya existe
		                        window.alert("El libro no se ha podido añadir porque algunos de los datos introducidos coinciden con otro libro");
		                        resolve(false);
		                    } else {
		                        // Mostrar mensaje de confirmación y enviar el formulario
		                        window.alert("Libro añadido correctamente.");
		                        resolve(true);
		                    }
		                } else {
		                    // Manejar errores de la solicitud AJAX
		                    reject(xhr.statusText);
		                }
		            }
		        };
		        var data = JSON.stringify({ title: title, author: author, isbn: isbn });
		        xhr.send(data);
		    });
		}