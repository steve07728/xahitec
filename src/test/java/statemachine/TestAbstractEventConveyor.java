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

import java.util.LinkedList;
import java.util.Queue;

import org.junit.Before;
import org.junit.Test;

import statemachine.trigger.Trigger;
import statemachine.trigger.TriggerHandle;

public class TestAbstractEventConveyor {

    private static Queue<Event> queue = new LinkedList<Event>();

    public enum MyEvent implements Event {
        one, two, three, four,;

        @Override
        public String getName() {

            return this.name();
        }

        @SuppressWarnings("unchecked")
        @Override
        public MyEvent getThis() {

            return this;
        }
    };

    private class MyEventConveyor extends AbstractEventConveyor {

        private static final long serialVersionUID = 1L;

        public MyEventConveyor() {
            super(queue);
        }
    }

    private EventConveyor eventConveyor = new MyEventConveyor();

    @Before
    public void setUp() throws Exception {}

    @Test
    public void testPostEvent() {

        eventConveyor.postEvent(MyEvent.one);
        eventConveyor.postEvent(MyEvent.two);
        eventConveyor.postEvent(MyEvent.three);
        eventConveyor.postEvent(MyEvent.four);
        assertTrue(queue.size() == 4);
    }

    @Test
    public void testDeliverAllEvents() throws StateMachineException {

        StateMachineContainer stateMachineContainer = new StateMachineContainer() {

            @Override
            public String getName() {

                return "stateMachineContainer";
            }

            @Override
            public void addStateMachine(StateMachine stateMachine) {}

            @Override
            public StateMachine findStateMachine(String name) {

                return null;
            }

            @Override
            public void removeStateMachine(StateMachine stateMachine) {}

            @Override
            public boolean deliverNextEvent() throws StateMachineException {

                return queue.poll() != null;
            }

            @Override
            public void addTrigger(TriggerHandle triggerHandle, Trigger trigger) {}

            @Override
            public void removeTrigger(TriggerHandle triggerHandle) {}
        };

        eventConveyor.deliverAllEvents(stateMachineContainer);
        assertTrue(queue.isEmpty());

        // No exception when queue begins empty
        eventConveyor.deliverAllEvents(stateMachineContainer);
    }

}
