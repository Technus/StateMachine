package com.github.technus.stateMachine;

/**
 * Trowing runnable
 *
 * @param <StateT>   StateMachine Context
 * @param <ContextT> Data Context
 */
public interface StateTransitionFunction<StateT, ContextT, ReturnT> {
    ReturnT apply(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
