package getir.service;

import getir.controller.payload.request.BookAddRequest;
import getir.controller.payload.response.BookDto;
import getir.model.Book;
import getir.repository.IBookRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService implements IBookService {

    private final IBookRepository bookRepository;

    @Override
    public Book getBookWithStock(String bookId, Integer stock) {
        Optional<Book> bookOpt = bookRepository.findById(bookId);

        if (bookOpt.isEmpty()) {
            log.error("Book is not found with bookId={}", bookId);
            return null;
        } else if (bookOpt.get().getStock() < stock) {
            log.error("Book does have enough stock with bookId={} existingStock={} requestedStock={}",
                    bookId, bookOpt.get().getStock(), stock);
            return null;
        }
        return bookOpt.get();
    }

    @Override
    public void saveBooks(List<Book> bookList) {
        bookRepository.saveAll(bookList);
    }

    @Override
    public BookDto addOrUpdateBook(BookAddRequest bookAddRequest) {

        String bookName = bookAddRequest.getName();
        String writer = bookAddRequest.getWriter();
        Integer publishYear = bookAddRequest.getPublishYear();
        Integer stock = bookAddRequest.getStock();
        Double price = bookAddRequest.getPrice();

        if (bookName == null || writer == null || publishYear == null) {
            log.error("New Book could not be added name={} write={} publishDate={}", bookName,
                    writer, publishYear);
            return null;
        }

        Optional<Book> bookOpt = bookRepository.findByNameAndWriterAndPublishYearAndPrice(bookName, writer, publishYear, price);

        Book book;
        if (bookOpt.isPresent()) {

            book = bookOpt.get();
            book.setUpdatedAt(LocalDateTime.now());
            book.setStock(book.getStock() + stock);

        } else {

             book = Book.builder()
                    .name(bookName)
                    .writer(writer)
                    .publishYear(publishYear)
                    .price(price)
                    .stock(stock)
                    .build();
        }

        Book savedBook = bookRepository.save(book);

        log.info("New Book is added successfully created with name={} write={} publishDate={} stock={} price={}",
                bookName, writer, publishYear, savedBook.getStock(), price);

        return BookDto.fromBook(savedBook);
    }

    @Override
    public BookDto updateBookStock(String bookId, Integer stockValue) {
        Optional<Book> bookOptional = bookRepository.findById(bookId);

        if (bookOptional.isEmpty()) {
            log.error("Book not found with id={}", bookId);
            return null;
        }

        Book book = bookOptional.get();
        book.setStock(stockValue);

        Book updatedBook = bookRepository.save(book);

        log.info("Book is updated successfully id={} stockValue={}", bookId, stockValue);

        return BookDto.fromBook(updatedBook);
    }
}
