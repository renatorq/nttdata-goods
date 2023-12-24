package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.Orden;
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

    public void actualizarStockArticulo(DetalleOrdenDTO dto) throws Exception {

        Optional<Articulo> articuloExiste = articuloRepository.findById(dto.getIdArticulo());
        Optional<Orden> orden = ordenEntradaRepository.findById(dto.getIdOrden());

        Orden ordenEncontrada = orden.get();

        if (articuloExiste.isEmpty()) {

            ordenEncontrada.setEstado("RECHAZADO");
            ordenEntradaRepository.save(ordenEncontrada);

        } else {

            Articulo articulo = articuloExiste.get();

            Integer nuevoStock = this.calcularStock(articulo.getStock(), dto);

            if (nuevoStock < 0) {
                ordenEncontrada.setEstado("RECHAZADO");
                ordenEntradaRepository.save(ordenEncontrada);

            } else {
                articulo.setStock(nuevoStock);
                articuloRepository.save(articulo);

                ordenEncontrada.setEstado("ACEPTADO");
                ordenEntradaRepository.save(ordenEncontrada);
            }
        }
    }

    private Integer calcularStock(Integer stockArticulo, DetalleOrdenDTO dto) {

        Integer total = 0;

        if (dto.getTipoOrden().equalsIgnoreCase("ENTRADA")) {
            total = stockArticulo + dto.getCantidad();
        }
        if (dto.getTipoOrden().equalsIgnoreCase("SALIDA")) {
            total = stockArticulo - dto.getCantidad();
        }

        return total;
    }

}
