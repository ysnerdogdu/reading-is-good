package getir.service;

import getir.controller.payload.request.BookOrder;
import getir.controller.payload.request.NewOrderRequest;
import getir.controller.payload.response.OrderDto;
import getir.model.Book;
import getir.model.Order;
import getir.repository.IOrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final IOrderRepository orderRepository;
    private final IBookService bookService;

    @Override
    public OrderDto createNewOrder(NewOrderRequest newOrderRequest) {

        String customerId = newOrderRequest.getCustomerId();
        List<BookOrder> bookOrderList = newOrderRequest.getBookOrders();

        // filter invalid book orders which have invalid book count
        final List<BookOrder> invalidBookOrders = bookOrderList.stream()
                .filter(bookOrder -> bookOrder.getCount() <= 0)
                .collect(Collectors.toList());

        if (customerId == null || !invalidBookOrders.isEmpty()) {
            log.error("New order request failed due to invalid customerId={} bookOrders={}", customerId, invalidBookOrders);
            return null;
        }

        // fetch books by id and controlling whether or not there is enough stock for each book order
        List<Book> bookList = bookOrderList.stream()
                .map(bookOrder -> bookService.getBookWithHavingEnoughStock(bookOrder.getBookId(), bookOrder.getCount()))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (bookList.size() < bookOrderList.size()) {
            log.error("Order is not created due to lack of requested books");
            return null;
        }

        Map<String, Integer> collect = bookOrderList.stream()
                .collect(Collectors.toMap(BookOrder::getBookId, BookOrder::getCount));

        // update each book stock
        bookList.forEach(book -> book.setStock(book.getStock() - collect.get(book.getId())));
        bookService.saveBooks(bookList);

        // calculate total order price
        Double totalPrice = bookList.stream()
                .map(book -> collect.get(book.getId()) * book.getPrice())
                .reduce(0.0, Double::sum);

        // calculate total book count
        Integer totalBooks = collect.values().stream()
                .reduce(0, Integer::sum);

        // create new order
        Order order = Order.builder()
                .customerId(customerId)
                .bookOrders(bookOrderList)
                .totalBooks(totalBooks)
                .totalPrice(totalPrice)
                .status("COMPLETED")
                .build();

        Order savedOrder = orderRepository.save(order);

        log.info("Order is created successfully with customer={} books={}", customerId, bookList);

        return OrderDto.fromOrder(savedOrder);

    }


    @Override
    public OrderDto getOrderById(String orderId) {

        final Optional<Order> orderOptional = orderRepository.findById(orderId);
        if (orderOptional.isEmpty()) {
            log.error("Order is not found with id={}", orderId);
            return null;
        }
        return OrderDto.fromOrder(orderOptional.get());
    }

    @Override
    public List<OrderDto> getOrdersByDate(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);

        return orders.stream()
                .map(OrderDto::fromOrder)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDto> getOrdersByCustomerId(String customerId) {

        List<Order> customerOrders = orderRepository.findByCustomerId(customerId);

        return customerOrders.stream()
                .map(OrderDto::fromOrder)
                .collect(Collectors.toList());
    }
}
