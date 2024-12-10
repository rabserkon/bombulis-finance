package com.bombulis.stock.control.proto;

import static io.grpc.MethodDescriptor.generateFullMethodName;
import static io.grpc.stub.ClientCalls.asyncUnaryCall;
import static io.grpc.stub.ClientCalls.blockingUnaryCall;
import static io.grpc.stub.ClientCalls.futureUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnaryCall;
import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

/**
 * <pre>
 * Сервис для получения информации о счете
 * </pre>
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.15.0)",
    comments = "Source: account.proto")
public final class AccountServiceGrpc {

  private AccountServiceGrpc() {}

  public static final String SERVICE_NAME = "com.bombulis.accounting.proto.AccountService";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest,
      com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> getGetAccountInfoMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccountInfo",
      requestType = com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest.class,
      responseType = com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest,
      com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> getGetAccountInfoMethod() {
    io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest, com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> getGetAccountInfoMethod;
    if ((getGetAccountInfoMethod = AccountServiceGrpc.getGetAccountInfoMethod) == null) {
      synchronized (AccountServiceGrpc.class) {
        if ((getGetAccountInfoMethod = AccountServiceGrpc.getGetAccountInfoMethod) == null) {
          AccountServiceGrpc.getGetAccountInfoMethod = getGetAccountInfoMethod = 
              io.grpc.MethodDescriptor.<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest, com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.bombulis.accounting.proto.AccountService", "GetAccountInfo"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AccountServiceMethodDescriptorSupplier("GetAccountInfo"))
                  .build();
          }
        }
     }
     return getGetAccountInfoMethod;
  }

  private static volatile io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest,
      com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> getGetAccountsMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "GetAccounts",
      requestType = com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest.class,
      responseType = com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest,
      com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> getGetAccountsMethod() {
    io.grpc.MethodDescriptor<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest, com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> getGetAccountsMethod;
    if ((getGetAccountsMethod = AccountServiceGrpc.getGetAccountsMethod) == null) {
      synchronized (AccountServiceGrpc.class) {
        if ((getGetAccountsMethod = AccountServiceGrpc.getGetAccountsMethod) == null) {
          AccountServiceGrpc.getGetAccountsMethod = getGetAccountsMethod = 
              io.grpc.MethodDescriptor.<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest, com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(
                  "com.bombulis.accounting.proto.AccountService", "GetAccounts"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse.getDefaultInstance()))
                  .setSchemaDescriptor(new AccountServiceMethodDescriptorSupplier("GetAccounts"))
                  .build();
          }
        }
     }
     return getGetAccountsMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static AccountServiceStub newStub(io.grpc.Channel channel) {
    return new AccountServiceStub(channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static AccountServiceBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    return new AccountServiceBlockingStub(channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static AccountServiceFutureStub newFutureStub(
      io.grpc.Channel channel) {
    return new AccountServiceFutureStub(channel);
  }

  /**
   * <pre>
   * Сервис для получения информации о счете
   * </pre>
   */
  public static abstract class AccountServiceImplBase implements io.grpc.BindableService {

    /**
     */
    public void getAccountInfo(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountInfoMethod(), responseObserver);
    }

    /**
     */
    public void getAccounts(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> responseObserver) {
      asyncUnimplementedUnaryCall(getGetAccountsMethod(), responseObserver);
    }

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
          .addMethod(
            getGetAccountInfoMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest,
                com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse>(
                  this, METHODID_GET_ACCOUNT_INFO)))
          .addMethod(
            getGetAccountsMethod(),
            asyncUnaryCall(
              new MethodHandlers<
                com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest,
                com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse>(
                  this, METHODID_GET_ACCOUNTS)))
          .build();
    }
  }

  /**
   * <pre>
   * Сервис для получения информации о счете
   * </pre>
   */
  public static final class AccountServiceStub extends io.grpc.stub.AbstractStub<AccountServiceStub> {
    private AccountServiceStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AccountServiceStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountServiceStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AccountServiceStub(channel, callOptions);
    }

    /**
     */
    public void getAccountInfo(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountInfoMethod(), getCallOptions()), request, responseObserver);
    }

    /**
     */
    public void getAccounts(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest request,
        io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> responseObserver) {
      asyncUnaryCall(
          getChannel().newCall(getGetAccountsMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * <pre>
   * Сервис для получения информации о счете
   * </pre>
   */
  public static final class AccountServiceBlockingStub extends io.grpc.stub.AbstractStub<AccountServiceBlockingStub> {
    private AccountServiceBlockingStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AccountServiceBlockingStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountServiceBlockingStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AccountServiceBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse getAccountInfo(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountInfoMethod(), getCallOptions(), request);
    }

    /**
     */
    public com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse getAccounts(com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest request) {
      return blockingUnaryCall(
          getChannel(), getGetAccountsMethod(), getCallOptions(), request);
    }
  }

  /**
   * <pre>
   * Сервис для получения информации о счете
   * </pre>
   */
  public static final class AccountServiceFutureStub extends io.grpc.stub.AbstractStub<AccountServiceFutureStub> {
    private AccountServiceFutureStub(io.grpc.Channel channel) {
      super(channel);
    }

    private AccountServiceFutureStub(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected AccountServiceFutureStub build(io.grpc.Channel channel,
        io.grpc.CallOptions callOptions) {
      return new AccountServiceFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse> getAccountInfo(
        com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountInfoMethod(), getCallOptions()), request);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse> getAccounts(
        com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest request) {
      return futureUnaryCall(
          getChannel().newCall(getGetAccountsMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_GET_ACCOUNT_INFO = 0;
  private static final int METHODID_GET_ACCOUNTS = 1;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AccountServiceImplBase serviceImpl;
    private final int methodId;

    MethodHandlers(AccountServiceImplBase serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_GET_ACCOUNT_INFO:
          serviceImpl.getAccountInfo((com.bombulis.stock.control.proto.AccountOuterClass.GetAccountRequest) request,
              (io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountResponse>) responseObserver);
          break;
        case METHODID_GET_ACCOUNTS:
          serviceImpl.getAccounts((com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsRequest) request,
              (io.grpc.stub.StreamObserver<com.bombulis.stock.control.proto.AccountOuterClass.GetAccountsResponse>) responseObserver);
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

  private static abstract class AccountServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    AccountServiceBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.bombulis.stock.control.proto.AccountOuterClass.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("AccountService");
    }
  }

  private static final class AccountServiceFileDescriptorSupplier
      extends AccountServiceBaseDescriptorSupplier {
    AccountServiceFileDescriptorSupplier() {}
  }

  private static final class AccountServiceMethodDescriptorSupplier
      extends AccountServiceBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    AccountServiceMethodDescriptorSupplier(String methodName) {
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
      synchronized (AccountServiceGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new AccountServiceFileDescriptorSupplier())
              .addMethod(getGetAccountInfoMethod())
              .addMethod(getGetAccountsMethod())
              .build();
        }
      }
    }
    return result;
  }
}
