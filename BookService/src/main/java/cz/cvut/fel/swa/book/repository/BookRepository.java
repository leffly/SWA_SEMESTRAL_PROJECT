package cz.cvut.fel.swa.book.repository;

import cz.cvut.fel.swa.book.enums.BookCarrierType;
import cz.cvut.fel.swa.book.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findAllByGenre(String genre);

    List<Book> findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc(String author, String name, BookCarrierType carrierType);
}
