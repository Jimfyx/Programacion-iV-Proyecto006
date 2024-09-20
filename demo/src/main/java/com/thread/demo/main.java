package com.thread.demo;

import com.thread.demo.repository.RegistroRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class main {
    public static void main(String[] args) {
        SpringApplication.run(main.class, args);
    }
    @Bean
    public BufferCompartido bufferCompartido(){
        return new BufferCompartido(10);
    };

    @Bean
    public CommandLineRunner run(ApplicationContext context) {
        return args -> {
            BufferCompartido buffer = context.getBean(BufferCompartido.class);
            RegistroRepository registroRepository = context.getBean(RegistroRepository.class);


            Thread productor1 = new Thread(new Productor(buffer, "input1.csv", "productor1"));
            Thread productor2 = new Thread(new Productor(buffer, "input2.csv", "productor2"));
            Thread productor3 = new Thread(new Productor(buffer, "input3.csv", "productor3"));

            productor1.start();
            productor2.start();
            productor3.start();

            Thread consumidor1 = null;
            Thread consumidor2 = null;
            Thread consumidor3 = null;

            try {
                consumidor1 = new Thread(new Consumidor(buffer, registroRepository));
                consumidor1.setName("consumidor1");
                consumidor2 = new Thread(new Consumidor(buffer, registroRepository));
                consumidor2.setName("consumidor2");
                consumidor3 = new Thread(new Consumidor(buffer, registroRepository));
                consumidor3.setName("consumidor3");

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            if (consumidor1 != null) {
                consumidor1.start();
            }
            if (consumidor2 != null) {
                consumidor2.start();
            }
            if (consumidor3 != null) {
                consumidor3.start();
            }

            try {
                productor1.join();
                productor2.join();
                productor3.join();

                if (consumidor1 != null) {
                    consumidor1.join();
                }
                if (consumidor2 != null) {
                    consumidor2.join();
                }
                if (consumidor3 != null) {
                    consumidor3.join();
                }

            }catch (InterruptedException ex){
                Thread.currentThread().interrupt();
            }
        };
    }
}
