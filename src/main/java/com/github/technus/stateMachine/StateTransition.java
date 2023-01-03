package com.github.technus.stateMachine;

import lombok.*;

import java.util.List;

/**
 * Defines the state transition of a machine
 *
 * @param <StateT>    of the machine
 * @param <ContextT>  of the machine
 * @param <UserDataT> of the transition
 */
public interface StateTransition<StateT, ContextT, UserDataT> extends StateTransitionFunction<StateT, ContextT, StateT>, UserDataSupplier<UserDataT> {
    /**
     * Callbacks to run on transition begin
     *
     * @return list of callbacks
     */
    List<StateTransitionCallback<StateT, ContextT>> beginCallbacks();

    /**
     * Callbacks to run on transition finish
     *
     * @return list of callbacks
     */
    List<StateTransitionCallback<StateT, ContextT>> finishCallbacks();

    /**
     * Helper to call all {@link StateTransition#beginCallbacks()}
     *
     * @param previous state
     * @param next     state
     * @param context  of the machine
     */
    @SneakyThrows
    default void onBegin(StateT previous, StateT next, ContextT context) {
        for (final StateTransitionCallback<StateT, ContextT> stateTransitionCallback : beginCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    /**
     * Helper to call all {@link StateTransition#finishCallbacks()}
     *
     * @param previous state
     * @param next     state
     * @param context  of the machine
     */
    @SneakyThrows
    default void onFinish(StateT previous, StateT next, ContextT context) {
        for (final StateTransitionCallback<StateT, ContextT> stateTransitionCallback : finishCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    /**
     * This predicate controls if the state should be changed using this transition
     *
     * @param previousState previous/current state
     * @param nextState     desired/next state
     * @param context       data context
     * @return null to cancel transition or else target state (can differ from nextState, useful when new state is {@link State#undefinedState()})
     * @throws Throwable just in case can throw
     */
    @Override
    StateT apply(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
