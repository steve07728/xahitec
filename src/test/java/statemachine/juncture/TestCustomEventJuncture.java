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
import statemachine.StateMachine;

public class TestCustomEventJuncture
{
    private StateMachine fsm;
    private CustomEventJuncture juncture;
    private Event event;

    @Before
    public void setUp() throws Exception
    {
    }

    @Test
    public void testIsJunctureConditionMet()
    {
        event = new MockJunctureEvent(true, false);
        fsm = new MockFiniteStateMachine(null, event);
        juncture = new CustomEventJuncture(fsm);
        assertTrue(juncture.isJunctureConditionMet());
        
        event = new MockJunctureEvent(false, true);
        fsm = new MockFiniteStateMachine(null, event);
        juncture = new CustomEventJuncture(fsm);
        assertFalse(juncture.isJunctureConditionMet());
    }

}