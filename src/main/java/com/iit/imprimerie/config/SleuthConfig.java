package com.iit.imprimerie.config;

import brave.sampler.Sampler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SleuthConfig {

// alwayes_sample demande que toutes les requêtes soient marquées par des ID et soient exportables vers d'autres services comme Zipkin.
    public Sampler defaultSampler(){
        return Sampler.ALWAYS_SAMPLE;
    }
}