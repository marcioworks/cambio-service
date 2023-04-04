package br.com.marcio.cambioservice.controller;

import br.com.marcio.cambioservice.model.Cambio;
import br.com.marcio.cambioservice.repository.CambioRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Tag(name = "Cambio service endpoints")
@RestController
@RequestMapping("cambio-service")
public class CambioController {

    @Autowired
    private Environment environment;

    @Autowired
    CambioRepository cambioRepository;

    private Logger logger = LoggerFactory.getLogger(CambioController.class);

    @Operation(summary = "Get cambio from currency")
    @GetMapping(value = "/{amount}/{from}/{to}")
    public Cambio getCambio(@PathVariable("amount") BigDecimal amount,
                            @PathVariable("from") String from,
                            @PathVariable("to") String to) {


        logger.info("getCambio was called with -> {}, {}, and {}", amount, from, to);
        Cambio cambio = cambioRepository.findByFromAndTo(from, to);
        if (cambio == null) {
            throw new RuntimeException("currency unsupported");
        }
        BigDecimal conversionFactor = cambio.getConversionFactor();
        BigDecimal convertedValue = conversionFactor.multiply(amount);
        var port = environment.getProperty("local.server.port");
        cambio.setConvertedValue(convertedValue.setScale(2, RoundingMode.CEILING));
        cambio.setEnvironment(port);
        return cambio;
    }
}
