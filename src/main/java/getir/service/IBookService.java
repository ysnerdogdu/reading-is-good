package getir.service;

import getir.controller.payload.request.BookAddRequest;
import getir.controller.payload.response.BookDto;
import getir.model.Book;

import java.util.List;

public interface IBookService {

    Book getBookWithStock(String bookId, Integer stock);

    void saveBooks(List<Book> bookList);

    BookDto addOrUpdateBook(BookAddRequest bookAddRequest);

    BookDto updateBookStock(String bookId, Integer stockValue);
}
