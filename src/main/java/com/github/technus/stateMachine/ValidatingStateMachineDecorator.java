package com.github.technus.stateMachine;

import lombok.*;
import lombok.experimental.Delegate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValidatingStateMachineDecorator<ContextT, UserDataT, KeyT> implements StateMachine<ContextT, UserDataT, KeyT> {
    @Delegate(excludes = ExcludedMethods.class)
    private final StateMachine<ContextT, UserDataT, KeyT> stateMachine;

    public static <ContextT, UserDataT, KeyT> StateMachine<ContextT, UserDataT, KeyT> decorate(StateMachine<ContextT, UserDataT, KeyT> stateMachine) {
        return new ValidatingStateMachineDecorator<>(stateMachine);
    }

    private abstract class ExcludedMethods {
        public abstract void forceStateChange(State<ContextT, UserDataT> previousState,
                                              State<ContextT, UserDataT> nextState,
                                              ContextT context);

        public abstract void forceStateTransition(State<ContextT, UserDataT> previousState,
                                                  State<ContextT, UserDataT> nextState,
                                                  ContextT context,
                                                  StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition);
    }

    @Override
    public void forceStateChange(State<ContextT, UserDataT> previousState,
                                 State<ContextT, UserDataT> nextState,
                                 ContextT context) {
        final KeyT key = definition().keyExtractor().apply(nextState.userData());
        final State<ContextT, UserDataT> value = definition().states().get(key);
        if (value == null)
            throw new IllegalArgumentException("State was not registered: " + nextState + " " + key);
        if (value != nextState)
            throw new IllegalArgumentException("State definition does not match: " + nextState + " " + key);
        StateMachine.super.forceStateChange(previousState, nextState, context);
    }

    @Override
    public void forceStateTransition(State<ContextT, UserDataT> previousState,
                                     State<ContextT, UserDataT> nextState,
                                     ContextT context,
                                     StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        final KeyT key = definition().keyExtractor().apply(transition.userData());
        final StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> value = definition().transitions().get(key);
        if (value == null)
            throw new IllegalArgumentException("Transition was not registered: " + transition + " " + key);
        if (value != transition)
            throw new IllegalArgumentException("Transition definition does not match: " + transition + " " + key);
        StateMachine.super.forceStateTransition(previousState, nextState, context, transition);
    }
}
