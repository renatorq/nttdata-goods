package com.nttdata.goods.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Orden {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idOrden;

    @Column(name = "fecharegistro", nullable = false)
    private LocalDate fecharegistro;

    @Column(name = "estado")
    private String estado;

    @Column(name = "tipoOrden", nullable = false)
    private String tipoOrden;

    @Column(name = "idProveedor")
    private Integer idProveedor;

}
