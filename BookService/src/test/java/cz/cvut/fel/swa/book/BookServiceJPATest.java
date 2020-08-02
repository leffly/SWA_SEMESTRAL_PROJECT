package cz.cvut.fel.swa.book;


import cz.cvut.fel.swa.book.enums.BookCarrierType;
import cz.cvut.fel.swa.book.model.Book;
import cz.cvut.fel.swa.book.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import static org.assertj.core.api.Assertions.assertThat;


@ExtendWith(SpringExtension.class)
@DataJpaTest
public class BookServiceJPATest {


    @Autowired
    private DataSource dataSource;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    BookRepository bookRepository;

    Book mockBook;

    @BeforeEach
    void init() {
        mockBook = new Book("Harry Potrat",
                "Roulingova",
                1500,
                BookCarrierType.ELECTRONIC_TEXT,
                "Luxor",
                "fantasy");
        bookRepository.save(mockBook);
    }

    @Test
    void injectedComponentsAreNotNull() {
        assertThat(dataSource).isNotNull();
        assertThat(jdbcTemplate).isNotNull();
        assertThat(entityManager).isNotNull();
        assertThat(bookRepository).isNotNull();
    }

    @Test
    void findAllByGenre() {
        assertThat(bookRepository.findAllByGenre("fantasy")).isNotEmpty();
        assertThat(bookRepository.findAll()).size().isEqualTo(1);

        assertThat(bookRepository.findById(5l)).isEmpty();
    }

    @Test
    void findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc() {
        assertThat(bookRepository
                .findAllByAuthorAndNameAndCarrierTypeOrderByPriceAsc("Roulingova",
                        "Harry Potrat", BookCarrierType.ELECTRONIC_TEXT)).isNotEmpty();
    }
}
