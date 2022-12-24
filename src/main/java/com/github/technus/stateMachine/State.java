package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

import java.util.List;

public interface State<ContextT, UserDataT> extends UserDataSupplier<UserDataT> {
    State<?, ?> UNDEFINED = SimpleState.builder().build();

    @SuppressWarnings("unchecked")
    static <ContextT, UserDataT> State<ContextT, UserDataT> undefinedState() {
        return (State<ContextT, UserDataT>) UNDEFINED;
    }

    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onEntryCallbacks();

    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onLeaveCallbacks();

    @SneakyThrows
    default void onEntry(State<ContextT, UserDataT> previous, ContextT context) {
        for (val stateTransitionCallback : onEntryCallbacks()) {
            stateTransitionCallback.run(previous, this, context);
        }
    }

    @SneakyThrows
    default void onLeave(State<ContextT, UserDataT> next, ContextT context) {
        for (val stateTransitionCallback : onLeaveCallbacks()) {
            stateTransitionCallback.run(this, next, context);
        }
    }
}
