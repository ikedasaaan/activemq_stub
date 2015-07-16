package com;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueReceiver;
import javax.jms.QueueSession;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.util.Constants;

public class MainApp {

    public static void main(String[] args){

        try {
            //Connectionを作成するFactoryを作成
            QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.MSG_BROKER_URL);
            QueueConnection connection = connectionFactory.createQueueConnection();

            //セッションの作成
            QueueSession session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);


            Queue testQueue = session.createQueue("destination");


            //Queueと関連付け
            QueueReceiver receiver = session.createReceiver(testQueue);

            connection.start();

            //メッセージの受信
            TextMessage msg = (TextMessage) receiver.receive();
            System.out.println(msg.getText());

            receiver.close();
            session.close();
            connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }
}