package cz.cvut.fel.swa.store.controller;

import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import cz.cvut.fel.swa.store.service.CompleteOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final CompleteOrderService completeOrderService;

    public StoreController(CompleteOrderService completeOrderService) {
        this.completeOrderService = completeOrderService;
    }

    @PostMapping(value = "/order", produces = APPLICATION_JSON_VALUE, consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity completeOrder(@RequestBody CompleteOrderRequest completeOrderRequest) {
        ResponseEntity responseEntity = new ResponseEntity(HttpStatus.BAD_REQUEST);
        if (completeOrderRequest.isValid()) {
            completeOrderService.sendCompleteOrderMessage(completeOrderRequest);
            responseEntity = new ResponseEntity(HttpStatus.OK);
        }
        return responseEntity;
    }

}
