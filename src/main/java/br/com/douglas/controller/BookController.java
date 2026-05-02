package br.com.douglas.controller;

import br.com.douglas.dto.ExchangeDto;
import br.com.douglas.environment.InstanceInformationService;
import br.com.douglas.model.Book;
import br.com.douglas.proxy.ExchangeProxy;
import br.com.douglas.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    InstanceInformationService informationService;

    @Autowired
    BookRepository repository;

    @Autowired
    ExchangeProxy exchangeProxy;

    @Operation(summary = "Find a specific book by ID")
    @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        return repository.findById(id)
                .<ResponseEntity<Book>>map(book -> {

                    ExchangeDto exchange = exchangeProxy.getExchange(book.getPrice(), "USD", currency);

                    String port = informationService.retrieveServerPort();

                    book.setCurrency(currency);
                    book.setPrice(exchange.getConvertedValue());
                    //book.setEnvironment("PORT: " + informationService.retrieveServerPort());
                    book.setEnvironment("BOOK PORT: " + port + " | EXCHANGE PORT: " + exchange.getEnvironment());

                    return ResponseEntity.ok(book);
                })
                .orElse(ResponseEntity.<Book>notFound().build());
    }
}
