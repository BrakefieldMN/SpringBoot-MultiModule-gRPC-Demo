package com.brakefield.grpc.server.services;

import com.brakefield.grpc.library.HelloReply;
import com.brakefield.grpc.library.HelloRequest;
import com.brakefield.grpc.library.StreamingGreeterGrpc;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

@Slf4j
@GRpcService(interceptors = {LogInterceptor.class})
public class StreamingGreeterService extends StreamingGreeterGrpc.StreamingGreeterImplBase {

    @Override
    public StreamObserver<HelloRequest> sayHelloStreaming(StreamObserver<HelloReply> replyStreamObserver) {
        return new StreamObserver<>() {
            private long count = 0;

            @Override
            public void onNext(HelloRequest value) {
                count++;
                replyStreamObserver.onNext(HelloReply.newBuilder().setMessage("Hello to you too, " + value.getName()).build());
            }

            @Override
            public void onError(Throwable t) {
                replyStreamObserver.onError(t);
            }

            @Override
            public void onCompleted() {
                replyStreamObserver.onNext(HelloReply.newBuilder().setMessage("Hello counter: " + count).build());
                replyStreamObserver.onCompleted();
            }
        };
    }
}
