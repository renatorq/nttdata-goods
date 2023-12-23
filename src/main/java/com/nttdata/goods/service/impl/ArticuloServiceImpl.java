package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenEntradaDTO;
import com.nttdata.goods.dto.RespuestaOrdenDTO;
import com.nttdata.goods.kafka.KafkaProducerImpl;
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

    @Autowired
    private KafkaProducerImpl kafkaProducerImpl;

    @Override
    public void registrarArticulo(Articulo articulo) throws Exception {

        Optional<Articulo> articuloLocal = articuloRepository.findById(articulo.getIdArticulo());

        if (articuloLocal.isEmpty()) {
            throw new Exception("EL articulo no se encuentra registrado");
        }
        articuloRepository.save(articulo);
    }

    @Override
    public Articulo actualizarArticulo(Articulo articulo) throws Exception {
        Optional<Articulo> articuloLocal = articuloRepository.findById(articulo.getIdArticulo());

        if (articuloLocal.isEmpty()) {
            throw new Exception("EL articulo no se encuentra registrado!");
        }

        return articuloRepository.save(articulo);
    }

    @Override
    public Articulo buscarArticulo(int id) throws Exception {
        Optional<Articulo> op = articuloRepository.findById(id);
        return op.isPresent() ? op.get() : new Articulo();
    }

    public void actualizarStockArticulo(DetalleOrdenEntradaDTO dto) throws Exception {

        RespuestaOrdenDTO respuesta = new RespuestaOrdenDTO();

        Optional<Articulo> articuloExiste = articuloRepository.findById(dto.getIdArticulo());

        if (articuloExiste.isEmpty()) {

            respuesta.setIdOrdenEntrada(dto.getIdOrdenEntrada());
            respuesta.setRespuesta("ERROR");

        } else {
            Articulo articulo = articuloExiste.get();
            articulo.setStock(articulo.getStock() + dto.getCantidad());
            articuloRepository.save(articulo);

            respuesta.setIdOrdenEntrada(dto.getIdOrdenEntrada());
            respuesta.setRespuesta("OK");

        }

        kafkaProducerImpl.sendMessage(respuesta);

    }
}
