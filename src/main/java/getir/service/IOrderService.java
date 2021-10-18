package getir.service;

import getir.controller.payload.request.NewOrderRequest;
import getir.controller.payload.response.OrderDto;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface IOrderService {

    OrderDto createNewOrder(NewOrderRequest newOrderRequest);

    OrderDto getOrderById(String orderId);

    List<OrderDto> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<OrderDto> getOrdersByCustomerId(String customerId, Pageable pageable);
}
