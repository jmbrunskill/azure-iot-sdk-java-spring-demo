package com.edgemodule;
import com.microsoft.azure.sdk.iot.device.*;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;


public class IotEdge {
    private static MessageCallbackMqtt msgCallback = new MessageCallbackMqtt();
    private static final String INPUT_NAME = "input1";
    private static final String OUTPUT_NAME = "output1";

    protected static class EventCallback implements IotHubEventCallback {
        @Override
        public void execute(IotHubStatusCode status, Object context) {
            if (context instanceof Message) {
                System.err.println("Send message with status: " + status.name());
            } else {
                System.err.println("Invalid context passed");
            }
        }
    }
    

    protected static class MessageCallbackMqtt implements MessageCallback {
        private int counter = 0;

        @Override
        public IotHubMessageResult execute(Message msg, Object context) {
            this.counter += 1;
            String msgString = new String(msg.getBytes(), Message.DEFAULT_IOTHUB_MESSAGE_CHARSET);
            System.err.println(String.format("Received message %d: %s", this.counter, msgString));
            return IotHubMessageResult.COMPLETE;
        }
      
    }

    protected static class ConnectionStatusChangeCallback implements IotHubConnectionStatusChangeCallback {

        @Override
        public void execute(IotHubConnectionStatus status,
                            IotHubConnectionStatusChangeReason statusChangeReason,
                            Throwable throwable, Object callbackContext) {
            String statusStr = "Connection Status: %s";
            switch (status) {
                case CONNECTED:
                    System.err.println(String.format(statusStr, "Connected"));
                    break;
                case DISCONNECTED:
                    System.err.println(String.format(statusStr, "Disconnected"));
                    if (throwable != null) {
                        throwable.printStackTrace();
                    }
                    System.exit(1);
                    break;
                case DISCONNECTED_RETRYING:
                    System.err.println(String.format(statusStr, "Retrying"));
                    break;
                default:
                    break;
            }
        }
    }

    public static void start(){
        try {
            IotHubClientProtocol protocol = IotHubClientProtocol.AMQPS;
            System.err.println("Start to create client with AMQPS protocol");
            ModuleClient client = ModuleClient.createFromEnvironment(protocol);
            System.err.println("Client created");
            client.setMessageCallback(IotEdge.INPUT_NAME, msgCallback, client);
            client.registerConnectionStatusChangeCallback(new ConnectionStatusChangeCallback(), null);
            client.open();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }


}
