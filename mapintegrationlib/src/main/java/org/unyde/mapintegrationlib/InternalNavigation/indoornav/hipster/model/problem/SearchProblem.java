package org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.problem;


import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.Node;
import org.unyde.mapintegrationlib.InternalNavigation.indoornav.hipster.model.function.NodeExpander;

public class SearchProblem<A,S,N extends Node<A,S,N>> {
    private N initialNode;
    private N finalNode;
    private NodeExpander<A,S,N> expander;

    public SearchProblem(N initialNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.finalNode = null;
        this.expander = expander;
    }

    public SearchProblem(N initialNode, N finalNode, NodeExpander<A, S, N> expander) {
        this.initialNode = initialNode;
        this.finalNode = finalNode;
        this.expander = expander;
    }

    public N getInitialNode() {
        return initialNode;
    }

    public NodeExpander<A, S, N> getExpander() {
        return expander;
    }

    public N getFinalNode() {
        return finalNode;
    }

    public void setFinalNode(N finalNode) {
        this.finalNode = finalNode;
    }
}
