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

class DefaultStrategy implements IStrategy {

    public void doPublish(int publisherId){

        EventMessage EM = new EventMessage("Default Strategy Header","Default Strategy Message");
        AbstractEvent event = EventFactory.createEvent(EventType.TypeC, publisherId, EM);
         
        List<String> channelsByDefStrategy= new ArrayList();
        channelsByDefStrategy.add("food");
        channelsByDefStrategy.add("snacks");
        ChannelEventDispatcher.getInstance().postEvent(event, channelsByDefStrategy, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event " + event.getEventID());

    }


    public void doPublish(AbstractEvent event, int publisherId){
        
        List<String> channelsByDefStrategy= new ArrayList();
        channelsByDefStrategy.add("food");
        channelsByDefStrategy.add("snacks");
        ChannelEventDispatcher.getInstance().postEvent(event, channelsByDefStrategy, publisherId);
        System.out.println("Publisher " + publisherId + "publishes event " + event.getEventID());
    }

}