package cz.cvut.fel.swa.order.request;

import cz.cvut.fel.swa.order.model.Book;
import cz.cvut.fel.swa.order.model.Client;

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

    @Override
    public String toString() {
        return "CompleteOrderRequest{" +
                "client=" + client +
                ", books=" + books +
                '}';
    }
}
