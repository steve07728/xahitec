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

package statemachine.juncture;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import statemachine.Event;
import statemachine.MockFiniteStateMachine;
import statemachine.MockJunctureEvent;
import statemachine.MockJunctureState;
import statemachine.State;
import statemachine.StateMachine;

public class TestStandardStateJuncture
{

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testIsJunctureConditionMet()
    {
        StandardStateJuncture juncture;
        Event event = new MockJunctureEvent("EventOne");
        State state = new MockJunctureState("StateOne");
        StateMachine fsm;
        
        fsm = new MockFiniteStateMachine(state, event);
        juncture = new StandardStateJuncture(fsm, state);
        assertTrue(juncture.isJunctureConditionMet());

        state = new MockJunctureState("StateTwo");
        juncture = new StandardStateJuncture(fsm, state);
        assertFalse(juncture.isJunctureConditionMet());
    }

}
