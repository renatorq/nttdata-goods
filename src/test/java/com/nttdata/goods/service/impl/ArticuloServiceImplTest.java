package com.nttdata.goods.service.impl;


import com.nttdata.goods.dto.DetalleOrdenDTO;
import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.model.Orden;
import com.nttdata.goods.repository.ArticuloRepository;
import com.nttdata.goods.repository.OrdenEntradaRepository;
import com.nttdata.goods.service.HistorialPrecioService;
import com.nttdata.goods.service.HistorialRechazoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ArticuloServiceImplTest {
    @Mock
    private ArticuloRepository articuloRepository;
    @Mock
    private OrdenEntradaRepository ordenEntradaRepository;
    @Mock
    private HistorialRechazoService rechazoService;
    @Mock
    private HistorialPrecioService precioService;

    @InjectMocks
    private ArticuloServiceImpl articuloService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListarArticulos() throws Exception {
        when(articuloRepository.findAll()).thenReturn(Arrays.asList(new Articulo(), new Articulo()));
        List<Articulo> result = articuloService.listarArticulos();
        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void testRegistrarArticuloConArticuloNoRegistrado() {
        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        when(articuloRepository.findById(articulo.getIdArticulo())).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(Exception.class, () -> articuloService.registrarArticulo(articulo));
        assertEquals("EL articulo no se encuentra registrado", excepcion.getMessage());
    }

    @Test
    void testActualizarArticuloConArticuloNoRegistrado() {
        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        when(articuloRepository.findById(articulo.getIdArticulo())).thenReturn(Optional.empty());

        Exception excepcion = assertThrows(Exception.class, () -> articuloService.actualizarArticulo(articulo));
        assertEquals("EL articulo no se encuentra registrado!", excepcion.getMessage());
    }

    @Test
    void testBuscarArticuloConIdValido() throws Exception {
        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        when(articuloRepository.findById(1)).thenReturn(Optional.of(articulo));

        Articulo encontrado = articuloService.buscarArticulo(1);
        assertNotNull(encontrado);
        assertEquals(articulo.getIdArticulo(), encontrado.getIdArticulo());
    }

    @Test
    void testActualizarStockArticulo() throws Exception {
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdArticulo(1);
        dto.setIdOrden(1);
        dto.setTipoOperacion("RE");
        dto.setTipoOrden("ENTRADA");
        dto.setCantidad(10);
        dto.setPrecioUnitario(new BigDecimal(100.0));

        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        articulo.setStock(20);

        Orden orden = new Orden();
        orden.setIdOrden(1);
        orden.setEstado("ACTIVO");

        when(articuloRepository.findById(dto.getIdArticulo())).thenReturn(Optional.of(articulo));
        when(ordenEntradaRepository.findById(dto.getIdOrden())).thenReturn(Optional.of(orden));

        articuloService.actualizarStockArticulo(dto);

        verify(articuloRepository, times(1)).save(articulo);
        verify(ordenEntradaRepository, times(1)).save(orden);
    }

    @Test
    void testRegistrarArticulo() throws Exception {

        when(articuloRepository.findById(any())).thenReturn(Optional.of(mock(Articulo.class,RETURNS_MOCKS)));

        articuloService.registrarArticulo(mock(Articulo.class,RETURNS_MOCKS));
        //verify(articuloRepository, times(1)).save(Optional.of(mock(Articulo.class,RETURNS_MOCKS));
    }

    @Test
    void testActualizarArticulo() throws Exception {

        when(articuloRepository.findById(any())).thenReturn(Optional.of(mock(Articulo.class,RETURNS_MOCKS)));

        articuloService.actualizarArticulo(mock(Articulo.class,RETURNS_MOCKS));

    }

    @Test
    void testActualizarStockArticulo2() throws Exception {
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdArticulo(1);
        dto.setIdOrden(1);
        dto.setTipoOperacion("REGISTRO");
        dto.setTipoOrden("ENTRADA");
        dto.setCantidad(10);
        dto.setPrecioUnitario(new BigDecimal(100.0));

        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        articulo.setStock(20);

        Orden orden = new Orden();
        orden.setIdOrden(1);
        orden.setEstado("ACTIVO");

        when(articuloRepository.findById(dto.getIdArticulo())).thenReturn(Optional.empty());
        when(ordenEntradaRepository.findById(dto.getIdOrden())).thenReturn(Optional.of(orden));

        articuloService.actualizarStockArticulo(dto);


    }


    @Test
    void testActualizarStockArticulo3() throws Exception {
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdArticulo(1);
        dto.setIdOrden(1);
        dto.setTipoOperacion("REGISTRO");
        dto.setTipoOrden("ENTRADA");
        dto.setCantidad(-30);
        dto.setPrecioUnitario(new BigDecimal(100.0));

        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        articulo.setStock(-20);

        Orden orden = new Orden();
        orden.setIdOrden(1);
        orden.setEstado("ACTIVO");

        when(articuloRepository.findById(dto.getIdArticulo())).thenReturn(Optional.of(articulo));
        when(ordenEntradaRepository.findById(dto.getIdOrden())).thenReturn(Optional.of(orden));

        articuloService.actualizarStockArticulo(dto);


    }

    @Test
    void testActualizarStockArticulo4() throws Exception {
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdArticulo(1);
        dto.setIdOrden(1);
        dto.setTipoOperacion("REGISTRO");
        dto.setTipoOrden("ENTRADA");
        dto.setCantidad(10);
        dto.setPrecioUnitario(new BigDecimal(100.0));

        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        articulo.setStock(20);
        articulo.setPrecio(new BigDecimal(30.0));

        Orden orden = new Orden();
        orden.setIdOrden(1);
        orden.setEstado("ACTIVO");

        when(articuloRepository.findById(dto.getIdArticulo())).thenReturn(Optional.of(articulo));
        when(ordenEntradaRepository.findById(dto.getIdOrden())).thenReturn(Optional.of(orden));

        articuloService.actualizarStockArticulo(dto);


    }

    @Test
    void testActualizarStockArticulo5() throws Exception {
        DetalleOrdenDTO dto = new DetalleOrdenDTO();
        dto.setIdArticulo(1);
        dto.setIdOrden(1);
        dto.setTipoOperacion("ANULACION");
        dto.setTipoOrden("ENTRADA");
        dto.setCantidad(10);
        dto.setPrecioUnitario(new BigDecimal(100.0));

        Articulo articulo = new Articulo();
        articulo.setIdArticulo(1);
        articulo.setStock(20);
        articulo.setPrecio(new BigDecimal(30.0));

        Orden orden = new Orden();
        orden.setIdOrden(1);
        orden.setEstado("ACTIVO");

        when(articuloRepository.findById(dto.getIdArticulo())).thenReturn(Optional.of(articulo));
        when(ordenEntradaRepository.findById(dto.getIdOrden())).thenReturn(Optional.of(orden));

        articuloService.actualizarStockArticulo(dto);


    }



}