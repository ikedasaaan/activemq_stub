package com;

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
            connection.start();
            //セッションの作成
            this.session = connection.createQueueSession(false,QueueSession.AUTO_ACKNOWLEDGE);


            Queue testQueue = session.createQueue("destination");


            //Queueと関連付け
			MessageConsumer consumer = this.session.createConsumer(testQueue);
			consumer.setMessageListener(this);


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
		try {
			if (message.getJMSCorrelationID() == null) {
				System.out.println("JMSCorrelationID :" + message.getJMSCorrelationID());
			}
			if (message instanceof TextMessage) {
				TextMessage msg = (TextMessage)message;
				System.out.println("[TextMessage]" + msg.getText());
			} else if (message instanceof MapMessage) {
				MapMessage msg = (MapMessage)message;
				System.out.println("[MapMessage]" + msg.getMapNames());
			}
		} catch (JMSException e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

	}
}