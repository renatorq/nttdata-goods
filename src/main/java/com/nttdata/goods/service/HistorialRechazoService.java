package com.nttdata.goods.service;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;

public interface HistorialRechazoService {

    void registrarHistorial(DetalleOrdenDTO dto, String motivoRechazo);

}
