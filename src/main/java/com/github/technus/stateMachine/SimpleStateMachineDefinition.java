package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

@Builder
@Getter
public class SimpleStateMachineDefinition<ContextT, UserDataT, KeyT> implements StateMachineDefinition<ContextT, UserDataT, KeyT> {
    /**
     * Handlers to call before the actual transition happen
     */
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>>         onPreChangeCallbacks;
    /**
     * Handlers to call after the actual transition happen
     */
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>>         onInterChangeCallbacks;
    /**
     * Handlers to call after the actual transition happen
     */
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>>         onPostChangeCallbacks;
    /**
     * Collects (all possible) state transitions
     */
    @Singular
    private final Map<KeyT, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT>> transitions;
    /**
     * Collects (all possible) states
     */
    @Singular
    private final Map<KeyT, State<ContextT, UserDataT>>                                       states;
    /**
     * Additional metadata
     */
    private final UserDataT                                                                   userData;
    /**
     * The UserData to key extractor
     */
    private final Function<UserDataT, KeyT>                                                   keyExtractor;
}
