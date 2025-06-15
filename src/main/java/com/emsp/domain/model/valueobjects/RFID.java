package com.emsp.domain.model.valueobjects;

import lombok.Getter;

@Getter
public class RFID {
    private final String uid;
    private final String visibleNumber;
    
    public RFID(String uid, String visibleNumber) {
        if (uid == null || uid.trim().isEmpty()) {
            throw new IllegalArgumentException("UID cannot be null or empty");
        }
        if (visibleNumber == null || visibleNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Visible number cannot be null or empty");
        }
        this.uid = uid;
        this.visibleNumber = visibleNumber;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RFID rfid = (RFID) o;
        return uid.equals(rfid.uid);
    }
    
    @Override
    public int hashCode() {
        return uid.hashCode();
    }
}