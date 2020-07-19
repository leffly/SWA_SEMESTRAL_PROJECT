package cz.cvut.fel.swa.order.controller;

import cz.cvut.fel.swa.order.model.Client;
import cz.cvut.fel.swa.order.model.Order;
import cz.cvut.fel.swa.order.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Order>> getOrder(@RequestBody Client client) {
        return new ResponseEntity<>(orderService.findOrderByClientEmail(client), HttpStatus.OK);
    }
}
