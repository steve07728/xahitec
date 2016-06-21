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


import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import statemachine.trigger.Trigger;
import statemachine.trigger.TriggerHandle;

/**
 * This implements the functionality of a StateMachineContainer.
 * <p>
 * The StateMachines that this holds are stored in a Map.
 * The Map key is derived from an Event implementation.
 * This class is parameterized so that the extending class determines
 * how the Map key is derived. However, the Map key is derived, 
 * an Event, that each distinct derived value comes from, 
 * must only be expected by one StateMachine. Basically, this means the
 * same Event can't go to multiple StateMachines; however, Events meant
 * for a particular StateMachine can have as many distinct derived values as
 * there are Events.
 * <p>
 * This also manages junctures allowing them to be added, changed, and removed.
 * <p>
 * @see StateMachineContainer
 * @see Event
 * @see GeneralStateMachineContainer
 */
public abstract class AbstractStateMachineContainer<I> implements StateMachineContainer, Serializable
{
    private static final long serialVersionUID = 8365509840101645075L;

    private final Map<I, StateMachine> keyInputToStateMachine; 
    protected final Map<TriggerHandle, Trigger> triggerHandleToTrigger;
    private final Queue<Event> priorityEventQueue;
    private final Queue<Event> normalEventQueue;
    private final String name;
    
    public AbstractStateMachineContainer(String name, Queue<Event> normalEventQueue)
    {
        this.keyInputToStateMachine = new ConcurrentHashMap<I, StateMachine>();
        this.triggerHandleToTrigger = new ConcurrentHashMap<TriggerHandle, Trigger>();
        this.priorityEventQueue = new ConcurrentLinkedQueue<Event>();
        this.normalEventQueue = normalEventQueue;
        this.name = name;
    }
    
    /**
     * Gets a data item from an Event.
     * <p>
     * The implementing class decides the type of item
     * and where in the Event it is extracted from.
     * <p>
     * To accomplish this, the implementing class needs some
     * knowledge of the contents of the Event. However, the
     * class, GeneralStateMachineContainer does not; it uses
     * the runtime class type of the Event.
     * <p>
     * The Event interface exposes three pieces of data found
     * in an Event: eventData which is an arbitrary type,
     * keeper which is a Keeper, and type which is an arbitrary type.
     * These are easy to access, via getters. 
     * Getting anything else requires casting the Event to a derived type.
     * <p>
     * @param event
     * @return item from Event
     * @see Event
     * @see GeneralStateMachineContainer
     */
    protected abstract I keyInput(Event event);
    
    /**
     * Gets the name of the StateMachineContainer.
     * <p>
     * @return name
     */
    @Override
    public String getName()
    {
        return name;
    }
    
    /**
     * Adds a StateMachine to the StateMachineContainer.
     * <p>
     * @param stateMachine
     * @see StateMachine
     */
    @Override
    public void addStateMachine(StateMachine stateMachine)
    {
        for ( Event event : stateMachine.getInputEvents() )
        {
            keyInputToStateMachine.put(keyInput(event), stateMachine);
        }
    }
    
    /**
     * Looks for a StateMachine of the given name.
     * <p>
     * Returns the StateMachine, if found, otherwise null.
     * <p>
     * @param name
     * @return stateMachine
     * @see StateMachine
     */
   public StateMachine findStateMachine(String name)
    {
        for ( Entry<I, StateMachine> entry : keyInputToStateMachine.entrySet() )
        {
            StateMachine stateMachine = entry.getValue();
            
            if ( stateMachine.getName().equals(name) )
            {
                return stateMachine;
            }
        }
        return null;
    }
    
   /**
    * Removes a StateMachine from the StateMachineContainer.
    * <p>
    * @param stateMachine
    * @see StateMachine
    */
    @Override
    public void removeStateMachine(StateMachine stateMachine)
    {
        for ( Entry<I, StateMachine> entry : keyInputToStateMachine.entrySet() )
        {
            // Remove every entry with a matching StateMachine as a value
            if ( entry.getValue().equals(stateMachine) )
            {
                keyInputToStateMachine.remove(entry.getKey());
            }
        }
    }
    
    /**
     * Delivers an Event to a StateMachine. 
     * The Event is taken from the priority queue if non empty,
     * otherwise the normal queue.
     * <p>
     * Returns false if both the normal queue and priority queue are empty.
     * <p>
     * The StateMachineContainer holds the priority queue.
     * <p>
     * @return true/false
     * @throws StateMachineException 
     * @see StateMachine
     * @see Event
     * @see EventConveyor
     */
    @Override
    public boolean deliverNextEvent() throws StateMachineException
    {
        // dequeue event
        Event event = priorityEventQueue.poll();
        if ( event == null )
        {
            // dequeue event
            event = normalEventQueue.poll();
        }
        
        if ( event != null )
        {
            if ( isEntry(keyInput(event)) )
            {
                deliverEvent(event, normalEventQueue);
            }
            return true;
        }

        return false;
    }
    
    /**
     * Adds a new Trigger or replaces an existing one with the same TriggerHandle.
     * <p>
     * - TriggerHandle is provided by the caller and is a unique identifier of the Trigger.
     * <p>
     * @param triggerHandle
     * @param trigger
     * @return void
     * @see TriggerHandle
     * @see Trigger
     * @see StateMachine
     * @see Event
     * @see State
     */
    @Override
    public void addTrigger(TriggerHandle triggerHandle, Trigger trigger)
    {
        triggerHandleToTrigger.put(triggerHandle, trigger);
    }
    
    /**
     * Removes a Trigger.
     * - triggerHandle is provided by the caller and is a unique identifier of 
     * a previously added Trigger.
     * <p>
     * @param triggerHandle
     */
    @Override
    public void removeTrigger(TriggerHandle triggerHandle)
    {
        triggerHandleToTrigger.remove(triggerHandle);
    }

    private void postPriorityEvent(Event event)
    {
        priorityEventQueue.offer(event);
    }
    
    private boolean isEntry(I keyInput)
    {
        return keyInputToStateMachine.containsKey(keyInput);
    }
    
    private void deliverEvent(Event event, Queue<Event> normalEventQueue) throws StateMachineException
    {
        // Figure out which StateMachine handles the Event
        // The call to deliverEvent checks that the entry exists
        StateMachine stateMachine = keyInputToStateMachine.get(keyInput(event));
        
        setEventSender(event);
        
        // Give the Event to the StateMachine to pass an input to the current State
        try {
            stateMachine.input(event);
            
        } catch (StateMachineException e) {
            
            removeStateMachine(stateMachine);
            throw e;
        } 
            
        pullActiveTriggers(stateMachine);
    }
    
    /**
     * Iterates through all Triggers.
     * For each Trigger, if the Trigger condition is met, 
     * post the Trigger Event to the priority queue.
     * Remove a Trigger if it is done (will never be active again)
     * @param stateMachine 
     */
    private void pullActiveTriggers(StateMachine stateMachine)
    {
        for ( Entry<TriggerHandle, Trigger> triggerEntry : triggerHandleToTrigger.entrySet() ) {
            
            Trigger trigger = triggerEntry.getValue();
            if ( trigger.isActive(stateMachine) ) {
                
                setEventSender(trigger.getEvent());
                postPriorityEvent(trigger.getEvent());
                
                if ( trigger.isDone() ) {
                    
                    triggerHandleToTrigger.remove(triggerEntry.getKey());
                }
            }
        }
    }

    private void setEventSender(Event event) {
        
        if ( event instanceof EventWithSender ) {
            
            // Allocate an EventSender
            EventSender eventSender = new AbstractEventSender(normalEventQueue, priorityEventQueue)
            {
                private static final long serialVersionUID = -8339890364798603569L;
            };

            EventWithSender eventWithSender = event.getThis();
            eventWithSender.setEventSender(eventSender);
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (!(obj instanceof AbstractStateMachineContainer))
        {
            return false;
        }
        AbstractStateMachineContainer<?> other = (AbstractStateMachineContainer<?>) obj;
        if (name == null)
        {
            if (other.name != null)
            {
                return false;
            }
        }
        else if (!name.equals(other.name))
        {
            return false;
        }
        return true;
    }
}
