package me.adeel.grpc.demo.service;

import io.grpc.ServerBuilder;
import org.lognet.springboot.grpc.GRpcServerBuilderConfigurer;
import org.springframework.stereotype.Component;

@Component
public class GrpcServerBuilderConfigurer extends GRpcServerBuilderConfigurer {

    @Override
    public void configure(ServerBuilder<?> serverBuilder) {
    }

}
