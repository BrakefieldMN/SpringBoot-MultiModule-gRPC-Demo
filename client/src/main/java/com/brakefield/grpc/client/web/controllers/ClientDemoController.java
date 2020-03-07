package com.brakefield.grpc.client.web.controllers;

import com.brakefield.grpc.client.services.ClientDemoService;
import com.brakefield.grpc.library.HelloReply;
import com.google.common.base.Stopwatch;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

@RestController
public class ClientDemoController {


    @GetMapping("/hello-world")
    String sayHello() {

        return "Hello world!";
    }


    @GetMapping("/hello-world-multiple/{number}")
    String sayHelloMultipleTimes(@PathVariable(value = "number") Integer number) {

        RestTemplate restTemplate = new RestTemplate();
        Stopwatch startime = Stopwatch.createStarted();
        for (int i = 0; i < number; i++) {
            String quote = restTemplate.getForObject(
                    "http://localhost:8080/hello-world", String.class);
            System.out.println(i);
            System.out.println(quote);
        }

        System.out.println("Using JSON took " + startime.stop().elapsed(TimeUnit.MILLISECONDS));

        return "Hello world!";
    }


    @GetMapping("/grpc-test/{user}")
    void testGrpcGreet(@PathVariable(value = "user") String user) throws InterruptedException {
        String target = "localhost:6565";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
        try {
            ClientDemoService clientDemoService = new ClientDemoService(channel);
            clientDemoService.greet(user);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @GetMapping("/grpc-test-async/{userrequests}")
    void testGrpcGreet(@PathVariable(value = "userrequests") Integer userrequests) throws InterruptedException {
        String target = "localhost:6565";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target)
                // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                // needing certificates.
                .usePlaintext()
                .build();
//        try {
            ClientDemoService clientDemoService = new ClientDemoService(channel);
            clientDemoService.streamingGreet(userrequests);
//        }
//        finally {
//            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
//            // resources the channel should be shut down when it will no longer be used. If it may be used
//            // again leave it running.
//            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
//        }
    }
}

