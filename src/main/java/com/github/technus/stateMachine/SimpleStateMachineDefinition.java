package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.*;
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

    public SimpleStateMachineDefinition(List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPreChangeCallbacks,
                                        List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onInterChangeCallbacks,
                                        List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onPostChangeCallbacks,
                                        Map<KeyT, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT>> transitions,
                                        Map<KeyT, State<ContextT, UserDataT>> states,
                                        UserDataT userData,
                                        Function<UserDataT, KeyT> keyExtractor) {
        this.onPreChangeCallbacks = new ArrayList<>(onPreChangeCallbacks);
        this.onInterChangeCallbacks = new ArrayList<>(onInterChangeCallbacks);
        this.onPostChangeCallbacks = new ArrayList<>(onPostChangeCallbacks);
        this.transitions = new HashMap<>(transitions);
        this.states = new HashMap<>(states);
        this.userData = userData;
        this.keyExtractor = keyExtractor;
    }

    public static class SimpleStateMachineDefinitionBuilder<ContextT, UserDataT, KeyT>{
        public SimpleStateMachineDefinitionBuilder<ContextT, UserDataT, KeyT> registerState(State<ContextT, UserDataT> state){
            state(keyExtractor.apply(state.userData()),state);
            return this;
        }

        public SimpleStateMachineDefinitionBuilder<ContextT, UserDataT, KeyT> registerTransition(StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition){
            transition(keyExtractor.apply(transition.userData()),transition);
            return this;
        }
    }
}
