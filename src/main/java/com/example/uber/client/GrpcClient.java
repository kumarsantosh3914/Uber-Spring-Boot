package com.example.uber.client;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.example.uber.RideNotificationRequest;
import com.example.uber.RideNotificationResponse;
import com.example.uber.RideNotificationServiceGrpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
public class GrpcClient {

    @Value("${grpc.server.port:9090}")
    private int grpcServerPort;

    @Value("${grpc.server.host:localhost}")
    private String grpcServerHost;

    private ManagedChannel channel;
    private RideNotificationServiceGrpc.RideNotificationServiceBlockingStub rideNotificationServiceStub;

    @PostConstruct
    public void init() {
        channel = ManagedChannelBuilder.forAddress(grpcServerHost, grpcServerPort)
                .usePlaintext()
                .build();

        rideNotificationServiceStub = RideNotificationServiceGrpc.newBlockingStub(channel);
    }

    @PreDestroy
    public void shutdown() {
        if (channel != null && !channel.isShutdown()) {
            try {
                channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                channel.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public boolean notifyDriversForNewRide(String pickUpLocationLatitude, String pickUpLocationLongitude,
            Integer bookingId, List<Integer> driverIds) {
        RideNotificationRequest request = RideNotificationRequest.newBuilder()
                .setPickUpLocationLatitude(pickUpLocationLatitude)
                .setPickUpLocationLongitude(pickUpLocationLongitude)
                .setBookingId(bookingId)
                .addAllDriverIds(driverIds)
                .build();

        RideNotificationResponse response = rideNotificationServiceStub.notifyDriversForNewRide(request);

        return response.getSuccess();
    }
}