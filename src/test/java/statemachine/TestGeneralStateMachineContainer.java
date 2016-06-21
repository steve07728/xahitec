/*
Copyright (c) 2016, Stephen M Milton
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are met: 

1. Redistributions of source code must retain the above copyright notice, this
   list of conditions and the following disclaimer. 
2. Redistributions in binary form must reproduce the above copyright notice,
   this list of conditions and the following disclaimer in the documentation
   and/or other materials provided with the distribution. 

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. 
*/

package statemachine;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

import statemachine.juncture.StandardStateJuncture;
import statemachine.trigger.RepeatingTrigger;
import statemachine.trigger.Trigger;
import statemachine.trigger.TriggerHandle;

public class TestGeneralStateMachineContainer
{
    // sodaMachine is a StateMachineContainer holding two StateMachines:
    // moneyCollector and sodaDispenser
    
    protected static final boolean IN_SERVICE = true;
    protected static final boolean OUT_OF_SERVICE = false;

    // money collector
    public class CoinBox {
        public CoinBox(int sodaCost) { this.sodaCost = sodaCost; }
        private int sodaCost; // cost in cents
        private int coinTotal = 0;
        public void addCoin(Coin coin) { coinTotal += coin.getValue(); }
        public int returnChange() { int total = coinTotal; coinTotal = 0;  return total; };
        public int getCoinTotal() { return coinTotal; };
        public void bookCredit() { coinTotal -= sodaCost; }
        public boolean enoughForSoda() { return coinTotal >= sodaCost; }
    }
    
    interface CoinBoxHandler {
        
        void handleCoinBox(CoinBox coinBox);
    }
    
    public enum Coin implements Event, CoinBoxHandler {
        
        Nickle(5),
        Dime(10),
        Quarter(25),
        Dollar(100),
        ;
        
        private int value;
        private Coin(int value) { this.value = value; }
        public int getValue() { return value; }
        @SuppressWarnings("unchecked")
        public Coin getThis() { return this; }
        public String getName() { return name(); }
        public boolean isJunctureEvent() { return false; }
        public boolean isCombinedJunctureEvent() { return false; }
        public void handleCoinBox(CoinBox coinBox) { coinBox.addCoin(this); }
    }
    
    public enum Return implements Event, CoinBoxHandler {
        
        Coins,
        ;
        @SuppressWarnings("unchecked")
        public Return getThis() { return this; }
        public String getName() { return name(); }
        public boolean isJunctureEvent() { return false; }
        public boolean isCombinedJunctureEvent() { return false; }
        public void handleCoinBox(CoinBox coinBox) { coinBox.returnChange(); }
    }
    
    public enum Accepted implements Event, CoinBoxHandler {
        
        SodaCredit,
        ;
        @SuppressWarnings("unchecked")
        public Accepted getThis() { return this; }
        public String getName() { return name(); }
        public boolean isJunctureEvent() { return false; }
        public boolean isCombinedJunctureEvent() { return false; }
        public void handleCoinBox(CoinBox coinBox) { coinBox.bookCredit(); }
    }
    
    public enum Rejected implements Event, CoinBoxHandler {
        
        SodaCredit,
        ;
        @SuppressWarnings("unchecked")
        public Rejected getThis() { return this; }
        public String getName() { return name(); }
        public boolean isJunctureEvent() { return false; }
        public boolean isCombinedJunctureEvent() { return false; }
        public void handleCoinBox(CoinBox coinBox) { coinBox.returnChange(); }
    }
    
    public class OfferingCredit extends AbstractState {
	
	private static final long serialVersionUID = 1L;
	private CoinBox coinBox;
        private Vector<Event> inputEvents;
        private Vector<State> transitionStates;
        private State collectingCoins;
       
	public OfferingCredit(CoinBox coinBox, State collectingCoins) { 
	    super("OfferingCredit"); 
	    this.coinBox = coinBox;
	    this.collectingCoins = collectingCoins;
	}
	public Vector<State> getTransitionStates() { return transitionStates; }
	public Vector<Event> getInputEvents() { return inputEvents; }	public void init() {
	    Event[] events = { Accepted.SodaCredit, Rejected.SodaCredit }; 
	    inputEvents = new Vector<Event>(Arrays.asList(events));
	    transitionStates = new Vector<State>(Arrays.asList(collectingCoins));
	}
	public State onTransition(Event event) throws StateMachineException {
	    
            CoinBoxHandler handler = event.getThis();
            handler.handleCoinBox(coinBox);
	    return collectingCoins;
	}
	@SuppressWarnings("unchecked")
	public OfferingCredit getThis() {

	    return this;
	}
    }
    
    public class CollectingCoins extends AbstractState {

 	private static final long serialVersionUID = 1L;
	private CoinBox coinBox;
        private Vector<Event> inputEvents;
        private Vector<State> transitionStates;
        private final State offeringCredit;
       
	public CollectingCoins(CoinBox coinBox) { 
	    super("CollectingCoins"); 
	    this.coinBox = coinBox; 
	    this.offeringCredit = new OfferingCredit(coinBox, this);
	}
	public Vector<State> getTransitionStates() { return transitionStates; }
	public Vector<Event> getInputEvents() { return inputEvents; }
	public void init() {
	    Event[] events = { Coin.Nickle, Coin.Dime, Coin.Quarter, Coin.Dollar, Return.Coins };
	    inputEvents = new Vector<Event>(Arrays.asList(events));
	    transitionStates = new Vector<State>(Arrays.asList(offeringCredit));
	}
	public State onTransition(Event event) throws StateMachineException {
	    
            CoinBoxHandler handler = event.getThis();
            handler.handleCoinBox(coinBox);
            if ( coinBox.enoughForSoda() ) return offeringCredit;
	    return this;
	}
	@SuppressWarnings("unchecked")
	public CollectingCoins getThis() { return this; }
    }

    // soda dispenser
    class CreditTally {
        
        int tally = 0;
        void addCredit() { tally++; }
        void useCredit() { if ( tally != 0 ) tally--; }
        int total() { return tally; }
    }
    
    class ChangedServiceState extends EventWithSender {
        private static final long serialVersionUID = 1L;
        private final boolean inService;
        
        public ChangedServiceState(boolean inService) {
            super(inService ? "InService" : "OutOfService");
            this.inService = inService;
        }

        @SuppressWarnings("unchecked")
        public ChangedServiceState getThis() { return this; };
        public boolean isInService() { return inService; }
    }
    
    class PaidOneCredit extends EventWithSender {
        private static final long serialVersionUID = 1L;
        public PaidOneCredit() { super("PaidOneCredit"); }
        
        @SuppressWarnings("unchecked")
        public PaidOneCredit getThis() { return this; };
    }
    
    class SelectedSoda extends EventWithSender {
        private static final long serialVersionUID = 1L;
        public SelectedSoda() { super("SelectedSoda"); }
        
        @SuppressWarnings("unchecked")
        public SelectedSoda getThis() { return this; };
    }
    
    class OutOfService extends AbstractState {
        private static final long serialVersionUID = 1L;
        private State inService;
        private Vector<Event> inputEvents;
        private Vector<State> transitionStates;
        
        protected OutOfService(State inService) {
            super("OutOfService");
            this.inService = inService;
        }
        public void init() {
            Event[] events = { new PaidOneCredit(), new ChangedServiceState(IN_SERVICE) };
            inputEvents = new Vector<Event>(Arrays.asList(events));
            transitionStates = new Vector<State>(Arrays.asList(inService));
        }
        public State onTransition(Event event) throws StateMachineException {

            if ( event instanceof ChangedServiceState ) {
                
                ChangedServiceState serviceEvent = event.getThis();
                if ( serviceEvent.isInService() ) { return inService; }
            } else {
                
                EventWithSender oneCredit = event.getThis();
                oneCredit.getEventSender().postPriorityEvent(Rejected.SodaCredit);
            }
            return this;
        }
        public Vector<State> getTransitionStates() { return transitionStates; }
        public Vector<Event> getInputEvents() { return inputEvents; }
        @SuppressWarnings("unchecked")
        public OutOfService getThis() { return this; }
    }
    
    class InService extends AbstractState {
        private static final long serialVersionUID = 1L;
        private CreditTally creditTally;
        private Vector<Event> inputEvents;
        private Vector<State> transitionStates;
        private final State outOfService;
        
        public InService(CreditTally creditTally) {
            super("InService");
            this.creditTally = creditTally;
            this.outOfService = new OutOfService(this);
        }

        public void init() {
            Event[] events = { new PaidOneCredit(), new SelectedSoda(),
                               new ChangedServiceState(OUT_OF_SERVICE) };
            inputEvents = new Vector<Event>(Arrays.asList(events));
            transitionStates = new Vector<State>(Arrays.asList(outOfService));
        }
        public State onTransition(Event event) throws StateMachineException {

            if ( event instanceof ChangedServiceState ) {
                
                ChangedServiceState serviceEvent = event.getThis();
                if ( ! serviceEvent.isInService() ) { return outOfService; }
                
            } else if (event instanceof PaidOneCredit) {
                
                creditTally.addCredit();
                EventWithSender paidOneCredit = event.getThis();
                paidOneCredit.getEventSender().postPriorityEvent(Accepted.SodaCredit);
                
            } else {
                
                creditTally.useCredit();
            }
            return this;
        }
        public Vector<State> getTransitionStates() { return transitionStates; }
        public Vector<Event> getInputEvents() { return inputEvents; }
        @SuppressWarnings("unchecked")
        public InService getThis() { return this; }
    }
    
    private CreditTally creditTally;
    private CoinBox coinBox;
    private Queue<Event> normalQueue;
    private GeneralStateMachineContainer gsmc;
    private FiniteStateMachine moneyCollector;
    private FiniteStateMachine sodaDispenser;
    private StandardStateJuncture collectingCoinsJuncture;
    private InService inService;
    private CollectingCoins collectingCoins;
    private TriggerHandle triggerHandle;
    
    @Before
    public void setUp() throws Exception
    {
        creditTally = new CreditTally();
        coinBox = new CoinBox(100); // one dollar soda
        normalQueue = new LinkedList<Event>();
        gsmc = new GeneralStateMachineContainer("SodaMachine", normalQueue);
        
        collectingCoins = new CollectingCoins(coinBox);
        moneyCollector = new FiniteStateMachine("MoneyCollector");
        moneyCollector.setup(collectingCoins);
        collectingCoinsJuncture 
            = new StandardStateJuncture(moneyCollector, 
                                        collectingCoins.getTransitionStates().get(0));
        inService = new InService(creditTally);
        sodaDispenser = new FiniteStateMachine("SodaDispenser");
        sodaDispenser.setup(inService);
        
        triggerHandle = new TriggerHandle(1);
    }

    @Test
    public void testKeyInput() {

        assertEquals(Coin.class, gsmc.keyInput(Coin.Quarter));
    }

    @Test
    public void testGetName() {

        assertEquals("SodaMachine", gsmc.getName());
    }

    @Test
    public void testAddStateMachine() {

        addStateMachines();
        assertNotNull(gsmc.findStateMachine("MoneyCollector"));
        assertNotNull(gsmc.findStateMachine("SodaDispenser"));
    }

    @Test
    public void testFindStateMachine() {

        addStateMachines();
        assertNotNull(gsmc.findStateMachine("MoneyCollector"));
        assertNotNull(gsmc.findStateMachine("SodaDispenser"));
    }

    @Test
    public void testRemoveStateMachine() {

        addStateMachines();
        gsmc.removeStateMachine(moneyCollector);
        gsmc.removeStateMachine(sodaDispenser);
        assertNull(gsmc.findStateMachine("MoneyCollector"));
        assertNull(gsmc.findStateMachine("SodaDispenser"));
    }

    @Test
    public void testDeliverNextEvent() throws StateMachineException {

        addTrigger(triggerHandle);
        addStateMachines();
        
        normalQueue.offer(Coin.Dollar);
        gsmc.deliverNextEvent();
        assertTrue(normalQueue.isEmpty());
        assertEquals("OfferingCredit", moneyCollector.getCurrentState().getName());
        assertEquals(100, coinBox.getCoinTotal());
        assertEquals(0, creditTally.total());
        gsmc.deliverNextEvent();
        assertEquals(1, creditTally.total());
        gsmc.deliverNextEvent();
        assertEquals("CollectingCoins", moneyCollector.getCurrentState().getName());
        assertEquals(0, coinBox.getCoinTotal());
        
        normalQueue.offer(new SelectedSoda());
        gsmc.deliverNextEvent();
        assertEquals(0, creditTally.total());
        
    }

    @Test
    public void testAddTrigger() {

        addTrigger(triggerHandle);
        Trigger trigger = gsmc.triggerHandleToTrigger.get(triggerHandle);
        assertNotNull(trigger);
    }

    @Test
    public void testRemoveTrigger() {

        addTrigger(triggerHandle);
        gsmc.removeTrigger(triggerHandle);
    }

    @Test
    public void testHashCode() {

        GeneralStateMachineContainer gsmc2 = new GeneralStateMachineContainer("SodaMachine", normalQueue);
        assertTrue(gsmc2.hashCode() == gsmc.hashCode());
    }

    @Test
    public void testEqualsObject() {

        GeneralStateMachineContainer gsmc2 = new GeneralStateMachineContainer("SodaMachine", normalQueue);
        assertTrue(gsmc2.equals(gsmc));
    }

    private void addTrigger(TriggerHandle triggerHandle) {
        
        gsmc.addTrigger(triggerHandle, 
                new RepeatingTrigger(new PaidOneCredit(), collectingCoinsJuncture));
    }
    
    private void addStateMachines() {
        
        gsmc.addStateMachine(moneyCollector);
        gsmc.addStateMachine(sodaDispenser);
    }

}
