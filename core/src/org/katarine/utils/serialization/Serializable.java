package org.katarine.utils.serialization;

import java.util.HashMap;

public interface Serializable {
    HashMap<String, Object> getFields();
    Object deserialize(HashMap<String, Object> fields);
    /*
     The result of deserialize(getFields()) must return an object which field values are equal to values this object.
     */
}
