package tr.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveDirect {
    public static void main(String[] args) {
        try {
            String EXCHANGE_NAME = "exchangeDirect";
            Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, Application.DIRECT_ROUTING);
            String queueName2 = channel.queueDeclare().getQueue();
            channel.queueBind(queueName2, EXCHANGE_NAME, Application.DIRECT_ROUTING);
            System.out.println("Waiting Message");

            channel.basicConsume(queueName, true, getCallBack("first"), consumerTag -> System.out.println("Cancel " + consumerTag));
            channel.basicConsume(queueName2, true, getCallBack("second"), consumerTag -> System.out.println("Cancel " + consumerTag));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static DeliverCallback getCallBack(String prefix){
        return (s, delivery) -> {
            System.out.println(" --> " + s);
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            System.out.println("Received " + prefix + "'" + message + "'");
        };
    }
}
