package com.brakefield.grpc.client.services;


import com.brakefield.grpc.library.GreeterGrpc;
import com.brakefield.grpc.library.GreeterOuterClass;
import io.grpc.Channel;
import io.grpc.StatusRuntimeException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ClientDemoService {

    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    public ClientDemoService(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    public void greet(String name) {
        log.info("Will try to greet " + name + " ...");
        GreeterOuterClass.HelloRequest request = GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
        GreeterOuterClass.HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: {0}", e.getStatus());
            return;
        }
        log.info("Greeting: " + response.getMessage());
    }

}
