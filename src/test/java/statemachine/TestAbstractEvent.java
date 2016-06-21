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

import org.junit.Before;
import org.junit.Test;

public class TestAbstractEvent {

    private class ConcreteEvent extends AbstractEvent {

        private static final long serialVersionUID = 1L;

        protected ConcreteEvent(String name) {
            super(name);
        }

        @SuppressWarnings("unchecked")
        @Override
        public ConcreteEvent getThis() {

            return this;
        }

        public boolean isConcrete() {

            return true;
        }
    }

    private AbstractEvent event;

    @Before
    public void setUp() throws Exception {

        event = new ConcreteEvent("Event");
    }

    @Test
    public void testGetName() {

        AbstractEvent anotherEvent = new ConcreteEvent("Event");
        assertEquals("Event", anotherEvent.getName());
    }

    @Test
    public void testGetThis() {

        ConcreteEvent myEvent = event.getThis();
        assertTrue(myEvent.isConcrete());
    }

    @Test
    public void testHashCode() {

        AbstractEvent anotherEvent = new ConcreteEvent("Event");
        assertTrue(event.hashCode() == anotherEvent.hashCode());

        AbstractEvent totherEvent = new ConcreteEvent("EventPrime");
        assertFalse(event.hashCode() == totherEvent.hashCode());
        assertFalse(event.hashCode() == anotherEvent.getName().hashCode());
    }

    @Test
    public void testEqualsObject() {

        AbstractEvent anotherEvent = new ConcreteEvent("Event");
        assertTrue(event.equals(anotherEvent));

        AbstractEvent totherEvent = new ConcreteEvent("EventPrime");
        assertFalse(event.equals(totherEvent));
    }

    @Test
    public void testToString() {

        AbstractEvent totherEvent = new ConcreteEvent("EventPrime");
        assertEquals("EventPrime", totherEvent.toString());
    }

}
