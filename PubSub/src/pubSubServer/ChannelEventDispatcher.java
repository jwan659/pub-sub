package pubSubServer;

import java.util.List;

import events.AbstractEvent;
import publishers.AbstractPublisher;


/**
 * @author kkontog, ktsiouni, mgrigori
 * MUST IMPLEMENT the Singleton design pattern
 * Class providing an interface for {@link AbstractPublisher} objects to cover their publishing needs 
 */
public class ChannelEventDispatcher {

	private static ChannelEventDispatcher instance = null;
	private ChannelPoolManager cpManager = ChannelPoolManager.getInstance();
	
	public static ChannelEventDispatcher getInstance() {

		if (instance == null)
			instance = new ChannelEventDispatcher();
		return instance;
	}

	
	
	/**
	 * @param event event to be published
	 * @param listOfChannels list of channel names to which the event must be published to 
	 */
	public void postEvent(AbstractEvent event, List<String> listOfChannels, int pID) {
		
		for(String channelName : listOfChannels) {
			AbstractChannel channel = cpManager.findChannel(channelName);
			if(channel == null) {
				channel = ChannelCreator.getInstance().addChannel(channelName);
			}
			
			channel.publishEvent(event);
			System.out.println("Channel " + channel.getChannelTopic() + "has event " + event.getEventID() + "from publisher " + pID);
		}
	}
	
	
}
