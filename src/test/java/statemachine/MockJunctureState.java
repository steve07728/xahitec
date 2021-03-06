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

import java.util.Vector;

import statemachine.AbstractState;
import statemachine.Event;
import statemachine.State;
import statemachine.StateMachineException;
import statemachine.juncture.JunctureState;

public class MockJunctureState extends AbstractState implements JunctureState {

    private static final long serialVersionUID = 1L;

    private final boolean junctureState;

    private final boolean combinedJunctureState;

    public MockJunctureState(boolean junctureState, boolean combinedJunctureState) {
        super("MockJunctureState");
        this.junctureState = junctureState;
        this.combinedJunctureState = combinedJunctureState;
    }

    public MockJunctureState(String name) {
        super(name);
        this.junctureState = false;
        this.combinedJunctureState = false;
    }

    @Override
    public boolean isJunctureState() {

        return junctureState;
    }

    @Override
    public boolean isCombinedJunctureState() {

        return combinedJunctureState;
    }

    @Override
    public State onTransition(Event event) throws StateMachineException {

        return null;
    }

    @Override
    public Vector<State> getTransitionStates() {

        return null;
    }

    @Override
    public Vector<Event> getInputEvents() {

        return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public MockJunctureState getThis() {

        return this;
    }

}
