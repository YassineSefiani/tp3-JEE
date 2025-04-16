package org.example.hopital2;

import org.example.hopital2.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class Hopital2Application implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(Hopital2Application.class, args);
    }


    @Override
    public void run(String... args) throws Exception {
        //patientRepository.save(new Patient(null,"Mohamed",new Date(),false,341));
        //patientRepository.save(new Patient(null,"Hanane",new Date(),false,4321));
        //patientRepository.save(new Patient(null,"Iman",new Date(),true,341));
    }

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
