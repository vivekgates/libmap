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

package org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.algorithm;


import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.algorithm.localsearch.AnnealingSearch;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.algorithm.localsearch.HillClimbing;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.CostNode;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.HeuristicNode;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.Node;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.impl.ADStarNodeExpander;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.impl.ADStarNodeFactory;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.impl.ADStarNodeImpl;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.problem.SearchComponents;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.problem.SearchProblem;

import java.util.Collections;

public final class Hipster {

	private Hipster() {

	}


	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> AStar<A, S, C, N> createAStar(
			SearchProblem<A, S, N> components) {
		return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Dijkstra algorithm (A* algorithm with no heuristic
	 * function) given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 *         problem definition, using no heuristic.
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> AStar<A, S, C, N> createDijkstra(
			SearchProblem<A, S, N> components) {
		// TODO: There is no difference with AStar. Actually if the NodeExpander
		// uses heuristics, this "Dijkstra" impl works as the AStar. This should
		// be changed!
		return new AStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Bellman Ford algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 *         for the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends CostNode<A, S, C, N>> BellmanFord<A, S, C, N> createBellmanFord(
			SearchProblem<A, S, N> components) {
		return new BellmanFord<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Breadth First Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         the problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> BreadthFirstSearch<A, S, N> createBreadthFirstSearch(
			SearchProblem<A, S, N> components) {
		return new BreadthFirstSearch<A, S, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Depth First Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> DepthFirstSearch<A, S, N> createDepthFirstSearch(
			SearchProblem<A, S, N> components) {
		return new DepthFirstSearch<A, S, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates Depth Limited Search algorithm for a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         problem definition
	 */
	public static <A, S, N extends Node<A, S, N>> DepthLimitedSearch<A, S, N> createDepthLimitedSearch(
            SearchProblem<A, S, N> components, int depth) {
		return new DepthLimitedSearch<A, S, N>(components.getInitialNode(), components.getFinalNode(),
				components.getExpander(), depth);
	}

	/**
	 * Instantiates a IDA* algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 *         the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> IDAStar<A, S, C, N> createIDAStar(
			SearchProblem<A, S, N> components) {
		return new IDAStar<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Hill Climbing algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param enforced
	 *            flag to use Enforced Hill Climbing instead of classic Hill
	 *            Climbing algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         for the problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> HillClimbing<A, S, C, N> createHillClimbing(
            SearchProblem<A, S, N> components, boolean enforced) {
		return new HillClimbing<A, S, C, N>(components.getInitialNode(), components.getExpander(), enforced);
	}

	/**
	 * Instantiates an AnnealingSearch algorithm given a problem definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param alpha
	 *            coefficient of the geometric cooling schedule
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         for the problem definition
	 */
	public static <A, S, N extends HeuristicNode<A, S, Double, N>> AnnealingSearch<A, S, N> createAnnealingSearch(
            SearchProblem<A, S, N> components, Double alpha, Double minTemp,
            AnnealingSearch.AcceptanceProbability acceptanceProbability, AnnealingSearch.SuccessorFinder<A, S, N> successorFinder) {
		return new AnnealingSearch<A, S, N>(components.getInitialNode(), components.getExpander(), alpha,
				minTemp, acceptanceProbability, successorFinder);
	}

	/**
	 * Instantiates a Multi-objective Label Setting algorithm given a problem
	 * definition.
	 *
	 * @param components
	 *            search problem definition with the components of the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 * @param <N>
	 *            type of the nodes
	 * @return instance of
	 *         problem definition
	 */
	public static <A, S, C extends Comparable<C>, N extends HeuristicNode<A, S, C, N>> MultiobjectiveLS<A, S, C, N> createMultiobjectiveLS(
			SearchProblem<A, S, N> components) {
		return new MultiobjectiveLS<A, S, C, N>(components.getInitialNode(), components.getExpander());
	}

	/**
	 * Instantiates a Anytime Dynamic A* algorithm given the search components.
	 * Search components can be obtained easily for graph-based problems using
	 *
	 * @param components
	 *            search components to be used by the algorithm
	 * @param <A>
	 *            type of the actions
	 * @param <S>
	 *            type of the states
	 * @param <C>
	 *            type of the cost
	 *         for the search components
	 */
	public static <A, S, C extends Comparable<C>> ADStarForward<A, S, C, ADStarNodeImpl<A, S, C>> createADStar(
			SearchComponents<A, S, C> components) {
		// node factory instantiation
		ADStarNodeFactory<A, S, C> factory = new ADStarNodeFactory<A, S, C>(components);
		// node expander instantiation
		ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>> expander = new ADStarNodeExpander<A, S, C, ADStarNodeImpl<A, S, C>>(
				components, factory, 1.0);
		// instantiate algorithm
		return new ADStarForward(components.getBegin(), Collections.singleton(components.getGoal()), expander);
	}
}
