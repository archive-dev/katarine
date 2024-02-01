package io.github.whoisamyy.components;

import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.Array;
import io.github.whoisamyy.editor.Editor;
import io.github.whoisamyy.logging.LogLevel;
import io.github.whoisamyy.test.Game;

public class TriggerBox extends Component {
    private Body body;
    private Fixture fixture;

    protected boolean isTouched;

    public TriggerBox(Shape shape) {
        World world;
        if (Game.getInstance()!=null) {
            world = Game.getInstance().getWorld();
        } else if (Editor.getInstance()!=null) {
            world = Editor.getInstance().getWorld();
        } else throw new IllegalStateException("Game or Editor not initialized");

        BodyDef bd = new BodyDef();
        bd.type = BodyDef.BodyType.KinematicBody;
        bd.bullet = true;

        FixtureDef fd = new FixtureDef();
        fd.isSensor = true;
        Shape shape1 = shape;
        fd.shape = shape1;

        this.body = world.createBody(bd);
        this.fixture = this.body.createFixture(fd);
    }

    public TriggerBox(Fixture fixture) {
        this.fixture = fixture;
    }

    @Override
    public void start() {
        logger.setLogLevel(LogLevel.DEBUG);

        logger.debug(gameObject.getId() +": "+transform.pos);
    }

    @Override
    public void update() {
        this.body.setTransform(transform.pos, 0);

        World world;
        if (Game.getInstance()!=null) {
            world = Game.getInstance().getWorld();
        } else if (Editor.getInstance()!=null) {
            world = Editor.getInstance().getWorld();
        } else throw new IllegalStateException("Game or Editor not initialized");

        Array<Contact> contacts = world.getContactList();

        isTouched = false;

        for (Contact contact : contacts) {
            if (contact.getFixtureA()==this.fixture && contact.getFixtureB()==Editor.getInstance().getCursorBox().getFixture()) {
                isTouched = true;
                break;
            } else if (contact.getFixtureB()==this.fixture && contact.getFixtureA()==Editor.getInstance().getCursorBox().getFixture()) {
                isTouched = true;
                break;
            }
        }
    }

    public boolean isTouched() {
        return isTouched;
    }

    public Body getBody() {
        return body;
    }

    public Fixture getFixture() {
        return fixture;
    }
}
