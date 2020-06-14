package tr.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ReceiveTopic {
    public static void main(String[] args) {
        try {
            String QUEUE_NAME = "topicQueue";
            String EXCHANGE_NAME = "exchangeTopic";
            Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            channel.queueDeclareNoWait(QUEUE_NAME, true, false, false, null);
            channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, Application.TOPIC_ROUTING);

            System.out.println("Waiting Message");
            DeliverCallback deliverCallback = (s, delivery) -> {
                System.out.println(" --> " + s);
                String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
                System.out.println(delivery.getEnvelope().getRoutingKey() + " Received '" + message + "'");
            };
            channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> System.out.println("Cancel " + consumerTag));
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
