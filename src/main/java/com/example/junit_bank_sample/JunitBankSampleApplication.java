package com.example.junit_bank_sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class JunitBankSampleApplication {

    public static void main(String[] args) {
//        ConfigurableApplicationContext context = SpringApplication.run(JunitBankSampleApplication.class, args);
//
//        String[] beanName = context.getBeanDefinitionNames();
//
//        for (String bean: beanName) {
//            System.out.println(bean);
//        }

        SpringApplication.run(JunitBankSampleApplication.class, args);

    }

}
