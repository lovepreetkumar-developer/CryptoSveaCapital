package com.svea.app.interfaces;

public interface ResponseInterface {

    default void login(String response) {
    }

    default void signUp(String response) {
    }

    default void logout(String response) {
    }

    default void errorDialog(String response) {
    }

    default void viewProfile(String response) {
    }

    default void viewWallet(String response) {
    }

    default void forgotPassword(String response) {
    }
}
