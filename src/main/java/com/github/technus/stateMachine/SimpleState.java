package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.List;

@Builder
@Getter
public class SimpleState<ContextT, UserDataT> implements State<ContextT, UserDataT> {
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onEntryCallbacks;
    @Singular
    private final List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onLeaveCallbacks;
    /**
     * Additional metadata
     */
    private final UserDataT                                                           userData;
}
