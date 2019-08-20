/*
* Copyright 2013 Centro de Investigación en Tecnoloxías da Información (CITIUS).
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.impl;


import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.CostFunction;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.HeuristicFunction;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.ScalarFunction;

public class ADStarNodeUpdater<A, S, C extends Comparable<C>> {

    private final CostFunction<A, S, C> costFunction;
    private final HeuristicFunction<S, C> heuristicFunction;
    private final BinaryOperation<C> add;
    private final ScalarFunction<C> scale;
    private double epsilon;


    public ADStarNodeUpdater(CostFunction<A, S, C> costFunction,
                             HeuristicFunction<S, C> heuristicFunction, BinaryOperation<C> add,
                             ScalarFunction<C> scale, double epsilon) {
        this.costFunction = costFunction;
        this.heuristicFunction = heuristicFunction;
        this.epsilon = epsilon;
        this.add = add;
        this.scale = scale;
    }




}
