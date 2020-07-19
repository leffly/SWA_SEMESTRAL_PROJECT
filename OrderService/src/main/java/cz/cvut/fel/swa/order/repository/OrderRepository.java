package cz.cvut.fel.swa.order.repository;

import cz.cvut.fel.swa.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByClient_Email(String email);
}
