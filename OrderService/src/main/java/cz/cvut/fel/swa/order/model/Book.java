package cz.cvut.fel.swa.order.model;

import cz.cvut.fel.swa.order.enums.BookCarrierType;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bookify_books")
public class Book implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "book_id")
    private Long id;
    private String name;
    private String author;
    private int price;
    private BookCarrierType carrierType;
    private String supplier;
    private String genre;

    @OneToMany(mappedBy = "book")
    private List<OrderedBooks> orders;

    public Book() {
    }

    public Book(String name, String author, int price, BookCarrierType carrierType, String supplier, String genre) {
        this.name = name;
        this.author = author;
        this.price = price;
        this.carrierType = carrierType;
        this.supplier = supplier;
        this.genre = genre;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public BookCarrierType getCarrierType() {
        return carrierType;
    }

    public void setCarrierType(BookCarrierType carrierType) {
        this.carrierType = carrierType;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public List<OrderedBooks> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderedBooks> orders) {
        this.orders = orders;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", author='" + author + '\'' +
                ", price=" + price +
                ", carrierType=" + carrierType +
                ", supplier='" + supplier + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}
