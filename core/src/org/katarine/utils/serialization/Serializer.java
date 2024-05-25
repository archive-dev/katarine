package org.katarine.utils.serialization;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class Serializer<T extends Serializable> {
    public File toFile(ObjectRepresentation<T> representation, File file) throws IOException {
        if (!file.exists()) throw new IOException("File does not exists: " + file.getPath());

        try(FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(toString(representation).getBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return file;
    }

    /**
     * Returns a string representation of the given ObjectRepresentation.
     *
     * @param representation the ObjectRepresentation to convert to a string
     * @return a string representation of the ObjectRepresentation
     */
    public abstract String toString(ObjectRepresentation<T> representation);

    /**
     * Returns a string representation of the given ObjectRepresentation.
     *
     * @param representation the ObjectRepresentation to convert to a string
     * @return a string representation of the ObjectRepresentation
     *
     * @see {@link Serializer#toString(ObjectRepresentation)}
     */

    public String serialize(ObjectRepresentation<T> representation) {
        return toString(representation);
    }

    public abstract ObjectRepresentation<T> fromString(String string);

    public ObjectRepresentation<T> deserialize(String string) {
        return fromString(string);
    }
}
