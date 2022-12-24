package com.github.technus.stateMachine;

import lombok.*;
import lombok.experimental.Delegate;

@Getter
@AllArgsConstructor(access=AccessLevel.PRIVATE)
public class ValidatingStateMachineDecorator<ContextT, UserDataT,KeyT> implements StateMachine<ContextT, UserDataT,KeyT>{
    @Delegate(excludes = ExcludedMethods.class)
    private final StateMachine<ContextT, UserDataT,KeyT> stateMachine;

    public static <ContextT, UserDataT, KeyT> StateMachine<ContextT, UserDataT,KeyT> decorate(StateMachine<ContextT, UserDataT,KeyT> stateMachine){
        return new ValidatingStateMachineDecorator<>(stateMachine);
    }

    private abstract class ExcludedMethods {
        public abstract void uncheckedChangeState(State<ContextT, UserDataT> nextState);
        public abstract void tryChangeState(State<ContextT, UserDataT> desiredState, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition);
    }

    @Override
    public void uncheckedChangeState(State<ContextT, UserDataT> nextState) {
        val key = definition().keyExtractor().apply(nextState.userData());
        val value = definition().states().get(key);
        if (value == null)
            throw new IllegalArgumentException("State was not registered: "+nextState+" "+key);
        if(value!=nextState)
            throw new IllegalArgumentException("State definition does not match: "+nextState+" "+key);

        stateMachine.uncheckedChangeState(nextState);
    }

    @Override
    public void tryChangeState(State<ContextT, UserDataT> desiredState, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        val key = definition().keyExtractor().apply(transition.userData());
        val value = definition().transitions().get(key);
        if (value == null)
            throw new IllegalArgumentException("Transition was not registered: "+transition+" "+key);
        if(value!=transition)
            throw new IllegalArgumentException("Transition definition does not match: "+transition+" "+key);

        stateMachine.tryChangeState(desiredState, transition);
    }
}
