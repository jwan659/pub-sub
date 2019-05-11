package orchestration;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import events.EventMessage;
import pubSubServer.AbstractChannel;
import pubSubServer.ChannelAccessControl;
import pubSubServer.ChannelDiscovery;
import pubSubServer.ChannelEventDispatcher;
import pubSubServer.SubscriptionManager;
import publishers.AbstractPublisher;
import publishers.PublisherFactory;
import publishers.PublisherType;
import states.subscriber.StateName;
import strategies.publisher.StrategyName;
import subscribers.AbstractSubscriber;
import subscribers.SubscriberFactory;
import subscribers.SubscriberType;
import events.EventFactory;
import events.EventType;

import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;


public class Orchestration {

	public static void main(String[] args) {

		Map<Integer, AbstractPublisher> mapOfPublishers = new HashMap<Integer, AbstractPublisher>();
		Map<Integer, AbstractSubscriber> mapOfSubscribers = new HashMap<Integer, AbstractSubscriber>();
		Orchestration testHarness = new Orchestration();
		
		try {
			mapOfPublishers = testHarness.createPublishers();
			mapOfSubscribers = testHarness.createSubscribers();
		
			
			List<AbstractChannel> channels = ChannelDiscovery.getInstance().listChannels();
			//For demonstration purposes only
			try {
			BufferedReader initialChannels = new BufferedReader(new FileReader(new File("Channels.chl")));
			List<String> channelList = new ArrayList<String>();
			String line = "";
			while((line = initialChannels.readLine()) != null )
				channelList.add(line);	
			int subscriberIndex = 0;
			for(AbstractSubscriber subscriber : mapOfSubscribers.values()) {
				subscriber.subscribe(channelList.get(subscriberIndex%channelList.size()));
				subscriberIndex++;
			}
			initialChannels.close();
			}catch(IOException ioe) {
				System.out.println("Loading Channels from file failed proceeding with random selection");
				Iterator<Integer> subscribers = mapOfSubscribers.keySet().iterator();
				
				
				while (subscribers.hasNext()) {
					int index = (int) Math.round((Math.random()*10))/3;
					SubscriptionManager.getInstance().subscribe(channels.get(index).getChannelTopic(), mapOfSubscribers.get(subscribers.next()));
				}
			}

			for(Integer publisherKey : mapOfPublishers.keySet()) {
				mapOfPublishers.get(publisherKey).publish();
			}

			testHarness.runScenario(mapOfPublishers, mapOfSubscribers);
			
		} catch (IOException ioe) {
			System.out.println(ioe.getMessage());
			System.out.println("Will now terminate");
			return;
		}
		for(Integer publisherKey : mapOfPublishers.keySet()) {
			mapOfPublishers.get(publisherKey).publish();
		}
		
		
	}

	
	private Map<Integer, AbstractPublisher> createPublishers() throws IOException{
		Map<Integer, AbstractPublisher> publishersById = new HashMap<Integer, AbstractPublisher>();
		AbstractPublisher newPub;
		BufferedReader StrategyBufferedReader = new BufferedReader(new FileReader(new File("Strategies.str")));
		
		while(StrategyBufferedReader.ready()) {
			String PublisherConfigLine = StrategyBufferedReader.readLine();
			String[] PublisherConfigArray = PublisherConfigLine.split("\t");
			int[] PublisherConfigIntArray = new int[2];
			for(int i = 0; i < PublisherConfigArray.length; i++){
				PublisherConfigIntArray[i] = Integer.parseInt(PublisherConfigArray[i]);
			}	


			if (!(publishersById.containsKey(PublisherConfigIntArray[0]))){
				newPub = PublisherFactory.createPublisher(PublisherType.values()[PublisherConfigIntArray[0]],
					StrategyName.values()[PublisherConfigIntArray[1]]);
				publishersById.put(PublisherConfigIntArray[0],newPub);
			}

		}
		StrategyBufferedReader.close();
		return publishersById;
	}
	
	private Map<Integer, AbstractSubscriber>  createSubscribers() throws IOException{
		

		AbstractSubscriber newSub;
		Map<Integer, AbstractSubscriber> subscriberById = new HashMap<Integer, AbstractSubscriber>();
		BufferedReader StateBufferedReader = new BufferedReader(new FileReader(new File("States.sts")));
		
		while(StateBufferedReader.ready()) {
			String StateConfigLine = StateBufferedReader.readLine();
			String[] StateConfigArray = StateConfigLine.split("\t");
			int[] StateConfigIntArray = new int[2];
			for(int i = 0; i < StateConfigArray.length; i++){
				StateConfigIntArray[i] = Integer.parseInt(StateConfigArray[i]);
			}

			if (!(subscriberById.containsKey(StateConfigIntArray[0]))){
				newSub = SubscriberFactory.createSubscriber(StateConfigIntArray[0], 
					SubscriberType.values()[StateConfigIntArray[0]], StateName.values()[StateConfigIntArray[1]]);
				subscriberById.put(StateConfigIntArray[0], newSub);
			}
			else{
				subscriberById.get(StateConfigIntArray[0]).setState(StateName.values()[StateConfigIntArray[1]]);
			}
		}

		return subscriberById;
	}
	
	
	private void runScenario(Map<Integer, AbstractPublisher> mapOfPublishers, Map<Integer, AbstractSubscriber> mapOfSubscribers){
		try{
			BufferedReader driver= new BufferedReader(new FileReader(new File("Driver.txt")));
			while(driver.ready()){
				
				String actionLine = driver.readLine();
				String[] actionArray = actionLine.split("\t");
				AbstractSubscriber subscriber = mapOfSubscribers.get(Integer.parseInt(actionArray[1]));
				
				switch(actionArray[0]){
					
					case "PUB":	
						AbstractPublisher publisher = mapOfPublishers.get(Integer.parseInt(actionArray[1]));
						if (actionArray.length == 5){
							String typeString = actionArray[2];
							EventType eventTypeInput;
							switch (typeString){
								case ("TypeA"):
									eventTypeInput = EventType.TypeA;
									break;
								case("TypeB"):
									eventTypeInput = EventType.TypeB;
									break;
								case("TypeC"):
									eventTypeInput = EventType.TypeC;
								default:
									eventTypeInput = EventType.TypeC;
							}
							publisher.publish(EventFactory.createEvent(eventTypeInput, Integer.parseInt(actionArray[1]), new EventMessage(actionArray[3], actionArray[4])));
						}
						else {
							publisher.publish();
						}
						break;
					case "SUB":
						
						SubscriptionManager.getInstance().subscribe(actionArray[1], subscriber);
						break;
	
					case "BLOCK":
						ChannelAccessControl.getInstance().blockSubscriber(subscriber, actionArray[1]);	
						break;
					case "UNBLOCK":
	
						ChannelAccessControl.getInstance().unBlockSubscriber(subscriber, actionArray[1]);	
						break;
					default:
						//System.out.println("LOL");
				}
			}
		}catch(FileNotFoundException e){
			System.out.println(e.getMessage());
		}catch(IOException e){
			System.out.println(e.getMessage());
		}
		
	}	
}
