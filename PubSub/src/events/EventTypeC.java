package events;


/**
 * @author kkontog, ktsiouni, mgrigori
 * sample event type, simply creates a new event using the constructor 
 * {@link AbstractEvent#AbstractEvent(long, int, EventMessage)}   
 */
class EventTypeC extends AbstractEvent {

	
	/**
	 * shouldn't be used outside the EventFactory
	 * for information on how to call it and what the parameters are please look 
	 * at the documentation {@link EventFactory#createEvent(EventType, int, EventMessage)}
	 * 
	 * @param eventID
	 * @param eventPublisherId
	 * @param payload
	 */
	protected EventTypeC(long eventID, int eventPublisherId, EventMessage payload) {
		super(eventID, eventPublisherId, payload);
	}


}
