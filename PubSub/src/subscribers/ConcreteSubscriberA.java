package subscribers;

import events.AbstractEvent;
import pubSubServer.SubscriptionManager;
import states.subscriber.IState;
import states.subscriber.StateFactory;
import states.subscriber.StateName;


/**
 * @author kkontog, ktsiouni, mgrigori
 * an example concrete subscriber
 */
class ConcreteSubscriberA extends AbstractSubscriber {
	int subscriberID;
	
	public int getSubscriberID(){
		return subscriberID;
	}

	protected ConcreteSubscriberA(int sID) {
		this.subscriberID = sID;
		state = StateFactory.createState(StateName.defaultState);
	System.out.println("Subscriber " + sID + " created");
	}
	
	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#setState(states.subscriber.StateName)
	 */
	public void setState(StateName stateName) {
		state = StateFactory.createState(stateName);
		System.out.println("Subscriber " + subscriberID + " is on state " + stateName);
		this.stateName = stateName;
	}
	
	
	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#alert(events.AbstractEvent, java.lang.String)
	 */
	@Override
	public void alert(AbstractEvent event, String channelName, StateName state) {
		this.state.handleEvent(event, channelName);
		System.out.println("Subscriber" + subscriberID + "receives event " + event.getEventID() + " and handles it at state" + state);
	}

	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#subscribe(java.lang.String)
	 */
	@Override
	public void subscribe(String channelName) {
		SubscriptionManager.getInstance().subscribe(channelName, this);		
	}

	/* (non-Javadoc)
	 * @see subscribers.ISubscriber#unsubscribe(java.lang.String)
	 */
	@Override
	public void unsubscribe(String channelName) {
		SubscriptionManager.getInstance().subscribe(channelName, this);
		
	}
	


}
