package tr.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtil {
    public static Connection createConnection(){
        try{
            ConnectionFactory factory = new ConnectionFactory();
            factory.setUri("amqp://guest:guest@localhost:5672");
            final Connection connection = factory.newConnection();
            System.out.println(connection.getClientProperties());
            return connection;
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        return null;
    }

    public static Channel createChannel(Connection connection){
        try {
            Channel channel = connection.createChannel();
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    channel.close();
                    connection.close();
                }
                catch (IOException | TimeoutException e) {
                    e.printStackTrace();
                }
            }));
            return channel;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
