package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

public interface StateMachine<ContextT, UserDataT,KeyT> extends UserDataSupplier<UserDataT> {
    default void uncheckedChangeState(State<ContextT, UserDataT> nextState) {
        definition().onPreChange(state(), nextState, context());
        state().onLeave(nextState, context());
        definition().onInterChange(state(), nextState, context());
        nextState.onEntry(state(), context());
        definition().onPostChange(state(), nextState, context());
    }

    default void checkedChangeState(State<ContextT, UserDataT> desiredState) {
        for (val transition : definition().transitions().values()) {
            tryChangeState(desiredState, transition);
        }
    }

    default void tryChangeState(StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        tryChangeState(State.undefinedState(), transition);
    }

    @SneakyThrows
    default void tryChangeState(State<ContextT, UserDataT> desiredState, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        val nextState = transition.apply(state(), desiredState, context());
        if (nextState != null) {
            transition.onBegin(state(), nextState, context());
            uncheckedChangeState(nextState);
            transition.onFinish(state(), nextState, context());
        }
    }

    /**
     * Current state, it must only be updated after all transition operations are done
     *
     * @return current/previous state
     */
    State<ContextT, UserDataT> state();

    /**
     * Data context
     *
     * @return the data context
     */
    ContextT context();

    StateMachineDefinition<ContextT, UserDataT,KeyT> definition();
}
