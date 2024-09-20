package com.thread.demo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Entity
@Table(name = "registro")
public class Registro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String nombrecompleto;

    private String productor;

    private String consumidor;

    public Registro() {}

    public Registro(String nombrecompleto, String productor, String consumidor) {
        this.nombrecompleto = nombrecompleto;
        this.productor = productor;
        this.consumidor = consumidor;
    }


}
