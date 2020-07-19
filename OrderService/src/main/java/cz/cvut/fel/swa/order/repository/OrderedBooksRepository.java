package cz.cvut.fel.swa.order.repository;

import cz.cvut.fel.swa.order.model.OrderedBooks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderedBooksRepository extends JpaRepository<OrderedBooks, Long> {
}
