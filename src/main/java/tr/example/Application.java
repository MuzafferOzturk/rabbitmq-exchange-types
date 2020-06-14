package tr.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class Application {
    public static String DIRECT_ROUTING = "firstDirect";
    public static String TOPIC_ROUTING = "firstTopic";
    public static void main(String[] args) {
        try{
            new Application().publishFanoutExchangeMessage();
            new Application().publishDirectExchangeMessage("Test Direct Message");
            new Application().publishTopicExchangeMessage();
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void publishFanoutExchangeMessage() throws IOException {
        String EXCHANGE_NAME = "exchangeFanout";
        Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        channel.basicPublish(EXCHANGE_NAME, "",null ,"Test Message Fanout".getBytes(StandardCharsets.UTF_8));
    }

    public void publishDirectExchangeMessage(String message) throws IOException {
        String EXCHANGE_NAME = "exchangeDirect";
        Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        channel.basicPublish(EXCHANGE_NAME, DIRECT_ROUTING,null , message.getBytes(StandardCharsets.UTF_8));
    }

    public void publishTopicExchangeMessage() throws IOException {
        String EXCHANGE_NAME = "exchangeTopic";
        Channel channel = ConnectionUtil.createChannel(ConnectionUtil.createConnection());
        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        channel.basicPublish(EXCHANGE_NAME, TOPIC_ROUTING,null ,"Test Message Topic".getBytes(StandardCharsets.UTF_8));
    }
}
