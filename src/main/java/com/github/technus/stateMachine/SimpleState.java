package com.github.technus.stateMachine;

import lombok.Builder;
import lombok.Getter;
import lombok.Singular;

import java.util.ArrayList;
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

    public SimpleState(List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onEntryCallbacks,
                       List<StateTransitionCallback<State<ContextT, UserDataT>, ContextT>> onLeaveCallbacks,
                       UserDataT userData) {
        this.onEntryCallbacks = new ArrayList<>(onEntryCallbacks);
        this.onLeaveCallbacks = new ArrayList<>(onLeaveCallbacks);
        this.userData = userData;
    }
}
