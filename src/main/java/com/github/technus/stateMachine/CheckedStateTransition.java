package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class CheckedStateTransition<ContextT, UserDataT> implements StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> {
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>>                       beginCallbacks;
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>>                       finishCallbacks;
    private final StateTransitionFunction<State<ContextT, UserDataT>, ContextT, State<ContextT, UserDataT>> transitionPredicate;

    private final UserDataT userData;

    @Override
    public State<ContextT, UserDataT> apply(State<ContextT, UserDataT> previousState, State<ContextT, UserDataT> nextState, ContextT context) throws Throwable {
        return transitionPredicate().apply(previousState, nextState, context);
    }

    public CheckedStateTransition(List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> beginCallbacks,
                                  List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> finishCallbacks,
                                  StateTransitionFunction<State<ContextT, UserDataT>, ContextT, State<ContextT, UserDataT>> transitionPredicate,
                                  UserDataT userData) {
        this.beginCallbacks = new ArrayList<>(beginCallbacks);
        this.finishCallbacks = new ArrayList<>(finishCallbacks);
        this.transitionPredicate = transitionPredicate;
        this.userData = userData;
    }
}
