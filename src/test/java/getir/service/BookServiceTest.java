package getir.service;

import getir.controller.payload.request.BookAddRequest;
import getir.controller.payload.response.BookDto;
import getir.model.Book;
import getir.repository.IBookRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BookServiceTest {

    @InjectMocks
    private BookService bookService;

    @Mock
    private IBookRepository bookRepository;


    @Test
    void addOrUpdateBook() {

        String name = "Test Book";
        String writer = "Test Writer";
        Integer publishYear = 2021;
        Integer stock = 100;
        Double price = 10.5;

        BookAddRequest request = new BookAddRequest(name, writer, publishYear, stock, price);

        Book book = Book.builder()
                .name(name)
                .writer(writer)
                .publishYear(publishYear)
                .stock(stock)
                .price(price)
                .build();

        when(bookRepository.findByNameAndWriterAndPublishYearAndPrice(name, writer, publishYear, price))
                .thenReturn(Optional.empty());

        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookDto bookDto = bookService.addOrUpdateBook(request);

        assertNotNull(bookDto);
        assertEquals(bookDto.getName(), book.getName());
        assertEquals(bookDto.getPrice(), book.getPrice());


    }

    @Test
    void updateBookStock() {

        String bookId = "b100";
        int newStock = 100;
        int existingStock= 1;

        Book book = Book.builder().stock(1).build();
        Book updatedBook = Book.builder().stock(newStock + existingStock).build();


        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        BookDto bookDto = bookService.updateBookStock(bookId, newStock);


        assertNotNull(bookDto);
        assertEquals(existingStock + newStock, bookDto.getStock());
    }
}
