package com.github.technus.stateMachine;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class SimpleStateTransition<ContextT, UserDataT> implements StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> {
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> beginCallbacks;
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> finishCallbacks;
    /**
     * Additional metadata
     */
    private final UserDataT userData;

    @Override
    public State<ContextT, UserDataT> apply(State<ContextT, UserDataT> previousState, State<ContextT, UserDataT> nextState, ContextT context) {
        return nextState;
    }

    public SimpleStateTransition(List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> beginCallbacks,
                                 List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> finishCallbacks,
                                 UserDataT userData) {
        this.beginCallbacks = new ArrayList<>(beginCallbacks);
        this.finishCallbacks = new ArrayList<>(finishCallbacks);
        this.userData = userData;
    }
}
