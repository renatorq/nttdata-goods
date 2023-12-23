package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenEntradaDTO;
import com.nttdata.goods.dto.RespuestaOrdenDTO;
import com.nttdata.goods.kafka.KafkaProducerImpl;
import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.OrdenEntrada;
import com.nttdata.goods.repository.ArticuloRepository;
import com.nttdata.goods.repository.OrdenEntradaRepository;
import com.nttdata.goods.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private OrdenEntradaRepository ordenEntradaRepository;

//    @Autowired
//    private KafkaProducerImpl kafkaProducerImpl;

    @Override
    public List<Articulo> listarArticulos() throws Exception {
        return articuloRepository.findAll();
    }

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

//        RespuestaOrdenDTO respuesta = new RespuestaOrdenDTO();

        Optional<Articulo> articuloExiste = articuloRepository.findById(dto.getIdArticulo());
        Optional<OrdenEntrada> ordenEntrada = ordenEntradaRepository.findById(dto.getIdOrdenEntrada());

        OrdenEntrada orden = ordenEntrada.get();

        if (articuloExiste.isEmpty()) {

            orden.setEstado("RECHAZADO");
            ordenEntradaRepository.save(orden);

//            respuesta.setIdOrdenEntrada(dto.getIdOrdenEntrada());
//            respuesta.setRespuesta("ERROR");

        } else {

            Articulo articulo = articuloExiste.get();

            Integer nuevoStock = this.calcularStock(articulo.getStock(), dto.getCantidad());

            if (nuevoStock < 0) {
                orden.setEstado("RECHAZADO");
                ordenEntradaRepository.save(orden);

            } else {
                articulo.setStock(articulo.getStock() + dto.getCantidad());
                articuloRepository.save(articulo);

                orden.setEstado("ACEPTADO");
                ordenEntradaRepository.save(orden);
            }

//            respuesta.setIdOrdenEntrada(dto.getIdOrdenEntrada());
//            respuesta.setRespuesta("OK");

        }

//        kafkaProducerImpl.sendMessage(respuesta);

    }

    private Integer calcularStock(Integer stockArticulo, Integer stockOrden) {

        Integer total = stockArticulo + stockOrden;

        return total;
    }

}
