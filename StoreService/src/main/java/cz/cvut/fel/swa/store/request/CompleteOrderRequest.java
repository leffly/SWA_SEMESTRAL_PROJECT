package cz.cvut.fel.swa.store.request;

import cz.cvut.fel.swa.store.model.Book;
import cz.cvut.fel.swa.store.model.Client;

import java.util.List;

public class CompleteOrderRequest {

    private Client client;
    private List<Book> books;

    public CompleteOrderRequest() {
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public boolean isValid() {
        return books != null && !books.isEmpty() && client != null && !client.getEmail().isBlank();
    }

    @Override
    public String toString() {
        return "CompleteOrderRequest{" +
                "client=" + client +
                ", books=" + books +
                '}';
    }
}
