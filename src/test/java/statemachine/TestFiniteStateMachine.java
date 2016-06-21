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
import java.util.HashSet;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class TestFiniteStateMachine {

    public enum AtmEventDirection {
        Forward, Backward, Down,;
    }

    public enum AtmEvent implements Event {
        Connected(AtmEventDirection.Forward), LoadSuccess(AtmEventDirection.Forward), LoadFail(
                AtmEventDirection.Backward), StartUp(AtmEventDirection.Forward), ShutDown(
                        AtmEventDirection.Backward), ConnectionLost(AtmEventDirection.Down), ConnectionClosed(
                                AtmEventDirection.Down), ConnectionRestored(AtmEventDirection.Forward),;

        private final AtmEventDirection direction;

        private AtmEvent(AtmEventDirection direction) {
            this.direction = direction;
        }

        @Override
        public String getName() {

            return name();
        }

        @SuppressWarnings("unchecked")
        @Override
        public AtmEvent getThis() {

            return this;
        }

        public AtmEventDirection getDirection() {

            return direction;
        }
    }

    public class AtmAction {

    }

    public enum AtmState implements State {
        Idle, 
        Loading, 
        OutOfService, 
        InService, 
        Disconnected,
        ;

        // This array is indexed by the ordinal of element of AtmEventDirection
        private State[] nextState;

        private Vector<State> allStates = new Vector<State>();

        private Vector<Event> inputEvents = new Vector<Event>();

        @Override
        public String getName() {

            return name();
        }

        @SuppressWarnings("unchecked")
        @Override
        public AtmState getThis() {
            
            return this;
        }
        
        @Override
        public void init() {
            // This transition description came from
            // https://github.com/hekailiang/squirrel
            // There are not enough states and transitions
            // to properly handle the failure conditions

            // Each AtmEvent has a direction
            // Each AtmState will transition to another state
            // based on the direction (see AtmState.onTransition)
            // If an AtmState does not transition on a particular event
            // it remains in the same state.

            switch (this) {
            case Idle:
                addTransitionStates(Loading);
                addInputEvents(AtmEvent.Connected);
                AtmState[] idleNextState = { Loading, null, null };
                this.nextState = idleNextState;
                break;
            case Loading:
                addTransitionStates(Disconnected, InService, OutOfService);
                addInputEvents(AtmEvent.ConnectionClosed, AtmEvent.LoadSuccess, AtmEvent.LoadFail);
                AtmState[] loadingNextState = { InService, OutOfService, Disconnected };
                this.nextState = loadingNextState;
                break;
            case InService:
                addTransitionStates(Disconnected, OutOfService);
                addInputEvents(AtmEvent.ConnectionLost, AtmEvent.ShutDown);
                AtmState[] inserviceNextState = { null, OutOfService, Disconnected };
                this.nextState = inserviceNextState;
                break;
            case OutOfService:
                addTransitionStates(Disconnected, InService);
                addInputEvents(AtmEvent.ConnectionLost, AtmEvent.StartUp);
                AtmState[] outofserviceNextState = { InService, null, Disconnected };
                this.nextState = outofserviceNextState;
                break;
            case Disconnected:
                addTransitionStates(InService);
                addInputEvents(AtmEvent.ConnectionRestored);
                AtmState[] disconnectedNextState = { InService, null, null };
                this.nextState = disconnectedNextState;
                break;
            }
        }

        @Override
        public void onEntry(Event event) throws StateMachineException {}

        @Override
        public State onTransition(Event event) throws StateMachineException {

            AtmEvent atmEvent = event.getThis();

            return nextState[atmEvent.getDirection().ordinal()];
        }

        @Override
        public Vector<State> getTransitionStates() {

            return allStates;
        }

        @Override
        public Vector<Event> getInputEvents() {

            return inputEvents;
        }

        private void addInputEvents(AtmEvent... events) {

            inputEvents = new Vector<Event>(Arrays.asList(events));
        }

        private void addTransitionStates(AtmState... states) {

            allStates = new Vector<State>(Arrays.asList(states));
        }
    }

    private static FiniteStateMachine fsm = new FiniteStateMachine("Fsm");

    @Before
    public void setUp() throws Exception {

        fsm.setup(AtmState.Idle);
    }

    @Test
    public void testGetName() {

        assertEquals("Fsm", fsm.getName());
    }

    @Test
    public void testSetup() {

        assertEquals(AtmEvent.values().length, fsm.getInputEvents().length);
        assertEquals(AtmState.values().length, fsm.getStates().length);
    }

    @Test
    public void testInput() throws StateMachineException {

        // Not a transition event for Idle State
        fsm.input(AtmEvent.StartUp);
        assertEquals(AtmState.Idle, fsm.getCurrentState());

        fsm.input(AtmEvent.Connected);
        assertEquals(AtmState.Loading, fsm.getCurrentState());

        fsm.input(AtmEvent.LoadSuccess);
        assertEquals(AtmState.InService, fsm.getCurrentState());

        fsm.input(AtmEvent.ShutDown);
        assertEquals(AtmState.OutOfService, fsm.getCurrentState());

        fsm.input(AtmEvent.StartUp);
        assertEquals(AtmState.InService, fsm.getCurrentState());

        // Not a transition event for InService State
        fsm.input(AtmEvent.ConnectionClosed);
        assertEquals(AtmState.InService, fsm.getCurrentState());

        fsm.input(AtmEvent.ConnectionLost);
        assertEquals(AtmState.Disconnected, fsm.getCurrentState());

    }

    @Test
    public void testGetCurrentState() {

        assertEquals(AtmState.Idle, fsm.getCurrentState());
    }

    @Test
    public void testGetLatestEvent() throws StateMachineException {

        fsm.input(AtmEvent.StartUp);
        assertEquals(AtmState.Idle, fsm.getCurrentState());

        fsm.input(AtmEvent.Connected);
        assertEquals(AtmState.Loading, fsm.getCurrentState());

        fsm.input(AtmEvent.LoadFail);
        assertEquals(AtmState.OutOfService, fsm.getCurrentState());
        assertEquals(AtmEvent.LoadFail, fsm.getLatestEvent());
    }

    @Test
    public void testGetStates() {

        HashSet<State> states = new HashSet<State>();
        states.addAll(Arrays.asList(fsm.getStates()));
        states.addAll(Arrays.asList(AtmState.values()));
        assertEquals(AtmState.values().length, states.size());
    }

    @Test
    public void testGetInputEvents() {

        HashSet<Event> events = new HashSet<Event>();
        events.addAll(Arrays.asList(fsm.getInputEvents()));
        events.addAll(Arrays.asList(AtmEvent.values()));
        assertEquals(AtmEvent.values().length, events.size());
    }

    @Test
    public void testHashCode() {}

    @Test
    public void testEqualsObject() {}

    @Test
    public void testToString() {

        assertEquals("Fsm", fsm.toString());
    }

}
