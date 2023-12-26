package com.nttdata.goods.service.impl;


import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.HistorialPrecio;
import com.nttdata.goods.repository.HistorialPrecioRepository;
import com.nttdata.goods.service.HistorialPrecioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialPrecioServiceImpl implements HistorialPrecioService {

    @Autowired
    private HistorialPrecioRepository precioRepository;

    @Override
    public void RegistroHistorialPrecioArticulo(Articulo articulo) {
        HistorialPrecio hp = new HistorialPrecio();
        hp.setIdArticulo(articulo.getIdArticulo());
        hp.setPrecioUnitario(articulo.getPrecio());
        hp.setFechaRegistro(LocalDateTime.now());
        precioRepository.save(hp);
    }
}
