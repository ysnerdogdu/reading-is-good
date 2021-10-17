package getir.controller;

import getir.controller.payload.request.CustomerAddRequest;
import getir.controller.payload.response.CustomerDto;
import getir.controller.payload.response.OrderDto;
import getir.service.ICustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CustomerController {

    private final ICustomerService customerService;

    @PostMapping("/create")
    public ResponseEntity<CustomerDto> createNewCustomer(@RequestBody CustomerAddRequest customerAddRequest){

        log.info("New customer request is received");
        try {
            CustomerDto customerDto = customerService.createNewCustomer(customerAddRequest);

            if (customerDto != null) {
                return new ResponseEntity<>(customerDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}/orders")
    public ResponseEntity<List<OrderDto>> getCustomerOrders(@PathVariable String id) {

        log.info("Customer orders request is received with customerId={}", id);

        try {
            List<OrderDto> orderDtos = customerService.getCustomerAllOrders(id);

            if (orderDtos.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return new ResponseEntity<>(orderDtos, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }


    }

}
