package com.nttdata.goods.service;

import com.nttdata.goods.model.Articulo;

import java.util.List;

public interface ArticuloService {

    List<Articulo> listarArticulos() throws Exception;

    Articulo registrar(Articulo articulo) throws Exception;

    Articulo actualizar(Articulo articulo) throws  Exception;

}
