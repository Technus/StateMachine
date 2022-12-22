package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

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
    private final UserDataT                                                           userData;

    @Override
    public State<ContextT, UserDataT> apply(State<ContextT, UserDataT> previousState, State<ContextT, UserDataT> nextState, ContextT context) {
        return nextState;
    }
}
