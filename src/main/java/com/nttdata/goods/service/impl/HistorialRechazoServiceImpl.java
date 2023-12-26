package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.HistorialRechazo;
import com.nttdata.goods.repository.HistorialRechazoRepository;
import com.nttdata.goods.service.HistorialRechazoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class HistorialRechazoServiceImpl implements HistorialRechazoService {

    @Autowired
    private HistorialRechazoRepository rechazoRepository;

    @Override
    public void registrarHistorial(DetalleOrdenDTO dto, String motivoRechazo) {
        HistorialRechazo hr = new HistorialRechazo();
        hr.setIdOrden(dto.getIdOrden());
        hr.setMotivoRechazo(motivoRechazo);
        hr.setFechaRegistro(LocalDateTime.now());
        rechazoRepository.save(hr);
    }
}
