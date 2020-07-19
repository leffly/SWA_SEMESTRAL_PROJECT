package cz.cvut.fel.swa.store.model;

import cz.cvut.fel.swa.store.enums.BookCarrierType;

import java.io.Serializable;

public class Book implements Serializable {

    private String name;
    private String author;
    private BookCarrierType carrierType;

    public Book() {
    }

    public Book(String name, String author, BookCarrierType carrierType) {
        this.name = name;
        this.author = author;
        this.carrierType = carrierType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public BookCarrierType getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(BookCarrierType carrierType) {
        this.carrierType = carrierType;
    }

    @Override
    public String toString() {
        return "Book{" +
                "name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", carrierType=" + carrierType +
                '}';
    }
}
