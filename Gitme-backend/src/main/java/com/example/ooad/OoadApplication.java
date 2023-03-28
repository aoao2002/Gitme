package com.example.ooad;

import com.zaxxer.hikari.HikariDataSource;
import com.zaxxer.hikari.pool.HikariPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.DirectFieldAccessor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.Resource;
import java.sql.Connection;

@SpringBootApplication
@RestController
@EnableScheduling
@EnableCaching
@EnableSwagger2
public class OoadApplication implements CommandLineRunner {

	@Resource
	private HikariDataSource dataSource;

	private static final Logger logger = LoggerFactory.getLogger(OoadApplication.class);

	@Override
	public void run(String... args) throws Exception {
		try(Connection conn = dataSource.getConnection()) {
			System.out.println(conn);
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(OoadApplication.class, args);
	}

//	@Scheduled(fixedRate = 1000)
//	public void HikariMonitor() {
//		HikariPool hikariPool = (HikariPool)
//				new DirectFieldAccessor(dataSource).getPropertyValue("pool");
//
//		logger.info("HikariPoolState = "
//				+ "Active=[" + String.valueOf(hikariPool.getActiveConnections() + "] "
//				+ "Idle=[" + String.valueOf(hikariPool.getIdleConnections() + "] "
//				+ "Wait=["+hikariPool.getThreadsAwaitingConnection()+"] "
//				+ "Total=["+hikariPool.getTotalConnections()+"]")));
//
//	}

	public static Logger getLogger() {
		return logger;
	}
}
