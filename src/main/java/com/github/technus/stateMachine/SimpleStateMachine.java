package com.github.technus.stateMachine;

import lombok.*;

@Builder
@Getter
public class SimpleStateMachine<ContextT, UserDataT, KeyT> implements StateMachine<ContextT, UserDataT, KeyT> {
    private final ContextT context;
    private final StateMachineDefinition<ContextT, UserDataT, KeyT> definition;
    @Builder.Default
    @Setter(AccessLevel.PUBLIC)
    private State<ContextT, UserDataT> state = State.undefinedState();
    /**
     * Additional metadata
     */
    private final UserDataT userData;
}
