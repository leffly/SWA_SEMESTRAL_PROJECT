package cz.cvut.fel.swa.book;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka
@DataJpaTest
class BookServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
