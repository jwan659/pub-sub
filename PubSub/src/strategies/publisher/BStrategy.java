package strategies.publisher;

import events.AbstractEvent;
import publishers.AbstractPublisher;
import events.EventFactory;
import events.EventMessage;
import pubSubServer.ChannelEventDispatcher;
import pubSubServer.ChannelDiscovery;
import events.EventType;
import java.util.List;
import java.util.ArrayList;

class BStrategy implements IStrategy {

    public void doPublish(int publisherId){

        EventMessage EM = new EventMessage("B Strategy Header","B Strategy Message");
        AbstractEvent event = EventFactory.createEvent(EventType.TypeB, publisherId, EM);
        

        List<String> channelsByStratB = new ArrayList();
        channelsByStratB.add("food");
        channelsByStratB.add("snacks");
        
        ChannelEventDispatcher.getInstance().postEvent(event, channelsByStratB, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event" + event.getEventID());

    }


    public void doPublish(AbstractEvent event, int publisherId){

        List<String> channelsByStratB = new ArrayList();
        channelsByStratB.add("food");
        channelsByStratB.add("snacks");

        ChannelEventDispatcher.getInstance().postEvent(event, channelsByStratB, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event " + event.getEventID());
    }

}