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

class AStrategy implements IStrategy {

    public void doPublish(int publisherId){

        EventMessage EM = new EventMessage("A Strategy Header","A Strategy Message");
        AbstractEvent event = EventFactory.createEvent(EventType.TypeA, publisherId, EM);
        

        List<String> channelsByStratA = new ArrayList();
        channelsByStratA.add("cars");
        channelsByStratA.add("snacks");
        
        ChannelEventDispatcher.getInstance().postEvent(event, channelsByStratA, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event" + event.getEventID());

    }


    public void doPublish(AbstractEvent event, int publisherId){

        List<String> channelsByStratA = new ArrayList();
        channelsByStratA.add("cars");
        channelsByStratA.add("snacks");

        ChannelEventDispatcher.getInstance().postEvent(event, channelsByStratA, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event " + event.getEventID());
    }

}
