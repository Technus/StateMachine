package com.github.technus.stateMachine;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class SimpleStateMachine<ContextT, UserDataT, KeyT> implements StateMachine<ContextT, UserDataT, KeyT> {
    private final ContextT                                    context;
    private final StateMachineDefinition<ContextT, UserDataT, KeyT> definition;
    @Builder.Default
    @Setter(AccessLevel.PROTECTED)
    private       State<ContextT, UserDataT>                  state = State.undefinedState();
    /**
     * Additional metadata
     */
    private final UserDataT                                   userData;

    @Override
    public void uncheckedChangeState(State<ContextT, UserDataT> nextState) {
        StateMachine.super.uncheckedChangeState(nextState);
        state(nextState == null ? State.undefinedState() : nextState);
    }
}
