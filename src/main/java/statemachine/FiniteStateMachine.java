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
import java.util.HashSet;
import java.util.Set;
import java.util.Vector;

import statemachine.Event;
import statemachine.State;
import statemachine.StateMachine;
import statemachine.StateMachineException;

/**
 * This implements the methods of StateMachine.
 * <p>
 * @see StateMachine
 */
public class FiniteStateMachine implements StateMachine, Serializable
{
    private static final long serialVersionUID = 8234049801391231988L;
    private final String name;
    private State arrayOfStates[] = new State[0];
    private Event arrayOfInputEvents[] = new Event[0];
    private State currentState = null;
    private Event lastestEvent = null;
    
    public FiniteStateMachine(String name)
    {
        this.name = name;
    }
    
    /**
     * Gets the name associated with a FiniteStateMachine.
     * <p>
     * @return name
     */
    @Override
    public String getName()
    {
        return name;
    }

    /**
     * Initializes the FiniteStateMachine with a list of all States and
     * all Events handled by the FiniteStateMachine. 
     * Beginning with the startState, the FiniteStateMachine obtains the
     * State transitions and the associated Events from each State.
     * The process is repeated until no new States or Events are found.
     * The current State is set to the startState.
     * <p> 
     * - stateState is the initial State of the FiniteStateMachine.
     * The first Event input to the FiniteStateMachine is passed to the
     * OnTransition method of the startState.
     * <p>
     * @param startState
     * @see State
     * @see Event
     */
   @Override
    public synchronized void setup(State startState)
    {
        Set<Event> setOfInputEvents = new HashSet<Event>();
        Set<State> setOfStates = new HashSet<State>();
        
        addState(startState, setOfInputEvents, setOfStates);
        addSuccessorStates(startState, setOfInputEvents, setOfStates);
        currentState = startState;
        
        // put arrayOfStates and arrayOfInputEvents in arrays for efficient access
        arrayOfStates = setOfStates.toArray(arrayOfStates);
        arrayOfInputEvents = setOfInputEvents.toArray(arrayOfInputEvents);
    }
    
   /**
    * Executes a state transition.
    * First, it calls the OnTransition method of the current State.
    * Then, it sets the State returned by OnTransition as the new
    * current State and calls its onEntry method.
    * The event and eventSender arguments are passed as 
    * the arguments of the two method calls.
    * <p>
    * @param event
    * @throws FiniteStateMachineException 
    * @see State
    * @see Event
    * @see EventSender
    */
   @Override
    public synchronized void input(Event event) throws StateMachineException
    {
        if ( currentState.getInputEvents().contains(event))
        {
            currentState = currentState.onTransition(event);
            currentState.onEntry(event);
            lastestEvent = event;
        }
    }
    
   /**
    * Gets the current State of the FiniteStateMachine.
    * <p>
    * @return current State
    * @see State
    */
   @Override
    public synchronized State getCurrentState()
    {
        return currentState;
    }
    
   /**
    * Gets the last input Event of the FiniteStateMachine.
    * <p>
    * @return last Event input to this FiniteStateMachine
    * @see Event
    */
    @Override
    public synchronized Event getLatestEvent()
    {
        return lastestEvent;
    }

    /**
     * Get all states that may be transitioned to within the FiniteStateMachine.
     * <p>
     * - states is an array of States
     * <p>
     * @return states
     * @see State
     */
    @Override
    public State[] getStates()
    {
        int length = arrayOfStates.length;
        State[] array = new State[length];
        System.arraycopy(arrayOfStates, 0, array, 0, length);
        return array;
    }
    
    /**
     * Get all events accepted by the FiniteStateMachine
     * <p>
     * - events is an array of Events
     * <p>
     * @return events
     */
   @Override
    public Event[] getInputEvents()
    {
        int length = arrayOfInputEvents.length;
        Event[] array = new Event[length];
        System.arraycopy(arrayOfInputEvents, 0, array, 0, length);
        return array;
    }
    
    /**
     * Allows a subclass to produce customized code for Event initialization.
     * The default is to do nothing.
     * <p>
     * @param event
     */
    protected void init(Event event) 
    {
    }
    
    /**
     * Allows a subclass to produce customized code for State initialization.
     * The default is to call State.init
     * <p>
     * @param state
     */
    protected void init(State state) 
    {
        state.init();
    }

    /**
     * Adds a new State to the Set of transition States
     * and calls the State's init method.
     * <p>
     * It also adds all of the State's transition Events
     * to the Set of transition Events.
     * <p>
     * Returns true if the State was added, false if the State was already present.
     * @param state
     * @param setOfInputEvents 
     * @param setOfStates 
     * @return true/false
     */
    private boolean addState(State state, Set<Event> setOfInputEvents, Set<State> setOfStates)
    {
        if ( setOfStates.contains(state) )
        {
            return false;
        }
        
        state.init();
        setOfStates.add(state);
        
        Vector<Event> inputEvents = state.getInputEvents();
        if ( inputEvents != null )
        {
            setOfInputEvents.addAll(inputEvents);
        }
        
        return true;
    }

    /**
     * Adds the successor States of a State.
     * <p>
     * Only newly encountered States are added;
     * this is enforced by the addState method.
     * <p>
     * @param state
     * @param setOfInputEvents 
     * @param setOfStates 
     */
    private void addSuccessorStates(State state, Set<Event> setOfInputEvents, Set<State> setOfStates)
    {
        Vector<State> successorStates = state.getTransitionStates();
        if ( successorStates != null )
        {
            for ( State successorState : successorStates )
            {
                if ( addState(successorState, setOfInputEvents, setOfStates) )
                {
                    addSuccessorStates(successorState, setOfInputEvents, setOfStates);
                }
            }
        }
    }
    
    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (!(obj instanceof FiniteStateMachine))
        {
            return false;
        }
        FiniteStateMachine other = (FiniteStateMachine) obj;
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

    @Override
    public String toString()
    {
        return getName();
    }

}
