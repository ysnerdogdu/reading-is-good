package getir.repository;

import getir.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ICustomerRepository extends MongoRepository<Customer, String> {

    Optional<Customer> findByEmail(String email);
}
