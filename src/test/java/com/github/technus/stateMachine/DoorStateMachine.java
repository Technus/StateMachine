package com.github.technus.stateMachine;

import lombok.SneakyThrows;

import java.util.concurrent.atomic.AtomicBoolean;

public class DoorStateMachine {
    private static final State<DoorStateMachine, String> OPENED =
            SimpleState.<DoorStateMachine, String>builder()
                    .userData("opened")
                    .onEntryCallback((previousState, nextState, context) -> System.out.println("Door opened"))
                    .onLeaveCallback((previousState, nextState, context) -> System.out.println("Door not opened"))
                    .build();
    private static final State<DoorStateMachine, String> CLOSED =
            SimpleState.<DoorStateMachine, String>builder()
                    .userData("closed")
                    .onEntryCallback((previousState, nextState, context) -> System.out.println("Door closed"))
                    .onLeaveCallback((previousState, nextState, context) -> System.out.println("Door not closed"))
                    .build();

    private static final StateTransition<State<DoorStateMachine, String>, DoorStateMachine, String> CLOSING =
            CheckedStateTransition.<DoorStateMachine, String>builder()
                    .userData("close")
                    .beginCallback((previousState, nextState, context) -> System.out.println("Started closing"))
                    .finishCallback((previousState, nextState, context) -> System.out.println("Finished closing"))
                    .transitionPredicate((previousState, nextState, context) ->
                            previousState == OPENED && context.shouldToggleDoorState.getAndSet(false) ? CLOSED : null)
                    .build();

    private static final StateTransition<State<DoorStateMachine, String>, DoorStateMachine, String> OPENING =
            CheckedStateTransition.<DoorStateMachine, String>builder()
                    .userData("open")
                    .beginCallback((previousState, nextState, context) -> System.out.println("Started opening"))
                    .finishCallback((previousState, nextState, context) -> System.out.println("Finished opening"))
                    .transitionPredicate((previousState, nextState, context) ->
                            previousState == CLOSED && context.shouldToggleDoorState.getAndSet(false) ? OPENED : null)
                    .build();

    private static final StateMachineDefinition<DoorStateMachine, String, String> DEFINITION =
            SimpleStateMachineDefinition.<DoorStateMachine, String, String>builder()
                    .keyExtractor(Object::toString)
                    .registerState(OPENED)
                    .registerState(CLOSED)
                    .registerTransition(CLOSING)
                    .registerTransition(OPENING)
                    .build();

    private final StateMachine<DoorStateMachine, String, String> stateMachine =
            SimpleStateMachine.<DoorStateMachine, String, String>builder()
                    .context(this)//bind this machine to data
                    .definition(DEFINITION)//set the behaviour
                    .build();

    private final AtomicBoolean shouldToggleDoorState = new AtomicBoolean();

    public DoorStateMachine(String state) {
        State<DoorStateMachine, String> doorState = stateMachine.definition().states().getOrDefault(state, State.undefinedState());

        //Force some initial state without event handling
        stateMachine.state(doorState);

        //Force some initial state with event handling
        //stateMachine.forceStateChange(State.undefinedState(),stateMachine.definition().states().get(state),this);

        //Force some transition to initial state with event handling (skips predicate)
        //if (doorState == OPENED)
        //    stateMachine.forceStateTransition(State.undefinedState(), OPENED, this, OPENING);
        //else if (doorState == CLOSED)
        //    stateMachine.forceStateTransition(State.undefinedState(), CLOSED, this, OPENING);
    }

    @SneakyThrows
    @SuppressWarnings("BusyWait")
    public static void main(String[] args) {
        DoorStateMachine doorStateMachine = new DoorStateMachine("opened");

        //Just to "send events to door"
        new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                doorStateMachine.shouldToggleDoorState.set(true);
                System.out.println("The door Should move!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }).start();

        while (!Thread.currentThread().isInterrupted()) {
            doorStateMachine.stateMachine.tryStateTransition();
            Thread.sleep(10);
        }
    }
}
