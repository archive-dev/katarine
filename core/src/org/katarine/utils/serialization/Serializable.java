package org.katarine.utils.serialization;

import org.katarine.annotations.RequiresDefaultConstructor;

import java.util.HashMap;

@RequiresDefaultConstructor
public interface Serializable {
    HashMap<String, Object> getFields();
    void fillFields(HashMap<String, Object> fields);
}
