package cz.cvut.fel.swa.order.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "bookify_orders")
public class Order implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToMany(mappedBy = "order")
    private List<OrderedBooks> books;

    private int totalPrice;

    public Order() {
    }

    //FIXME wrong constructor ?
    public Order(Client client, List<Book> books) {
        this.client = client;
        this.calculateTotalPrice();
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<OrderedBooks> getBooks() {
        return books;
    }

    public void setBooks(List<OrderedBooks> books) {
        this.books = books;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int calculateTotalPrice() {
        this.totalPrice = 0;
        books.forEach(b -> {
            this.totalPrice += b.getBook().getPrice();
        });
        return this.totalPrice;
    }

    @Override
    public String toString() {
        return "Order{" +
                "id=" + id +
                ", client=" + client +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
