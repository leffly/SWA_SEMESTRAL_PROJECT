package cz.cvut.fel.swa.store.controller;

import cz.cvut.fel.swa.store.request.CompleteOrderRequest;
import cz.cvut.fel.swa.store.service.CompleteOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final CompleteOrderService completeOrderService;

    public StoreController(CompleteOrderService completeOrderService) {
        this.completeOrderService = completeOrderService;
    }

    @PostMapping(value = "/order", produces = "application/json", consumes = "application/json")
    public ResponseEntity<String> completeOrder(@RequestBody CompleteOrderRequest completeOrderRequest){
        ResponseEntity responseEntity = new ResponseEntity("FAIL", HttpStatus.INTERNAL_SERVER_ERROR);
        if(completeOrderRequest != null && completeOrderRequest.getBooks() != null
                && completeOrderRequest.getBooks().size() > 0 && completeOrderRequest.getClient() != null
                && !completeOrderRequest.getClient().getEmail().isBlank()){
            completeOrderService.sendCompleteOrderMessage(completeOrderRequest);
            responseEntity = new ResponseEntity("SUCCESS", HttpStatus.OK);
        }
        return responseEntity;
    }

}
