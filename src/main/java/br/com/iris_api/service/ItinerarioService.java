package br.com.iris_api.service;


import br.com.iris_api.repository.ItinerarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItinerarioService {

    @Autowired
    private ItinerarioRepository itierarioRepository;
}
