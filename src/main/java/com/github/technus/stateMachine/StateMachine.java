package com.github.technus.stateMachine;

import lombok.SneakyThrows;
import lombok.val;

/**
 * The state machine runner
 * @param <ContextT> of the machine
 * @param <UserDataT> of the machine
 * @param <KeyT> of the states and transitions
 */
public interface StateMachine<ContextT, UserDataT,KeyT> extends UserDataSupplier<UserDataT> {
    /**
     * Does modify the machine internal state, used to call all callbacks with same args.
     * Forces the machine to transition to next state, WITHOUT using any transition!.
     * Calls:
     * {@link StateMachineDefinition#onPreChange(State, State, Object)}
     * {@link State#onLeave(State, Object)}
     * {@link StateMachineDefinition#onInterChange(State, State, Object)}
     * {@link StateMachine#state(State)} to next state
     * {@link State#onEntry(State, Object)}
     * {@link StateMachineDefinition#onPostChange(State, State, Object)}
     * @param previousState of the machine
     * @param nextState of the machine
     */
    default void forceStateChange(State<ContextT,UserDataT> previousState,
                                  State<ContextT, UserDataT> nextState,
                                  ContextT context) {
        definition().onPreChange(previousState, nextState, context);
        previousState.onLeave(nextState, context);
        //state(State.undefinedState());
        definition().onInterChange(previousState, nextState, context);
        state(nextState);
        nextState.onEntry(previousState, context);
        definition().onPostChange(previousState, nextState, context);
    }

    /**
     * Does modify the machine internal state, used to call all callbacks with same args.
     * Forces the machine to transition to next state, with a transition!.
     * Calls:
     * {@link StateTransition#onBegin(Object, Object, Object)}
     * {@link StateMachine#forceStateChange(State, State, Object)}
     * {@link StateTransition#onFinish(Object, Object, Object)}
     * @param previousState of the machine
     * @param nextState of the machine
     * @param context of the machine
     * @param transition of the machine
     */
    default void forceStateTransition(State<ContextT,UserDataT> previousState,
                                      State<ContextT, UserDataT> nextState,
                                      ContextT context,
                                      StateTransition<State<ContextT,UserDataT>,ContextT,UserDataT> transition) {
        transition.onBegin(previousState, nextState, context);
        forceStateChange(previousState,nextState,context);
        transition.onFinish(previousState, nextState, context);
    }

    /**
     * Will call {@link StateMachine#tryStateTransition(State, StateTransition)} for each registered transition
     * Equivalent to calling {@link StateMachine#tryStateTransition(State)} with {@link State#undefinedState()}
     */
    default void tryStateTransition() {
        tryStateTransition(State.undefinedState());
    }

    /**
     * Will call {@link StateMachine#tryStateTransition(State, StateTransition)} for each registered transition
     * One use case is to call it with {@link State#undefinedState()} as argument to find first matching transition.
     * @param desiredState of the machine
     */
    default void tryStateTransition(State<ContextT, UserDataT> desiredState) {
        for (final StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition : definition().transitions().values()) {
            tryStateTransition(desiredState, transition);
        }
    }

    /**
     * Will call {@link StateMachine#tryStateTransition(State, StateTransition)} for the selected transition,
     * using {@link  State#undefinedState()} as next desired state.
     * @param transition to attempt
     */
    default void tryStateTransition(StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        tryStateTransition(State.undefinedState(), transition);
    }

    /**
     * The actual state change logic using transition.
     * If predicate {@link StateTransition#apply(Object, Object, Object)} does not match any state (returned null) then no other logic is run.
     * If predicate returns {@link State#undefinedState()} then the behaviour is defined by the implementation.
     * (Usually in such case machine should transition to undefined state and halt any operation?)
     * @param desiredState to transition to
     * @param transition to attempt
     */
    @SneakyThrows
    default void tryStateTransition(State<ContextT, UserDataT> desiredState, StateTransition<State<ContextT, UserDataT>, ContextT, UserDataT> transition) {
        final State<ContextT, UserDataT> state = state();
        final ContextT context = context();
        final State<ContextT, UserDataT> nextState = transition.apply(state, desiredState, context);

        if (nextState == null) return;

        if(nextState == State.undefinedState()){
            System.err.println("Transitioned to UNDEFINED state");
        }

        forceStateTransition(state,nextState,context,transition);
    }

    /**
     * Current state, updated internally by {@link StateMachine#forceStateChange(State, State, Object)}
     *
     * @return current/previous state
     */
    State<ContextT, UserDataT> state();

    /**
     * {@link StateMachine#state()} setter used internally by {@link StateMachine#forceStateChange(State, State, Object)}
     * @param nextState to set the machine into
     * @return itself
     */
    StateMachine<ContextT, UserDataT,KeyT> state(State<ContextT,UserDataT> nextState);

    /**
     * Data context
     *
     * @return the data context
     */
    ContextT context();

    /**
     * The whole machine logic to obey.
     * @return the machine definition
     */
    StateMachineDefinition<ContextT, UserDataT,KeyT> definition();
}
