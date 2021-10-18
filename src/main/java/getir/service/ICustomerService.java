package getir.service;

import getir.controller.payload.request.CustomerAddRequest;
import getir.controller.payload.response.CustomerDto;
import getir.controller.payload.response.OrderDto;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ICustomerService {

    CustomerDto createNewCustomer(CustomerAddRequest customerAddRequest);

    List<OrderDto> getCustomerAllOrders(String customerId, Pageable pageable);
}
