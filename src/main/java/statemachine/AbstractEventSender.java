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
 * This implements the methods of EventSender.
 * <p>
 * @see EventSender
 */
public abstract class AbstractEventSender implements Serializable, EventSender
{
    private static final long serialVersionUID = -8400967194268060160L;
    private final Queue<Event> normalQueue;
    private final Queue<Event> priorityQueue;
    
    public AbstractEventSender(Queue<Event> normalQueue, Queue<Event> priorityQueue)
    {
        this.normalQueue = normalQueue;
        this.priorityQueue = priorityQueue;
    }

    /**
     * Puts an event on the queue hosted by the
     * EventConveyor that feeds the StateMachineContainer
     * which holds the StateMachine
     * containing the State or Event calling this method.
     * <p>
     * @param event
     * @see EventConveyor
     * @see StateMachineContainer
     * @see StateMachine
     * @see State
     * @see Event
     */
    @Override
    public void postEvent(Event event)
    {
        normalQueue.offer(event);
    }

    /**
     * Puts an event on the queue hosted by the
     * StateMachineContainer which holds the StateMachine
     * containing the State or Event calling this method.
     * <p>
     * @param event
     * @see StateMachineContainer
     * @see StateMachine
     * @see State
     * @see Event
     */
    @Override
    public void postPriorityEvent(Event event)
    {
        priorityQueue.offer(event);
    }

}
