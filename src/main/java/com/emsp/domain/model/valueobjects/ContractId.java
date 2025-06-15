package com.emsp.domain.model.valueobjects;


import com.emsp.infrastructure.util.EMAIDValidator;
import lombok.Getter;

@Getter
public class ContractId {
    private final String value;
    
    public ContractId(String value) {
        if (!EMAIDValidator.isValid(value)) {
            throw new IllegalArgumentException("Invalid EMAID format for contract ID");
        }
        this.value = value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContractId that = (ContractId) o;
        return value.equals(that.value);
    }
    
    @Override
    public int hashCode() {
        return value.hashCode();
    }
}