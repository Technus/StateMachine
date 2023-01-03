package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * Static machine definition
 * @param <ContextT> of the machine
 * @param <UserDataT> of the machine
 * @param <KeyT> of the states and transitions
 */
public interface StateMachineDefinition<ContextT, UserDataT, KeyT> extends UserDataSupplier<UserDataT> {
    /**
     * Callbacks to run before state change.
     * @return list of callbacks
     */
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPreChangeCallbacks();

    /**
     * Callbacks to run in-between state change.
     * @return list of callbacks
     */
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onInterChangeCallbacks();

    /**
     * Callbacks to run after state change.
     * @return list of callbacks
     */
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPostChangeCallbacks();

    /**
     * Map of all registered state transitions
     * @return map of all known transitions
     */
    Map<KeyT,StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT>> transitions();

    /**
     * Map of all registered states
     * @return map of all known states
     */
    Map<KeyT,State<ContextT, UserDataT>> states();

    /**
     * The key extractor for states and transitions.
     * @return the processor for user data to extract key
     */
    Function<UserDataT,KeyT> keyExtractor();

    /**
     * Registers transition under this machine definition.
     * {@link StateMachineDefinition#keyExtractor()} must be set before
     * @param transition to register
     * @return itself
     */
    default StateMachineDefinition<ContextT, UserDataT, KeyT> registerTransition(StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition){
        transitions().put(keyExtractor().apply(transition.userData()), transition);
        return this;
    }

    /**
     * Register state under this machine definition.
     * {@link StateMachineDefinition#keyExtractor()} must be set before
     * @param state to register
     * @return itself
     */
    default StateMachineDefinition<ContextT, UserDataT, KeyT> registerState(State<ContextT, UserDataT> state){
        states().put(keyExtractor().apply(state.userData()), state);
        return this;
    }

    /**
     * Helper to call all {@link StateMachineDefinition#onPreChangeCallbacks()} callbacks
     * @param previous state
     * @param next state
     * @param context of the machine
     */
    @SneakyThrows
    default void onPreChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> stateTransitionCallback : onPreChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    /**
     * Helper to call all {@link StateMachineDefinition#onInterChangeCallbacks()}
     * @param previous state
     * @param next state
     * @param context of the machine
     */
    @SneakyThrows
    default void onInterChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> stateTransitionCallback : onInterChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    /**
     * Helper to call all {@link StateMachineDefinition#onPostChangeCallbacks()}
     * @param previous state
     * @param next state
     * @param context of the machine
     */
    @SneakyThrows
    default void onPostChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> stateTransitionCallback : onPostChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }
}
