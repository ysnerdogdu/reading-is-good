package getir.repository;

import getir.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IBookRepository extends MongoRepository<Book, String> {

    Optional<Book> findByNameAndWriterAndPublishYearAndPrice(String name, String writer, Integer publishYear, Double price);

}
