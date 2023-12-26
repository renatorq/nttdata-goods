package com.nttdata.goods.service.impl;

import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.HistorialPrecio;
import com.nttdata.goods.model.HistorialRechazo;
import com.nttdata.goods.model.Orden;
import com.nttdata.goods.repository.ArticuloRepository;
import com.nttdata.goods.repository.HistorialPrecioRepository;
import com.nttdata.goods.repository.HistorialRechazoRepository;
import com.nttdata.goods.repository.OrdenEntradaRepository;
import com.nttdata.goods.service.ArticuloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.nttdata.goods.utils.Constantes.*;

@Service
public class ArticuloServiceImpl implements ArticuloService {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private OrdenEntradaRepository ordenEntradaRepository;

    @Autowired
    private HistorialRechazoRepository rechazoRepository;

    @Autowired
    private HistorialPrecioRepository precioRepository;

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
            this.RegistroHistorialRechazo(dto, MOTIVO_RECHAZO_ARTICULO);

        } else {

            Articulo articulo = articuloExiste.get();

            Integer nuevoStock = this.calcularStock(articulo.getStock(), dto);

            if (nuevoStock < 0) {
                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    ordenEncontrada.setEstado(ESTADO_ORDEN_RECHAZADO);
                    ordenEntradaRepository.save(ordenEncontrada);
                }
                this.RegistroHistorialRechazo(dto, MOTIVO_RECHAZO_STOCK);

            } else {
                articulo.setStock(nuevoStock);

                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    if (articulo.getPrecio().compareTo(dto.getPrecioUnitario()) != 0) {
                        articulo.setPrecio(dto.getPrecioUnitario());
                        this.RegistroHistorialPrecioArticulo(articulo);
                    }

                }

                articuloRepository.save(articulo);

                if (dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE)) {
                    ordenEncontrada.setEstado(ESTADO_ORDEN_ACEPTADO);
                    ordenEntradaRepository.save(ordenEncontrada);
                }
            }
        }
    }

    private Integer calcularStock(Integer stockArticulo, DetalleOrdenDTO dto) {

        Integer total = 0;

        if ((dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_ENTRADA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE))
                || (dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_SALIDA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_AN))
        ) {
            total = stockArticulo + dto.getCantidad();
        }
        if ((dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_SALIDA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_RE))
                || (dto.getTipoOrden().equalsIgnoreCase(TIPO_ORDEN_ENTRADA) && dto.getTipoOperacion().equalsIgnoreCase(TIPO_OPERACION_AN))
        ) {
            total = stockArticulo - dto.getCantidad();
        }

        return total;
    }

    void RegistroHistorialRechazo(DetalleOrdenDTO dto, String motivoRechazo) {
        HistorialRechazo hr = new HistorialRechazo();
        hr.setIdOrden(dto.getIdOrden());
        hr.setMotivoRechazo(motivoRechazo);
        hr.setFechaRegistro(LocalDateTime.now());
        rechazoRepository.save(hr);
    }

    void RegistroHistorialPrecioArticulo(Articulo articulo) {

        HistorialPrecio hp = new HistorialPrecio();
        hp.setIdArticulo(articulo.getIdArticulo());
        hp.setPrecioUnitario(articulo.getPrecio());
        hp.setFechaRegistro(LocalDateTime.now());
        precioRepository.save(hp);

    }

}
