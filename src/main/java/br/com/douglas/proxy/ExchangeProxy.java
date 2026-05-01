package br.com.douglas.proxy;

import br.com.douglas.dto.ExchangeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.math.BigDecimal;

@FeignClient(name = "exchange-service")
public interface ExchangeProxy {

    @GetMapping(value = "/exchange-service/{amount}/{from}/{to}")
    public ExchangeDto getExchange(@PathVariable("amount") Double amount, @PathVariable("from") String from, @PathVariable("to") String to);
}
