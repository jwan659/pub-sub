package publishers;

import events.AbstractEvent;
import strategies.publisher.IStrategy;
import strategies.publisher.StrategyFactory;	
import strategies.publisher.StrategyName;


/**
 * @author kkontog, ktsiouni, mgrigori
 * 
 * the WeatherPublisher class is an example of a ConcretePublisher 
 * implementing the IPublisher interface. Of course the publish 
 * methods could have far more interesting logics
 */
public class ConcretePublisher extends AbstractPublisher {

	
	
	
	
	/**
	 * @param concreteStrategy attaches a concreteStrategy generated from the {@link StrategyFactory#createStrategy(strategies.publisher.StrategyName)}
	 * method
	 */
	protected ConcretePublisher(IStrategy concreteStrategy, StrategyName strategyName) {
		this.publishingStrategy = concreteStrategy;
		System.out.println("Publisher " + this.hashCode() + " created");
		System.out.println("Publisher " + this.hashCode() + " has strategy " + strategyName);
	}

	/* (non-Javadoc)
	 * @see publishers.IPublisher#publish(events.AbstractEvent)
	 */
	@Override
	public void publish(AbstractEvent event) {
		publishingStrategy.doPublish(event, this.hashCode());
		// long eventID = event.getEventID();
	}

	/* (non-Javadoc)
	 * @see publishers.IPublisher#publish()
	 */
	@Override
	public void publish() {
		publishingStrategy.doPublish(this.hashCode());
	}

	public void setStrategy(StrategyName strategyName) {
		publishingStrategy = StrategyFactory.createStrategy(strategyName);
		System.out.println("Subscriber " + this.hashCode() + " is on state " + strategyName);
		this.strategyName= strategyName;
	}
}
