package cz.cvut.fel.swa.book.dto;

import cz.cvut.fel.swa.book.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookOfferDTO {

    private List<Book> books;

    public BookOfferDTO(List<Book> books) {
        this.books = books;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public void addBook(Book book) {
        if (books == null) books = new ArrayList<>();
        books.add(book);
    }
}
