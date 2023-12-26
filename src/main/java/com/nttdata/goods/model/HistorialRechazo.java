package com.nttdata.goods.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class HistorialRechazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorial;

    @Column
    private Integer idOrden;

    @Column
    private String motivoRechazo;

    @Column
    private LocalDateTime fechaRegistro;

}
