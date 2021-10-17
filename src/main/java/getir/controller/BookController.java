package getir.controller;

import getir.controller.payload.request.BookAddRequest;
import getir.controller.payload.response.BookDto;
import getir.service.IBookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin
@RestController
@RequestMapping(value = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class BookController {

    private final IBookService bookService;

    @PostMapping("/create")
    public ResponseEntity<BookDto> createBook(@RequestBody BookAddRequest bookAddRequest){

        log.info("Add new book request is received");

        try {
            BookDto bookDto = bookService.addOrUpdateBook(bookAddRequest);

            if (bookDto != null) {
                return new ResponseEntity<>(bookDto, HttpStatus.CREATED);
            } else {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping ("/update/{bookId}")
    public ResponseEntity<BookDto> updateBook(@PathVariable String bookId,
                                              @RequestParam Integer stockCount){

        log.info("Update book stock request is received");

        try {
            BookDto updatedBook = bookService.updateBookStock(bookId, stockCount);

            if (updatedBook != null) {
                return new ResponseEntity<>(updatedBook, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
