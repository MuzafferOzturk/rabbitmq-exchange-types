package tr.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveFanout {
    public static void main(String[] args) {
        try {
            String EXCHANGE_NAME = "exchangeFanout";
            Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, "");
            System.out.println("Waiting Message");
            String queueName2 = channel.queueDeclare().getQueue();
            channel.queueBind(queueName2, EXCHANGE_NAME, "");
            DeliverCallback deliverCallback = (s, delivery) -> {
                System.out.println(" --> " + s);
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println("Received '" + message + "'");
            };
            channel.basicConsume(queueName, true, deliverCallback, consumerTag -> System.out.println("Cancel " + consumerTag));
            channel.basicConsume(queueName2, true, deliverCallback, consumerTag -> System.out.println("Cancel " + consumerTag));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
