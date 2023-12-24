package com.nttdata.goods.service;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;

import java.util.List;

public interface ArticuloService {
    List<Articulo> listarArticulos() throws Exception;

    void registrarArticulo(Articulo articulo) throws Exception;

    Articulo actualizarArticulo(Articulo articulo) throws Exception;

    Articulo buscarArticulo(int id) throws Exception;

    void actualizarStockArticulo(DetalleOrdenDTO dto) throws Exception;

}
