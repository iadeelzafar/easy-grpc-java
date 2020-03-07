package me.adeel.grpc.demo.service.demo.api;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import me.adeel.grpc.UserServiceGrpc;
import me.adeel.grpc.UserServiceGrpc.UserServiceBlockingStub;
import me.adeel.grpc.UserServiceGrpc.UserServiceFutureStub;
import me.adeel.grpc.UserServiceGrpc.UserServiceStub;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceHelper {

    private final LoadBalancerClient loadBalancerClient;

    private final ConcurrentHashMap<ServiceInstance, ManagedChannel> MANAGED_CHANNELS = new ConcurrentHashMap<>();

    public ServiceHelper(LoadBalancerClient loadBalancerClient) {
        this.loadBalancerClient = loadBalancerClient;
    }

    private ManagedChannel managedChannel(ServiceInstance serviceInstance) {
        if (MANAGED_CHANNELS.contains(serviceInstance))
            return MANAGED_CHANNELS.get(serviceInstance);
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress(serviceInstance.getHost(), serviceInstance.getPort())
                .usePlaintext(true)
                .build();
        MANAGED_CHANNELS.put(serviceInstance, managedChannel);
        return managedChannel;
    }

    public UserServiceStub userServiceStub(String serviceId) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        Objects.requireNonNull(serviceInstance, "Val3" + serviceId);
        ManagedChannel managedChannel = managedChannel(serviceInstance);
        return UserServiceGrpc.newStub(managedChannel);
    }

    public UserServiceBlockingStub userServiceBlockingStub(String serviceId) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        Objects.requireNonNull(serviceInstance, "Val" + serviceId);
        ManagedChannel managedChannel = managedChannel(serviceInstance);
        return UserServiceGrpc.newBlockingStub(managedChannel);
    }

    public UserServiceFutureStub userServiceFutureStub(String serviceId) {
        ServiceInstance serviceInstance = loadBalancerClient.choose(serviceId);
        Objects.requireNonNull(serviceInstance, "Val2" + serviceId);
        ManagedChannel managedChannel = managedChannel(serviceInstance);
        return UserServiceGrpc.newFutureStub(managedChannel);
    }
}
