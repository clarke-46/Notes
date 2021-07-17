package com.example.notes;

public interface Keystore {
    boolean notHasPincode();

    void hasPincode();

    boolean checkPincode(String pin);

    void saveNewPincode(String pin);
}