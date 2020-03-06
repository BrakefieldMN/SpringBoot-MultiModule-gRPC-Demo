package com.brakefield.grpc.client.web.controllers;

import com.brakefield.grpc.client.services.ClientDemoService;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
public class ClientDemoController {

    @GetMapping("/hello-world")
    String sayHello() {
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
}

