package com;

import java.util.Enumeration;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;

import com.util.Constants;

public class MainApp implements MessageListener {

	private Session session;


	public static void main(String[] args) {
		new MainApp();
	}


	/**
	 * Constructor
	 */
	public MainApp() {
		this.setupMessageQueueConsumer();
	}

	private void setupMessageQueueConsumer() {

		//Connectionを作成するFactoryを作成
		QueueConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Constants.MSG_BROKER_URL);

        try {
            //Connectionを作成するFactoryを作成
            QueueConnection connection = connectionFactory.createQueueConnection();
            //セッションの作成
            this.session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);



            Queue testQueue = session.createQueue("destination");


            //Queueと関連付け
			MessageConsumer consumer = this.session.createConsumer(testQueue);
			consumer.setMessageListener(this);

			connection.start();
            //receiver.close();
            //session.close();
            //connection.close();
        } catch (JMSException e) {
            e.printStackTrace();
        }

    }


	@Override
	public void onMessage(Message message) {
		// TODO 自動生成されたメソッド・スタブ
		// terminate if JMSCorrelationID is not set
		System.out.println("---------------onMessage---------------");
		try {
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage)message;
				System.out.println("[TextMessage]" + msg.getText());
			} else if (message instanceof MapMessage) {
				processMapMessage(message);

			}
		} catch (JMSException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (NumberFormatException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}

	private void processMapMessage(Message message)
			throws NumberFormatException, Exception {
		MapMessage msg = (MapMessage)message;


        MapMessage replyMessage = session.createMapMessage();

		if (message.getJMSReplyTo() != null) {
			System.out.println("getJMSReplyTo :" + message.getJMSReplyTo());
			replyMessage.setJMSReplyTo(message.getJMSReplyTo());
		}

		if (message.getJMSCorrelationID() != null) {
			System.out.println("JMSCorrelationID :" + message.getJMSCorrelationID());
			replyMessage.setJMSCorrelationID(message.getJMSCorrelationID());
		}
		if (message.getJMSMessageID() != null) {
			System.out.println("JMSMessageID :" + message.getJMSMessageID());
			replyMessage.setJMSMessageID(message.getJMSMessageID());
		}

		replyMessage.setString("data", "AAAAA");
		replyMessage.setString("error_cd", "AAAAA");
		replyMessage.setString("error_msg", "AAAAA");
		replyMessage.setJMSType("MapMessage");


        // log出すだけ
        Enumeration<?> enumeratio = msg.getMapNames();
        while( enumeratio.hasMoreElements() )
        {
            // 分割した各要素を取得します。
            String current = (String)enumeratio.nextElement();
            // 出力します。
            System.out.println("[MapMessage]" + current );
        }
        System.out.println("[MapMessage getObject]" + msg.getObject("setObject"));
        Destination replyDestination = (Destination)message.getJMSReplyTo();
        this.session.createProducer(replyDestination).send(replyMessage);

	}
}