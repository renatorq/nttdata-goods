package com.nttdata.goods.service.impl;

import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.repository.ArticuloRepository;
import com.nttdata.goods.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Override
    public List<Articulo> listarArticulos() throws Exception {
        return articuloRepository.findAll();
    }

    @Override
    public Articulo registrar(Articulo articulo) throws Exception {

        Optional<Articulo> articuloLocal = articuloRepository.findById(articulo.getIdArticulo());

        if (articuloLocal.isEmpty()) {
            System.out.println("EL articulo no existe!");
            throw new Exception("EL articulo no existe");
        }

        Articulo articuloOK = articuloRepository.save(articulo);

        return articuloOK;
    }

    @Override
    public Articulo actualizar(Articulo articulo) throws Exception {
        return null;
    }
}
