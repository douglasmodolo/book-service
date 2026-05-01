package br.com.douglas.controller;

import br.com.douglas.dto.ExchangeDto;
import br.com.douglas.environment.InstanceInformationService;
import br.com.douglas.model.Book;
import br.com.douglas.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;

@RestController
@RequestMapping("book-service")
public class BookController {

    @Autowired
    InstanceInformationService informationService;

    @Autowired
    BookRepository repository;

    @Autowired
    RestTemplate restTemplate;

    @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Book> findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        return repository.findById(id)
                .<ResponseEntity<Book>>map(book -> {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("amount", book.getPrice().toString());
                    params.put("from", "USD");
                    params.put("to", currency);

                    var exchange = restTemplate
                            .getForEntity("http://localhost:8000/exchange-service/{amount}/{from}/{to}", ExchangeDto.class, params)
                            .getBody();

                    if (exchange == null) {
                        return ResponseEntity.<Book>status(HttpStatus.BAD_GATEWAY).build();
                    }

                    book.setCurrency(currency);
                    book.setPrice(exchange.getConvertedValue());
                    book.setEnvironment("PORT: " + informationService.retrieveServerPort());

                    return ResponseEntity.ok(book);
                })
                .orElse(ResponseEntity.<Book>notFound().build());
    }
}
