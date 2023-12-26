package com.nttdata.goods.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistorialPrecio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;

    @Column(name = "idArticulo")
    private Integer idArticulo;

    @Column
    private LocalDateTime fechaRegistro;

    @Column
    private BigDecimal precioUnitario;

}
