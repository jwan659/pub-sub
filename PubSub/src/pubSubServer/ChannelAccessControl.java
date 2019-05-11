package pubSubServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import subscribers.AbstractSubscriber;


/**
 * @author kkontog, ktsiouni, mgrigori
 * MUST Implements the Singleton design pattern
 * Class that acts as an access control module that allows for the 
 * blocking and unblocking of specific subscribers for specific channels
 * 
 */
public class ChannelAccessControl {

	private static ChannelAccessControl instance = null;
	
	public static ChannelAccessControl getInstance() {

		if (instance == null)
			instance = new ChannelAccessControl();
		return instance;
	}
	Map<String, List<AbstractSubscriber>> blackList = new HashMap<>();
	
	
	
	/**
	 * 
	 * blocks the provided subscriber from accessing the designated channel
	 * @param subscriber an instance of any implementation of {@link AbstractSubscriber}
	 * @param channelName a String value representing a valid channel name
	 */
	public void blockSubscriber(AbstractSubscriber subscriber, String channelName) {
		
		List<AbstractSubscriber> blockedSubscribers = blackList.getOrDefault(channelName, new ArrayList<AbstractSubscriber>());
		blockedSubscribers.add(subscriber);
		blackList.put(channelName, blockedSubscribers);
		System.out.println("Subscriber " + subscriber.getID() + " is blocked on Channel " + channelName);
	}

	/**
	 * unblocks the provided subscriber from accessing the designated channel
	 * @param subscriber an instance of any implementation of {@link AbstractSubscriber}
	 * @param channelName a String value representing a valid channel name
	 */
	public void unBlockSubscriber(AbstractSubscriber subscriber, String channelName) {
		
		List<AbstractSubscriber> blockedSubscribers;
		if((blockedSubscribers = blackList.get(channelName)) == null)
			return;
		blockedSubscribers.remove(subscriber);
		System.out.println("Subscriber " + subscriber.getID() + " is un-blocked on channel " + channelName);
	}

	
	/**
	 * checks if the provided subscriber is blocked from accessing the specified channel
	 * @param subscriber an instance of any implementation of {@link AbstractSubscriber}
	 * @param channelName a String value representing a valid channel name
	 * @return true if blocked false otherwise
	 */
	protected boolean checkIfBlocked(AbstractSubscriber subscriber, String channelName) {
		
		List<AbstractSubscriber> blockedSubscribers;
		if((blockedSubscribers = blackList.get(channelName)) == null)
			return false;
		return (blockedSubscribers.contains(subscriber));
	}


	
	
}
