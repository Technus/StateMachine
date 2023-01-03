package com.github.technus.stateMachine;

import lombok.*;

import java.util.List;

/**
 * A unit in which the {@link StateMachine} can be
 * @param <ContextT> of the machine
 * @param <UserDataT> attached to the state
 */
public interface State<ContextT, UserDataT> extends UserDataSupplier<UserDataT> {
    /**
     * Deprecated use the getter method to cast for you.
     */
    @SuppressWarnings("all")
    @Deprecated
    State<?, ?> UNDEFINED = SimpleState.builder().build();

    /**
     * State placeholder to use instead of 'null'
     * It will not contain any user data ever, you must explicitly check against 'this' state instance.
     * @return Casted version of {@link State#UNDEFINED},
     * @param <ContextT> of the machine
     * @param <UserDataT> attached to the state
     */
    @SuppressWarnings("all")
    static <ContextT, UserDataT> State<ContextT, UserDataT> undefinedState() {
        return (State<ContextT, UserDataT>) UNDEFINED;
    }

    /**
     * List of callbacks to call when entering state.
     * @return list of callbacks
     */
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onEntryCallbacks();

    /**
     * List of callbacks to call when exiting state.
     * @return list of callbacks
     */
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onLeaveCallbacks();

    /**
     * The callback caller, used by {@link StateMachine}
     * @param previous state
     * @param context of the machine
     */
    @SneakyThrows
    default void onEntry(State<ContextT, UserDataT> previous, ContextT context) {
        for (final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> stateTransitionCallback : onEntryCallbacks()) {
            stateTransitionCallback.run(previous, this, context);
        }
    }

    /**
     * The callback caller, used by {@link StateMachine}
     * @param next state
     * @param context of the machine
     */
    @SneakyThrows
    default void onLeave(State<ContextT, UserDataT> next, ContextT context) {
        for (final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> stateTransitionCallback : onLeaveCallbacks()) {
            stateTransitionCallback.run(this, next, context);
        }
    }
}
