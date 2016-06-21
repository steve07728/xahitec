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
import java.util.Vector;

/**
 * This implements some of the methods of State as empty methods. It also
 * provides useful utility methods.
 */
public abstract class AbstractState implements State, Serializable {

    private static final long serialVersionUID = -8315405554750329078L;

    private final String name;

    protected AbstractState(String name) {
        this.name = name;
    }

    /**
     * Gets a name associated with an State. States of the same state machine
     * need unique names. The name is used by AbstractState as one of the
     * comparisons of hashCode and equals.
     * <p>
     * 
     * @return name
     * @see State
     */
    @Override
    public String getName() {

        return name;
    }

    /**
     * Allows extracting the real object behind the State interface. This is an
     * alternative to explicit casting.
     * <p>
     * 
     * @return class that implements State
     * @see State
     */
    @Override
    public abstract <S extends State> S getThis();

    /**
     * Performs initialization of the State implementation, if needed. In
     * particular, if the State implementation builds a table of States it
     * transitions into, this should be done in init() rather than a constructor
     * (or bad things can happen).
     */
    @Override
    public void init() {}

    /**
     * Is called after input to another State. It represents the point or
     * incoming arrow from a state transition diagram.
     * <p>
     * - event is the Event causing the transition.
     * <p>
     * - next State is the new state after the transition which may be the same
     * State.
     * <p>
     * 
     * @param event
     * @return next State
     * @throws StateMachineException
     */
    @Override
    public void onEntry(Event event) throws StateMachineException {}

    /**
     * Is called for an Event input to transition to another State. It
     * represents the feather or outgoing arrow from a state transition diagram.
     * <p>
     * - event is the Event causing the transition.
     * <p>
     * - next State is the new state after the transition which may be the same
     * State.
     * <p>
     * 
     * @param event
     * @return next State
     * @throws StateMachineException
     */
    @Override
    public abstract State onTransition(Event event) throws StateMachineException;

    /**
     * Gets all States this State may transition to other than itself. If there
     * are none, returns an empty Vector<State> (not null)
     * <p>
     * 
     * @return block of States
     */
    @Override
    public abstract Vector<State> getTransitionStates();

    /**
     * Gets all Events accepted by this State. If there are none, returns an
     * empty Vector<Event> (not null)
     * <p>
     * 
     * @return block of events
     */
    @Override
    public abstract Vector<Event> getInputEvents();

    /**
     * Returns a hash code value for the State.
     * <p>
     * Because States are used in Sets and Maps it is important that each one
     * has a good implementation of hashCode. AbstractState provides an
     * implementation of each that is good enough for most purposes.
     * <p>
     * 
     * @see State
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        return result;
    }

    /**
     * Indicates whether some other Event is "equal to" this one.
     * <p>
     * Because States are used in Sets and Maps it is important that each one
     * has a good implementation of equals. AbstractState provides an
     * implementation of each that is good enough for most purposes.
     * <p>
     * 
     * @see State
     */
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractState)) {
            return false;
        }
        if (!this.getClass().equals(obj.getClass())) {
            return false;
        }
        AbstractState other = (AbstractState) obj;
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        return getName();
    }

    /**
     * Returns its arguments as a Vector of States.
     * <p>
     * This is useful for implementing the getTransitionStates method.
     * <p>
     * 
     * @param states
     * @return Vector of States
     * @see State
     */
    protected Vector<State> setOfStates(State... states) {

        Vector<State> transitionStates = new Vector<State>(states.length);
        for (State state : states) {
            transitionStates.add(state);
        }
        return transitionStates;
    }

    /**
     * Returns its arguments as a Vector of Events.
     * <p>
     * This is useful for implementing the getInputEvents method.
     * <p>
     * @param events
     * @return Vector of Events
     * @see Event
     */
    protected Vector<Event> setOfEvents(Event... events) {

        Vector<Event> inputEvents = new Vector<Event>(events.length);
        for (Event event : events) {
            inputEvents.add(event);
        }
        return inputEvents;
    }
}
