package cz.cvut.fel.swa.order;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

@EmbeddedKafka
@DataJpaTest
class OrderServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
