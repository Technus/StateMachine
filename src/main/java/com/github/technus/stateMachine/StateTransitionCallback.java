package com.github.technus.stateMachine;

/**
 * Trowing runnable
 *
 * @param <StateT>   StateMachine Context
 * @param <ContextT> Data Context
 */
public interface StateTransitionCallback<StateT, ContextT> {
    /**
     * A throwing callback
     *
     * @param previousState of the machine
     * @param nextState     of the machine
     * @param context       of the machine
     * @throws Throwable in case anything bad happens...
     */
    void run(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
