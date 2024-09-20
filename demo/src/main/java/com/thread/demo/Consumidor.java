package com.thread.demo;

import com.thread.demo.model.Registro;
import com.thread.demo.repository.RegistroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Component
public class Consumidor implements Runnable{
    private  final  BufferCompartido buffer;

    private final RegistroRepository registroRepository;

    private static final Lock lock = new ReentrantLock();

    @Autowired
    public Consumidor(BufferCompartido buffer, RegistroRepository registroRepository) {
        this.buffer = buffer;
        this.registroRepository = registroRepository;
    }

    @Override
    public void run() {
        try {
            while (true){
                String mensaje = buffer.consumir();

                if(mensaje.startsWith("End")){
                    System.out.println("Finalizado");
                    break;
                }
                lock.lock();
                try {
                    String[] partes = mensaje.split(",",3);
                    if(partes.length == 3){
                        String nombreCompleto = partes[0] + " " + partes[1];
                        String idProductor = partes[2];
                        String salida = nombreCompleto + "," + idProductor + "," + Thread.currentThread().getName();
                        Registro registro = new Registro(nombreCompleto, idProductor, Thread.currentThread().getName());
                        registroRepository.save(registro);
                        System.out.println("Consumido por: " + Thread.currentThread().getName() + ":" + salida);
                    }
                }finally{
                    lock.unlock();
                }
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }
}
