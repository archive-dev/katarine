package io.github.whoisamyy.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class CollisionsListener implements ContactListener { //TODO: TODO
    @Override
    public void beginContact(Contact contact) {
        //RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        //RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        //if (rb==null || rb2 == null) return;
        //if (rb.getPreviousContact()==rb2 && rb2.getPreviousContact()==rb) return;

        //rb.onContactStart(Game.getRigidBody2DByFixture(contact.getFixtureB()));
        //rb.setContacting(true);
        //rb2.onContactStart(Game.getRigidBody2DByFixture(contact.getFixtureA()));
        //rb2.setContacting(true);

        //rb.setPreviousContact(rb2);
        //rb2.setPreviousContact(rb);
    }

    @Override
    public void endContact(Contact contact) {
        //RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        //RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        //if (rb==null || rb2 == null) return;

        //rb.onContactEnd(Game.getRigidBody2DByFixture(contact.getFixtureB()));
        //rb.setContacting(false);
        //rb2.onContactEnd(Game.getRigidBody2DByFixture(contact.getFixtureA()));
        //rb2.setContacting(false);

        //rb.setPreviousContact(rb2);
        //rb2.setPreviousContact(rb);
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        //RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        //RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        //if (rb==null || rb2 == null) return;

        //rb.onPreResolve(Game.getRigidBody2DByFixture(contact.getFixtureB()), oldManifold);
        //rb2.onPreResolve(Game.getRigidBody2DByFixture(contact.getFixtureA()), oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        //RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        //RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        //if (rb==null || rb2 == null) return;

        //rb.onPostResolve(Game.getRigidBody2DByFixture(contact.getFixtureB()), impulse);
        //rb2.onPostResolve(Game.getRigidBody2DByFixture(contact.getFixtureA()), impulse);
    }
}
