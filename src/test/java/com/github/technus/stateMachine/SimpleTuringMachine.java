package com.github.technus.stateMachine;

import lombok.*;

@Builder
public class SimpleTuringMachine {
    @Builder.Default
    protected int position=0;
    protected char[] tape;
    protected final StateMachine<SimpleTuringMachine,String,String> stateMachine = SimpleStateMachine.<SimpleTuringMachine, String, String>builder()
            .definition(stateMachineDefinition)
            .context(this)
            .state(q0)
            .build();

    protected static State<SimpleTuringMachine, String> state(String name) {
        return SimpleState.<SimpleTuringMachine, String>builder()
                .userData(name)
                .build();
    }
    protected static StateTransition<State<SimpleTuringMachine, String>, SimpleTuringMachine, String> transition(String name,
                                                                                                                     State<SimpleTuringMachine, String> fromState,
                                                                                                                     State<SimpleTuringMachine, String> toState,
                                                                                                                     int movement,
                                                                                                                     char readValue,
                                                                                                                     char writeValue) {
        return CheckedStateTransition.<SimpleTuringMachine, String>builder()
                .userData(name)
                .transitionPredicate((previousState, nextState, context) ->
                        previousState == fromState && readValue == context.tape[context.position] ? toState : null)
                .beginCallback((previousState, nextState, context) -> {
                    context.tape[context.position] = writeValue;
                    context.position += movement;
                })
                .build();
    }

    public static final State<SimpleTuringMachine, String> q0 = state("q0");
    public static final State<SimpleTuringMachine, String> q1 = state("q1");
    public static final State<SimpleTuringMachine, String> q2 = state("q2");
    public static final State<SimpleTuringMachine, String> f = state("f");
    public static final SimpleStateMachineDefinition<SimpleTuringMachine,String,String> stateMachineDefinition = SimpleStateMachineDefinition.<SimpleTuringMachine, String, String>builder()
            .keyExtractor(str -> str)
            .registerState(q0)
            .registerState(q1)
            .registerState(q2)
            .registerState(f)
            .onPostChangeCallback((previousState, nextState, context) -> {
                System.out.println(new String(context.tape));
                for (int i = 0; i < context.position; i++) {
                    System.out.print(' ');
                }
                System.out.println('^');
            })
            .registerTransition(transition("seekToStart",   q0, q0,  1, ' ', ' '))
            .registerTransition(transition("foundStart0",   q0, q1,  1, '0', '0'))
            .registerTransition(transition("foundStart1",   q0, q1,  1, '1', '1'))
            .registerTransition(transition("seekToEnd0",    q1, q1,  1, '0', '0'))
            .registerTransition(transition("seekToEnd1",    q1, q1,  1, '1', '1'))
            .registerTransition(transition("endFound",      q1, q2, -1, ' ', ' '))
            .registerTransition(transition("cascadeOnes",   q2, q2, -1, '1', '0'))
            .registerTransition(transition("incrementZero", q2, f , -1, '0', '1'))
            .registerTransition(transition("incrementEnd",  q2, f , -1, ' ', '1'))
            .build();

    public void run(){
        while (!stateMachine.state().equals(f)) {
            stateMachine.tryStateTransition();
        }
    }

    public static void main(String[] args) {
        SimpleTuringMachine.builder().tape("   010011111   ".toCharArray()).build().run();
    }
}
