package com.nttdata.goods.controller;

import com.nttdata.goods.model.Articulo;
import com.nttdata.goods.service.ArticuloService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/articulos")
public class ArticuloController {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArticuloController.class);

    @Autowired
    private ArticuloService articuloService;

    @PostMapping("/nuevo")
    public ResponseEntity<Object> registrar(@RequestBody Articulo a) {

        try {
            LOGGER.info(String.format("Registro de Articulo -> %s", a.toString()));
            articuloService.registrarArticulo(a);
            return ResponseEntity.ok(a);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/actualizar")
    public ResponseEntity<Object> actualizar(@RequestBody Articulo a) throws Exception {
        try {
            LOGGER.info(String.format("Actualizacion de Proveedor -> %s", a.toString()));
            Articulo proveedor = articuloService.actualizarArticulo(a);
            return ResponseEntity.ok(proveedor);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<Articulo> buscar(@RequestParam("id") int id) throws Exception {
        LOGGER.info(String.format("Buscar de Proveedor"));
        return ResponseEntity.ok(articuloService.buscarArticulo(id));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Articulo>> listar() throws Exception {
        LOGGER.info(String.format("Listar Proveedor"));
        return ResponseEntity.ok(articuloService.listarArticulos());
    }
}
