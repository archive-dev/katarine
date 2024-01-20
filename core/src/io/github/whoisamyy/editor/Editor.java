package io.github.whoisamyy.editor;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.ScreenUtils;
import io.github.whoisamyy.components.SpriteComponent;
import io.github.whoisamyy.components.Transform2D;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.utils.EditorObject;
import io.github.whoisamyy.utils.Utils;
import io.github.whoisamyy.utils.input.Input;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.LinkedList;

public class Editor extends ApplicationAdapter {
    public static Editor instance;

    private boolean editorMode = true, debugRender = true;

    private LinkedList<GameObject> editorObjects = new LinkedList<>();
    private LinkedList<GameObject> gameObjects = new LinkedList<>();

    private float width, height;

    protected SpriteBatch batch;
    protected World world;
    protected Box2DDebugRenderer renderer;

    protected Camera camera;

    GameObject cam;

    public static float getScreenToWorld() {
        return Utils.PPM;
    }

    public Editor() {
        this(1280, 720);
    }

    public Editor(int width, int height) {
        this.width = width / Utils.PPM;
        this.height = height / Utils.PPM;

        if (instance==null) instance = this;
    }

    @Override
    public void create() {
        Gdx.input.setInputProcessor(new Input());

        world = new World(new Vector2(0, -9.8f), false);
        batch = new SpriteBatch();
        renderer = new Box2DDebugRenderer();

        debugRender = true;
        editorMode = true;

        if (editorMode) {
            cam = instantiate(GameObject.class);
            cam.addComponent(new Transform2D());
            cam.addComponent(new EditorCamera(width, height, batch));

            GameObject exmpl = instantiate(GameObject.class);
            exmpl.addComponent(new Transform2D());
            exmpl.addComponent(new SpriteComponent(batch, new Texture(Gdx.files.internal("bucket.png")), 5, 5));
            exmpl.getComponent(Transform2D.class).setPosition(new Vector2(2, 2));

            editorObjects.forEach(GameObject::create);
        }
    }

    @Override
    public void resize(int width, int height) {
        this.width = width / Utils.PPM;
        this.height = height / Utils.PPM;
    }

    @Override
    public void render() {
        camera = cam.getComponent(EditorCamera.class).getCamera();
        ScreenUtils.clear(0,0,0,1);
        batch.begin();
        if (editorMode) {
            for (GameObject go : editorObjects) {
                go.render();
            }
        }
        batch.end();
        if (debugRender)
            renderer.render(world, camera.combined);
        if (!editorMode)
            world.step(1/240f, 6, 2);
    }

    @Override
    public void dispose() {
        if (!editorMode) {
            for (GameObject go : gameObjects) {
                go.render();
            }
        }
        batch.dispose();
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link EditorObject} annotation.
     * @param gameObjectClass
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    protected static <T extends GameObject> T instantiate(Class<T> gameObjectClass) {
        if (!gameObjectClass.isAnnotationPresent(EditorObject.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" in the editor because the class is not marked as editor object");
        try {
            Constructor<T> constructor = gameObjectClass.getDeclaredConstructor();
            boolean isAccessible = constructor.canAccess(null);
            constructor.setAccessible(true);
            T ret = constructor.newInstance();
            constructor.setAccessible(isAccessible);

            long id = Utils.getStaticFieldValue(GameObject.class, "lastId");
            Utils.setFieldValue(ret, "id", id);
            id++;
            Utils.setStaticFieldValue(GameObject.class, "lastId", id);

            ret.init();
            instance.getEditorObjects().add(ret);
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link EditorObject} annotation.
     * @param gameObjectClass
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    @SuppressWarnings("unchecked")
    protected static <T extends GameObject> T instantiate(Class<T> gameObjectClass, Object... constructorParams) {
        if (!gameObjectClass.isAnnotationPresent(EditorObject.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" in the editor because the class is not marked as editor object");
        Class<?>[] paramsTypes = new Class<?>[constructorParams.length];

        for (int i = 0; i < constructorParams.length; i++) {
            Class<?> sc = constructorParams[i].getClass().getSuperclass();
            if (Modifier.isAbstract(sc.getModifiers())) {
                paramsTypes[i] = sc;
                continue;
            }
            paramsTypes[i] = constructorParams[i].getClass();
        }

        try {
            Constructor<T> constructor = gameObjectClass.getDeclaredConstructor(paramsTypes);
            boolean isAccessible = constructor.canAccess(null);
            constructor.setAccessible(true);
            T ret = constructor.newInstance(constructorParams);
            constructor.setAccessible(isAccessible);

            long id = Utils.getStaticFieldValue(GameObject.class, "lastId");
            Utils.setFieldValue(ret, "id", id);
            id++;
            Utils.setStaticFieldValue(GameObject.class, "lastId", id);

            ret.init();
            instance.getEditorObjects().add(ret);
            return ret;
        } catch (NoSuchMethodException e) {
            return instantiate((Class<T>) gameObjectClass.getSuperclass(), constructorParams);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link EditorObject} annotation.
     * @param gameObjectClass
     * @param parent parent {@code GameObject}
     * @param constructorParams parameters of available constructor of {@code Class<T> gameObjectClass}. Order sensitive
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    protected static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent, Object... constructorParams) {
        if (!gameObjectClass.isAnnotationPresent(EditorObject.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go = instantiate(gameObjectClass, constructorParams);
        parent.addChild(go);
        return go;
    }

    /**
     * Surely it is possible to use constructors, but this method is more safe because of check for {@link EditorObject} annotation.
     * @param gameObjectClass
     * @param parent parent {@code GameObject}
     * @return instance of {@code <T extends GameObject>}
     * @param <T>
     */
    protected static <T extends GameObject> T instantiate(Class<T> gameObjectClass, GameObject parent) {
        if (!gameObjectClass.isAnnotationPresent(EditorObject.class)) throw new RuntimeException("Cannot instantiate "+gameObjectClass+" because the class is marked as not instantiatable");
        T go;
        parent.addChild(go = instantiate(gameObjectClass));
        return go;
    }

    public static Editor getInstance() {
        if (instance == null) instance = new Editor(1280, 720);

        return instance;
    }

    public boolean isEditorMode() {
        return editorMode;
    }

    public boolean isDebugRender() {
        return debugRender;
    }

    public LinkedList<GameObject> getEditorObjects() {
        return editorObjects;
    }

    public LinkedList<GameObject> getGameObjects() {
        return gameObjects;
    }

    public float getWidth() {
        return width;
    }

    public float getHeight() {
        return height;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public World getWorld() {
        return world;
    }

    public Box2DDebugRenderer getRenderer() {
        return renderer;
    }

    public Camera getCamera() {
        return camera;
    }

    protected void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
    }

    public void setDebugRender(boolean debugRender) {
        this.debugRender = debugRender;
    }
}
