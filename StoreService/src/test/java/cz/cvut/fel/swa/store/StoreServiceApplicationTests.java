package cz.cvut.fel.swa.store;

import cz.cvut.fel.swa.store.enums.BookCarrierType;
import cz.cvut.fel.swa.store.model.Book;
import cz.cvut.fel.swa.store.model.Client;
import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static cz.cvut.fel.swa.store.TestUtils.toJson;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@EmbeddedKafka
@WebMvcTest
class StoreServiceApplicationTests {

    @Autowired
    private MockMvc mvc;

    @Test
    void contextLoads() throws Exception {
        CompleteOrderRequest request = new CompleteOrderRequest();
        request.setBooks(Collections.singletonList(new Book("name", "author", BookCarrierType.HARDCOVER)));
        request.setClient(new Client("firstName", "lastName", "email"));
        this.mvc.perform(post("/store/order")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(request)))
                .andDo(print())
                .andExpect(status().is2xxSuccessful());
    }

}
