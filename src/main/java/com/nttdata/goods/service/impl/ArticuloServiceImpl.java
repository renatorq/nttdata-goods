package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.Orden;
import com.nttdata.goods.repository.ArticuloRepository;
import com.nttdata.goods.repository.OrdenEntradaRepository;
import com.nttdata.goods.service.ArticuloService;
import com.nttdata.goods.service.HistorialPrecioService;
import com.nttdata.goods.service.HistorialRechazoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import static com.nttdata.goods.utils.Constantes.*;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private OrdenEntradaRepository ordenEntradaRepository;

    @Autowired
    private HistorialRechazoService rechazoService;

    @Autowired
    private HistorialPrecioService precioService;

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

        if (ordenEncontrada.getEstado() != null && ordenEncontrada.getEstado().equalsIgnoreCase(ESTADO_ORDEN_RECHAZADO)) {
            return;
        }

        if (articuloExiste.isEmpty()) {

            if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                ordenEncontrada.setEstado(ESTADO_ORDEN_RECHAZADO);
                ordenEntradaRepository.save(ordenEncontrada);
            }
            rechazoService.registrarHistorial(dto, MOTIVO_RECHAZO_ARTICULO);

        } else {

            Articulo articulo = articuloExiste.get();

            Integer nuevoStock = this.calcularStock(articulo.getStock(), dto);

            if (nuevoStock < 0) {
                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    ordenEncontrada.setEstado(ESTADO_ORDEN_RECHAZADO);
                    ordenEntradaRepository.save(ordenEncontrada);
                }
                ordenEntradaRepository.save(ordenEncontrada);
                rechazoService.registrarHistorial(dto, MOTIVO_RECHAZO_STOCK);

            } else {
                articulo.setStock(nuevoStock);

                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    if (articulo.getPrecio().compareTo(dto.getPrecioUnitario()) != 0) {
                        articulo.setPrecio(dto.getPrecioUnitario());
                        precioService.RegistroHistorialPrecioArticulo(articulo);
                    }

                }

                articuloRepository.save(articulo);

                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    ordenEncontrada.setEstado(ESTADO_ORDEN_ACEPTADO);
                }
                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_AN)) {
                    ordenEncontrada.setEstado(ESTADO_ORDEN_ANULADO);
                }
                ordenEntradaRepository.save(ordenEncontrada);
            }
        }
    }

    private static Integer calcularStock(Integer stockArticulo, DetalleOrdenDTO dto) {
        BiFunction<Integer, Integer, Integer> calcular = (stock, cantidad) ->
                dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_ENTRADA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE) ||
                        dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_SALIDA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_AN) ?
                        stock + cantidad : stock - cantidad;

        return calcular.apply(stockArticulo, dto.getCantidad());
    }
}
