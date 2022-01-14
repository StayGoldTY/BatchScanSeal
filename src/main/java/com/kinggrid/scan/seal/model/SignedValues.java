package com.kinggrid.scan.seal.model;

import java.io.Serializable;
import java.util.List;

/**
 * @author xionglei
 * @version 1.0.0
 * 2021年09月11日 16:07
 */
public class SignedValues implements Serializable {

    private List<SignedValueResult> signedValues;

    public List<SignedValueResult> getSignedValues() {
        return signedValues;
    }

    public void setSignedValues(List<SignedValueResult> signedValues) {
        this.signedValues = signedValues;
    }
}
