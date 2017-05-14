package com.maintainwind.demo.hazelcastdemo;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.ReplicatedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootApplication
@EnableCaching
@RestController
public class HazelcastDemoApplication implements CommandLineRunner {

	@Autowired
	HazelcastInstance hazelcastInstance;


	public static void main(String[] args) {
//		SpringApplication.run(HazelcastDemoApplication.class);
		new SpringApplicationBuilder()
				.profiles("member")
				.sources(HazelcastDemoApplication.class)
				.run(args);
	}

	@Override
	public void run(String... strings) throws Exception {
		for(String s: strings) {
			System.out.println("parmam: " + s);
		}


		if(strings.length > 0) {

			System.out.println("populate test values to hazelcast cache\n");
			IMap<String,String> map = hazelcastInstance.getMap("test");

			for(int i = 0; i<10000; i++) {
				map.put("test"+i, "test value test value" + i);
			}

			System.out.println("map size = " + map.size());
		}
	}


	@GetMapping("/data")
	public String getTestData() {
		return "test string";
	}


	@GetMapping("/test")
	public List<String> getData() {
		List<String> results = new ArrayList<>();


		IMap<String,String> map = hazelcastInstance.getMap("test");

		System.out.println("Map size = " + map.size());

		for(ReplicatedMap.Entry<String, String> entry: map.entrySet()) {
//			System.out.println(entry.getValue());
			results.add(entry.getValue());

		}

		return results;
	}


	@PostMapping("/data")
	public void putDataToCache() {

		IMap<String,String> map = hazelcastInstance.getMap("test");

		int mapSize = map.size();

		for(int i = mapSize; i <mapSize + 100; i++) {
			map.set("test"+i, "test value test value" + i);
		}

		System.out.println("Map size: " + map.size());


	}

}
