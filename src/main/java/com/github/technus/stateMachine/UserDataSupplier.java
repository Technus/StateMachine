package com.github.technus.stateMachine;

public interface UserDataSupplier<UserDataT> {
    /**
     * User Data context
     *
     * @return the data context
     */
    UserDataT userData();
}
