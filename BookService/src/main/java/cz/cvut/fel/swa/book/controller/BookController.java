package cz.cvut.fel.swa.book.controller;

import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping(value = "/", produces = "application/json")
    public ResponseEntity<List<Book>> getAllBooks() {
        return new ResponseEntity<>(bookService.findAll(),HttpStatus.ACCEPTED);
    }

    @PostMapping(value = "/", consumes = "application/json", produces = "application/json")
    public ResponseEntity<HttpStatus> storeBooks
            (@RequestBody List<Book> books) {
        bookService.save(books);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
