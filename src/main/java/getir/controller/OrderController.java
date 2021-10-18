package getir.controller;

import getir.controller.payload.request.NewOrderRequest;
import getir.controller.payload.response.OrderDto;
import getir.service.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/order", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;


    @PostMapping("/create")
    public ResponseEntity<OrderDto> placeOrder(@RequestBody NewOrderRequest newOrderRequest) {

        log.info("New order request is received for customer={}", newOrderRequest.getCustomerId());

        try {
            OrderDto newOrderDto = orderService.createNewOrder(newOrderRequest);
            if (newOrderDto != null) {
                return new ResponseEntity<>(newOrderDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception ex) {
            log.error("Exception is occurred during order creation ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDto> getOrder(@PathVariable String id) {

        log.info("Order query by id={} is received", id);

        try {
            OrderDto orderDto = orderService.getOrderById(id);
            if (orderDto == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(orderDto, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Exception is occurred during order fetching ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping()
    public ResponseEntity<List<OrderDto>> getOrdersByDateInterval(@DateTimeFormat(iso = DATE) @RequestParam LocalDate startDate,
                                                    @DateTimeFormat(iso = DATE) @RequestParam LocalDate endDate) {

        log.info("Orders query by startDate={} endDate={} is received", startDate, endDate);

        try {
            List<OrderDto> orders = orderService.getOrdersByDate(startDate.atStartOfDay(), endDate.atStartOfDay());
            if (orders == null) {
                return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
            }
            return new ResponseEntity<>(orders, HttpStatus.OK);

        } catch (Exception ex) {
            log.error("Exception is occurred during order fetching ", ex);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
