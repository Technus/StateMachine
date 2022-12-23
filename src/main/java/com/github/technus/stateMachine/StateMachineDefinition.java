package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface StateMachineDefinition<ContextT, UserDataT, KeyT> extends UserDataSupplier<UserDataT> {
    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPreChangeCallbacks();

    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onInterChangeCallbacks();

    List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPostChangeCallbacks();

    Map<KeyT,StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT>> transitions();

    Map<KeyT,State<ContextT, UserDataT>> states();

    Function<UserDataT,KeyT> keyExtractor();

    default StateMachineDefinition<ContextT, UserDataT, KeyT> registerTransition(StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition){
        transitions().put(keyExtractor().apply(transition.userData()), transition);
        return this;
    }

    default StateMachineDefinition<ContextT, UserDataT, KeyT> registerState(State<ContextT, UserDataT> state){
        states().put(keyExtractor().apply(state.userData()), state);
        return this;
    }

    @SneakyThrows
    default void onPreChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (val stateTransitionCallback : onPreChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    @SneakyThrows
    default void onPostChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (val stateTransitionCallback : onPostChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }

    @SneakyThrows
    default void onInterChange(State<ContextT, UserDataT> previous, State<ContextT, UserDataT> next, ContextT context) {
        for (val stateTransitionCallback : onInterChangeCallbacks()) {
            stateTransitionCallback.run(previous, next, context);
        }
    }
}
