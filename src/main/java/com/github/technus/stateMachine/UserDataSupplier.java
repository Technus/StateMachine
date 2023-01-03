package com.github.technus.stateMachine;

/**
 * Common interface for user data holders
 * @param <UserDataT> of the thing
 */
public interface UserDataSupplier<UserDataT> {
    /**
     * User Data context
     *
     * @return the data context
     */
    UserDataT userData();
}
