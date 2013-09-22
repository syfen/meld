package com.syfen.zookeeper.exceptions;

/**
 * User: ToneD
 * Created: 21/09/13 9:30 PM
 */
public class PropertyNotFoundException extends Exception {

    private String propertyName;

    public PropertyNotFoundException(String propName) {

        propertyName = propName;
    }

    public String getMessage() {

        return "Property '" + propertyName + "' not found.";
    }
}
