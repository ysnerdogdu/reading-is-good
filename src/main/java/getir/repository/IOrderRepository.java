package getir.repository;

import getir.model.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface IOrderRepository extends MongoRepository<Order, String> {

    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByCustomerId(String customerId);


}
