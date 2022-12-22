package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

import java.util.List;

public interface StateTransition<StateT, ContextT, UserDataT> extends StateTransitionFunction<StateT, ContextT, StateT>, UserDataSupplier<UserDataT> {
    List<StateTransitionCallback<StateT, ContextT>> beginCallbacks();

    List<StateTransitionCallback<StateT, ContextT>> finishCallbacks();

    @SneakyThrows
    default void onBegin(StateT previous, StateT next, ContextT context) {
        for (val stateTransitionCallback : beginCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    @SneakyThrows
    default void onFinish(StateT previous, StateT next, ContextT context) {
        for (val stateTransitionCallback : finishCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    /**
     * This predicate controls if the state should be changed using this transition
     *
     * @param previousState previous/current state
     * @param nextState     desired/next state
     * @param context       data context
     * @return null to cancel transition or else target state
     * @throws Throwable just in case can throw
     */
    @Override
    StateT apply(StateT previousState, StateT nextState, ContextT context) throws Throwable;
}
