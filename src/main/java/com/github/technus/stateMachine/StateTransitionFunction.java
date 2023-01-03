package com.github.technus.stateMachine;

/**
 * Trowing runnable
 *
 * @param <StateT>   StateMachine Context
 * @param <ContextT> Data Context
 */
public interface StateTransitionFunction<StateT, ContextT, ReturnT> {
    /**
     * Arbitrary function
     * @param previousState of the machine
     * @param nextState of the machine
     * @param context of the machine
     * @return some useful value (or not)
     * @throws Throwable in case something happens
     */
    ReturnT apply(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
