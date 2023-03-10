package com.github.technus.stateMachine;

import lombok.*;

@Builder
@Getter
public class CheckedStateTransitionCallback<ContextT, UserDataT> implements StateTransitionCallback<State<ContextT, UserDataT>, ContextT> {
    private final StateTransitionCallback<State<ContextT, UserDataT>, ContextT> transitionHandler;
    private final StateTransitionFunction<State<ContextT, UserDataT>, ContextT, Boolean> transitionPredicate;

    @Override
    public void run(State<ContextT, UserDataT> previousState, State<ContextT, UserDataT> nextState, ContextT context) throws Throwable {
        if (transitionPredicate().apply(previousState, nextState, context)) {
            transitionHandler().run(previousState, nextState, context);
        }
    }
}
