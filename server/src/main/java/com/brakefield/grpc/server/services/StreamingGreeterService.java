package com.brakefield.grpc.server.services;

import com.brakefield.grpc.library.HelloReply;
import com.brakefield.grpc.library.HelloRequest;
import com.brakefield.grpc.library.StreamingGreeterGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@GRpcService(interceptors = {LogInterceptor.class})
public class StreamingGreeterService extends StreamingGreeterGrpc.StreamingGreeterImplBase {

    @Override
    public StreamObserver<HelloRequest> sayHelloStreaming(StreamObserver<HelloReply> replyStreamObserver) {
        return new StreamObserver<>() {

            @Override
            public void onNext(HelloRequest value) {
                int random = ThreadLocalRandom.current().nextInt(0, 10 + 1);
                log.info("Saying hello to: " + REPLIES.get(random));
                replyStreamObserver.onNext(REPLIES.get(random));
            }

            @Override
            public void onError(Throwable t) {
                replyStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                replyStreamObserver.onCompleted();
            }
        };
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
