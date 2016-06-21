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
import java.util.Queue;

/**
 * This implements the methods of EventConveyor.
 * <p>
 * @see EventConveyor
 */
public abstract class AbstractEventConveyor implements EventConveyor, Serializable
{
    private static final long serialVersionUID = 8354271571791836102L;
    private final Queue<Event> eventQueue;
    
    public AbstractEventConveyor(Queue<Event> eventQueue)
    {
        this.eventQueue = eventQueue;
    }

    /**
     * Adds an Event to the Event Queue.
     * Returns an indication whether the add was successful.
     * <p>
     * @param event
     * @return true/false
     */
    @Override
    public boolean postEvent(Event event)
    {
        // queue event
        return eventQueue.offer(event);
    }

    /**
     * Empties its queue by passing all of its contents to
     * a StateMachineContainer via its deliverNextEvent method.
     * For this to work the StateMachineContainer must reference the
     * same Event Queue as this object.
     * <p>
     * @param container
     * @throws StateMachineException 
     * @see StateMachineContainer
     */
    @Override
    public void deliverAllEvents(StateMachineContainer container) throws StateMachineException
    {
        boolean eventsRemain = container.deliverNextEvent();
        while ( eventsRemain )
        {
            eventsRemain = container.deliverNextEvent();
        }
    }

}
