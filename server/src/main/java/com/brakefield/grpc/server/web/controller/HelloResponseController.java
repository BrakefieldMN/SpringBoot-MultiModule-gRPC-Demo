package com.brakefield.grpc.server.web.controller;

import com.brakefield.grpc.library.HelloReply;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class HelloResponseController {

    @GetMapping("/hello-world")
    String sayHello() {
        return "Hello";
    }

    private static final List<HelloReply> REPLIES = Arrays.asList(
            HelloReply.newBuilder().setMessage("Hello Kylo").build(),
            HelloReply.newBuilder().setMessage("Hello Bella").build(),
            HelloReply.newBuilder().setMessage("Hello Brett").build(),
            HelloReply.newBuilder().setMessage("Hello Krista").build(),
            HelloReply.newBuilder().setMessage("Hello Scott").build(),
            HelloReply.newBuilder().setMessage("Hello Charlie").build(),
            HelloReply.newBuilder().setMessage("Hello Bill").build(),
            HelloReply.newBuilder().setMessage("Hello Philip").build(),
            HelloReply.newBuilder().setMessage("Hello Maren").build(),
            HelloReply.newBuilder().setMessage("Hello Danny").build(),
            HelloReply.newBuilder().setMessage("Hello Brenden").build(),
            HelloReply.newBuilder().setMessage("Hello Jyothi").build()
    );
}
