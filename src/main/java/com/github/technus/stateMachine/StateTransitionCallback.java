package com.github.technus.stateMachine;

/**
 * Trowing runnable
 *
 * @param <StateT>   StateMachine Context
 * @param <ContextT> Data Context
 */
public interface StateTransitionCallback<StateT, ContextT> {
    void run(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
