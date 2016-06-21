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

/**
 * @author steve
 *
 */
public abstract class AbstractEvent implements Event, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;

    protected AbstractEvent(String name) {
        this.name = name;
    }

    /**
     * Gets a name associated with an Event. Events used in the same state
     * machine need to have unique names. The name is used by AbstractEvent as
     * one of the comparisons of hashCode and equals.
     * <p>
     * 
     * @return name
     * @see Event
     */
    @Override
    public String getName() {

        return name;
    }

    /**
     * Allows extracting the real object behind the Event interface. This is an
     * alternative to explicit casting.
     * <p>
     * 
     * @return class that implements Event
     * @see Event
     */
    @Override
    public abstract <E extends Event> E getThis();

    /**
     * Returns a hash code value for the Event.
     * <p>
     * Because Events are used in Sets and Maps it is important that each one
     * has a good implementation of hashCode. AbstractEvent provides an
     * implementation of each that is good enough for most purposes.
     * 
     * @see Event
     */
    @Override
    public int hashCode() {

        final int prime = 31;
        int result = 1;
        result = prime * result + name.hashCode();
        return result;
    }

    /**
     * Indicates whether some other Event is "equal to" this one.
     * <p>
     * Because Events are used in Sets and Maps it is important that each one
     * has a good implementation of equals. AbstractEvent provides an
     * implementation of each that is good enough for most purposes.
     * 
     * @see Event
     */
    @Override
    public boolean equals(Object obj) {

        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof AbstractEvent)) {
            return false;
        }
        AbstractEvent other = (AbstractEvent) obj;
        if (!other.getClass().equals(obj.getClass())) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {

        return getName();
    }

}
