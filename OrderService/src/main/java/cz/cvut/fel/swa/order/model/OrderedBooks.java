package cz.cvut.fel.swa.order.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "bookify_ordered_books")
public class OrderedBooks implements Serializable {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public OrderedBooks() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    @Override
    public String toString() {
        return "OrderedBooks{" +
                "id=" + id +
                ", order=" + order +
                ", book=" + book +
                '}';
    }
}
