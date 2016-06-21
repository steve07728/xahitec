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

import statemachine.Event;
import statemachine.StateMachine;
import statemachine.juncture.Juncture;

/**
 * @author steve
 *
 */
public class OneTimeTrigger implements Trigger
{
    private final Event event;
    private final Juncture juncture;
    private boolean done;
    
    /**
     * 
     */
    public OneTimeTrigger(Event event, Juncture juncture)
    {
        this.event = event;
        this.juncture = juncture;
        this.done = false;
    }

    /* (non-Javadoc)
     * @see statemachine.Trigger#isActive()
     */
    @Override
    public boolean isActive(StateMachine stateMachine)
    {
        if ( ! done && stateMachine == juncture.getStateMachine()
          && juncture.isJunctureConditionMet() ) {
            
            done = true;
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see statemachine.Trigger#isDone()
     */
    @Override
    public boolean isDone()
    {
        return done;
    }

    /* (non-Javadoc)
     * @see statemachine.Trigger#getEvent()
     */
    @Override
    public Event getEvent()
    {
        return event;
    }

}
