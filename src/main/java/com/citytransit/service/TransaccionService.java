package com.citytransit.service;

import com.citytransit.model.entity.Transaccion;
import com.citytransit.repository.TransaccionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransaccionService {

    private final TransaccionRepository transaccionRepository;

    public Transaccion save(Transaccion transaccion) {
        return transaccionRepository.save(transaccion);
    }
}
