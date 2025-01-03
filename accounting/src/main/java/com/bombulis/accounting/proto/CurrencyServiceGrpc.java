package com.bombulis.accounting.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: currency.proto")
public final class CurrencyServiceGrpc {

  private CurrencyServiceGrpc() {}

  public static final String SERVICE_NAME = "com.bombulis.accounting.proto.CurrencyService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bombulis.accounting.proto.Currency.CurrencyRequest,
      com.bombulis.accounting.proto.Currency.CurrencyResponse> getGetCurrencyInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetCurrencyInfo",
      requestType = com.bombulis.accounting.proto.Currency.CurrencyRequest.class,
      responseType = com.bombulis.accounting.proto.Currency.CurrencyResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bombulis.accounting.proto.Currency.CurrencyRequest,
      com.bombulis.accounting.proto.Currency.CurrencyResponse> getGetCurrencyInfoMethod() {
    io.grpc.MethodDescriptor<com.bombulis.accounting.proto.Currency.CurrencyRequest, com.bombulis.accounting.proto.Currency.CurrencyResponse> getGetCurrencyInfoMethod;
    if ((getGetCurrencyInfoMethod = CurrencyServiceGrpc.getGetCurrencyInfoMethod) == null) {
      synchronized (CurrencyServiceGrpc.class) {
        if ((getGetCurrencyInfoMethod = CurrencyServiceGrpc.getGetCurrencyInfoMethod) == null) {
          CurrencyServiceGrpc.getGetCurrencyInfoMethod = getGetCurrencyInfoMethod = 
              io.grpc.MethodDescriptor.<com.bombulis.accounting.proto.Currency.CurrencyRequest, com.bombulis.accounting.proto.Currency.CurrencyResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.bombulis.accounting.proto.CurrencyService", "GetCurrencyInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.accounting.proto.Currency.CurrencyRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.accounting.proto.Currency.CurrencyResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new CurrencyServiceMethodDescriptorSupplier("GetCurrencyInfo"))
                  .build();
          }
        }
     }
     return getGetCurrencyInfoMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static CurrencyServiceStub newStub(io.grpc.Channel channel) {
    return new CurrencyServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static CurrencyServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new CurrencyServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static CurrencyServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new CurrencyServiceFutureStub(channel);
  }

  /**
   */
  public static abstract class CurrencyServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getCurrencyInfo(com.bombulis.accounting.proto.Currency.CurrencyRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.accounting.proto.Currency.CurrencyResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetCurrencyInfoMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetCurrencyInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bombulis.accounting.proto.Currency.CurrencyRequest,
                com.bombulis.accounting.proto.Currency.CurrencyResponse>(
                  this, METHODID_GET_CURRENCY_INFO)))
          .build();
    }
  }

  /**
   */
  public static final class CurrencyServiceStub extends io.grpc.stub.AbstractStub<CurrencyServiceStub> {
    private CurrencyServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyServiceStub(channel, callOptions);
    }

    /**
     */
    public void getCurrencyInfo(com.bombulis.accounting.proto.Currency.CurrencyRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.accounting.proto.Currency.CurrencyResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetCurrencyInfoMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   */
  public static final class CurrencyServiceBlockingStub extends io.grpc.stub.AbstractStub<CurrencyServiceBlockingStub> {
    private CurrencyServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bombulis.accounting.proto.Currency.CurrencyResponse getCurrencyInfo(com.bombulis.accounting.proto.Currency.CurrencyRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetCurrencyInfoMethod(), getCallOptions(), request);
    }
  }

  /**
   */
  public static final class CurrencyServiceFutureStub extends io.grpc.stub.AbstractStub<CurrencyServiceFutureStub> {
    private CurrencyServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private CurrencyServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected CurrencyServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new CurrencyServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bombulis.accounting.proto.Currency.CurrencyResponse> getCurrencyInfo(
        com.bombulis.accounting.proto.Currency.CurrencyRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetCurrencyInfoMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_CURRENCY_INFO = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final CurrencyServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(CurrencyServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_CURRENCY_INFO:
          serviceImpl.getCurrencyInfo((com.bombulis.accounting.proto.Currency.CurrencyRequest) request,
              (io.grpc.stub.StreamObserver<com.bombulis.accounting.proto.Currency.CurrencyResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  private static abstract class CurrencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    CurrencyServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bombulis.accounting.proto.Currency.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("CurrencyService");
    }
  }

  private static final class CurrencyServiceFileDescriptorSupplier
      extends CurrencyServiceBaseDescriptorSupplier {
    CurrencyServiceFileDescriptorSupplier() {}
  }

  private static final class CurrencyServiceMethodDescriptorSupplier
      extends CurrencyServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    CurrencyServiceMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (CurrencyServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new CurrencyServiceFileDescriptorSupplier())
              .addMethod(getGetCurrencyInfoMethod())
              .build();
        }
      }
    }
    return result;
  }
}
