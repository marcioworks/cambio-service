package br.com.marcio.cambioservice.repository;

import br.com.marcio.cambioservice.model.Cambio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CambioRepository extends JpaRepository<Cambio,Long> {

    Cambio findByFromAndTo(String from, String to);
}
