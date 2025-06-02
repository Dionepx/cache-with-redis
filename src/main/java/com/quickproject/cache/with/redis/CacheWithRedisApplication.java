package com.quickproject.cache.with.redis;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@SpringBootApplication
@EnableCaching
public class CacheWithRedisApplication {

	public static void main(String[] args) {
		SpringApplication.run(CacheWithRedisApplication.class, args);
	}

	@Bean
	ApplicationRunner cacheVerificationRoutine(ProductCatalogService productCatalogService) {
		return args -> {
			System.out.println("\n\n--- Iniciando Verificação de Cache ---");
			System.out.println("Produto Chave: 1: " + productCatalogService.findProductRecordById(1L));
			System.out.println("Produto Chave: 2: " + productCatalogService.findProductRecordById(2L));
			System.out.println("Produto Chave: 1: " + productCatalogService.findProductRecordById(1L));
			System.out.println("Produto Chave: 1: " + productCatalogService.findProductRecordById(1L));
			System.out.println("Produto Chave: 1: " + productCatalogService.findProductRecordById(1L));
			System.out.println("--- Verificação de Cache Concluída ---\n\n");
		};
	}
}

record ProductRecord(Long recordId, String productName, String productSpec) implements Serializable {
}

@Service
class ProductCatalogService {
	Map<Long, ProductRecord> productDataStore = new LinkedHashMap<>() {
		{
			put(1L, new ProductRecord(1L, "Dispositivo Móvel", "Smartphone Ultra"));
			put(2L, new ProductRecord(2L, "Periférico de Áudio", "Fone Bluetooth ANC"));
			put(3L, new ProductRecord(3L, "Componente Eletrônico", "Placa de Vídeo RTX"));
			put(4L, new ProductRecord(4L, "Acessório Conectado", "Smartwatch Esportivo"));
			put(5L, new ProductRecord(5L, "Armazenamento Externo", "SSD Portátil Rápido"));
		}
	};

	@Cacheable("productRecords")
	ProductRecord findProductRecordById(Long keyId) {
		System.out.println("Consultando a fonte de dados primária...");
		applyArtificialDelay();
		return productDataStore.get(keyId);
	}

	private void applyArtificialDelay() {
		try {
			long delayDurationMs = 2000L;
			Thread.sleep(delayDurationMs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			throw new IllegalStateException("O atraso simulado foi interrompido inesperadamente.", e);
		}
	}
}
