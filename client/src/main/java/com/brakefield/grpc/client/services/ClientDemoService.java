package com.brakefield.grpc.client.services;


import com.brakefield.grpc.library.*;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;

@Slf4j
public class ClientDemoService {

    private final GreeterGrpc.GreeterBlockingStub blockingStub;
    private final StreamingGreeterGrpc.StreamingGreeterStub asyncStub;


    public ClientDemoService(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = GreeterGrpc.newBlockingStub(channel);
        asyncStub = StreamingGreeterGrpc.newStub(channel);
    }

    public void greet(String name) {
        log.info("Will try to greet " + name + " ...");
        GreeterOuterClass.HelloRequest request = GreeterOuterClass.HelloRequest.newBuilder().setName(name).build();
        GreeterOuterClass.HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            log.error("RPC failed: {}", e.getStatus());
            return;
        }
        log.info("Greeting: " + response.getMessage());
    }

    public void streamingGreet(Integer number) {
        log.info("Will try to greet " + number + "people.");
        final CountDownLatch finishLatch = new CountDownLatch(number);
        int i = 0;
        while( i < number) {

            StreamObserver<HelloReply> request = new StreamObserver<>() {
                @Override
                public void onNext(HelloReply value) {
                    log.info("Still " + finishLatch.getCount() + "messages to process");
                    finishLatch.countDown();
                }

                @Override
                public void onError(Throwable t) {
                    log.error("Message route failed: {}", Status.fromThrowable(t));
                    finishLatch.countDown();
                }

                @Override
                public void onCompleted() {
                    log.info("Finished messages route");
                    finishLatch.countDown();
                }
            };
            StreamObserver<HelloRequest> requestObserver = asyncStub.sayHelloStreaming(request);
            try {
                requestObserver.onNext(HelloRequest.newBuilder().setName("Got " + finishLatch.getCount()).build());
                if (finishLatch.getCount() == 0) {
                    return;
                }
            } catch (RuntimeException e) {
                log.error("RPC failed: {}", e);
                throw e;
            }
            i++;
        }
    }

}
