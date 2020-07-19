package cz.cvut.fel.swa.order.repository;

import cz.cvut.fel.swa.order.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByGenre(String genre);
}
