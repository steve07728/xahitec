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

import statemachine.juncture.Juncture;
import statemachine.trigger.Trigger;
import statemachine.trigger.TriggerHandle;

/**
 * A StateMachineContainer manages the delivery of Events to 
 * one or more StateMachines. It also manages the setup of Triggers.
 * A Trigger provides an Event for the container to deliver when
 * a condition (called a Juncture) occurs within one of the StateMachines.
 * <p>
 * @see AbstractStateMachineContainer
 * @see Event
 * @see StateMachine
 * @see Trigger
 * @see Juncture
 */
public interface StateMachineContainer
{
    String getName();

    void addStateMachine(StateMachine stateMachine);

    StateMachine findStateMachine(String name);

    void removeStateMachine(StateMachine stateMachine);
    
    boolean deliverNextEvent() throws StateMachineException;

    void addTrigger(TriggerHandle triggerHandle, Trigger trigger);
    
    void removeTrigger(TriggerHandle triggerHandle);
}