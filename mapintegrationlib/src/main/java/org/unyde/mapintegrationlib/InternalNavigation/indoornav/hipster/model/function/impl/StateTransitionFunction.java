/*
 * Copyright 2014 CITIUS <http://citius.usc.es>, University of Santiago de Compostela.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.impl;


import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.Transition;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.TransitionFunction;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.util.F;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.util.Function;

public abstract class StateTransitionFunction<S> implements TransitionFunction<Void, S> {

    @Override
    public Iterable<Transition<Void, S>> transitionsFrom(final S state) {
        //generate successor states
        return F.map(successorsOf(state), new Function<S, Transition<Void, S>>() {
            @Override
            public Transition<Void, S> apply(S current) {
                return Transition.create(state, null, current);
            }
        });
    }

    /**
     * Obtain the successor states of a given one.
     *
     * @param state current states
     * @return successor states of the current
     */
    public abstract Iterable<S> successorsOf(S state);
}

