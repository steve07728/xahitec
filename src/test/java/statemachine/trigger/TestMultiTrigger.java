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

package statemachine.trigger;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import statemachine.Event;
import statemachine.MockFiniteStateMachine;
import statemachine.MockJunctureEvent;
import statemachine.MockJunctureState;
import statemachine.State;
import statemachine.StateMachine;
import statemachine.StateMachineException;
import statemachine.juncture.CustomEventJuncture;

public class TestMultiTrigger
{
    private StateMachine fsm;
    private CustomEventJuncture juncture;
    private Event event;
    private State state;
    private Trigger trigger;
    private Event triggerEvent;

    @Before
    public void setUp() throws Exception
    {
        event = new MockJunctureEvent(true, true);
        state = new MockJunctureState(true, true);
        fsm = new MockFiniteStateMachine(state, event);
        juncture = new CustomEventJuncture(fsm);
        triggerEvent = event;
    }

    @Test
    public void testIsActive() throws StateMachineException
    {
        trigger = new RepeatingTrigger(triggerEvent, juncture);
        assertTrue(trigger.isActive(fsm));
        
        Event eventTwo = new MockJunctureEvent(false, false);
        fsm.input(eventTwo);
        
        assertFalse(trigger.isActive(fsm));
    }

    @Test
    public void testIsDone()
    {
        trigger = new RepeatingTrigger(event, juncture);
        assertFalse(trigger.isDone());
    }

    @Test
    public void testGetEvent()
    {
        trigger = new RepeatingTrigger(triggerEvent, juncture);
        assertEquals(triggerEvent, trigger.getEvent());
    }

}
