package org.katarine.utils.serialization;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

public final class Assets {
    private Assets() {}

    public static FileHandle save(String path, byte[] data) {
        FileHandle fh = Gdx.files.local("assets/"+path);
        fh.writeBytes(data, false);

        return fh;
    }

    public static FileHandle save(String path, String data) {
        FileHandle fh = Gdx.files.local("assets/"+path);
        fh.writeString(data, false);

        return fh;
    }

    public static FileHandle save(String path) {
        return save(path, new byte[0]);
    }

    public static FileHandle get(String path) {
        return Gdx.files.local("assets/"+path);
    }

    public static String readString(String path) {
        return Gdx.files.local(path).readString();
    }

    public static byte[] readBytes(String path) {
        return Gdx.files.local(path).readBytes();
    }
}
