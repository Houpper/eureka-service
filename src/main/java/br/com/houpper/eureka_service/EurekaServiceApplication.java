package br.com.houpper.eureka_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Esta aplicação atua como um servidor Eureka, permitindo que serviços se registrem e localizem outros serviços
 * disponíveis no ambiente.
 */
@EnableEurekaServer
@SpringBootApplication
public class EurekaServiceApplication {

	/**
	 * Inicializa o servidor de descoberta de serviços da aplicação.
	 *
	 * @param args Os argumentos da linha de comando.
	 */
	public static void main(String[] args) {
		SpringApplication.run(EurekaServiceApplication.class, args);
	}
}