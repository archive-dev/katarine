package io.github.whoisamyy.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import io.github.whoisamyy.objects.GameObject;
import io.github.whoisamyy.objects.RigidBody2D;
import io.github.whoisamyy.test.Game;

public class CollisionsListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        if (rb==null || rb2 == null) return;

        rb.onContactStart(Game.getRigidBody2DByFixture(contact.getFixtureB()));
        rb2.onContactStart(Game.getRigidBody2DByFixture(contact.getFixtureA()));
    }

    @Override
    public void endContact(Contact contact) {
        RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        if (rb==null || rb2 == null) return;

        rb.onContactEnd(Game.getRigidBody2DByFixture(contact.getFixtureB()));
        rb2.onContactEnd(Game.getRigidBody2DByFixture(contact.getFixtureA()));
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        if (rb==null || rb2 == null) return;

        rb.onPreResolve(Game.getRigidBody2DByFixture(contact.getFixtureB()), oldManifold);
        rb2.onPreResolve(Game.getRigidBody2DByFixture(contact.getFixtureA()), oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        RigidBody2D rb = Game.getRigidBody2DByFixture(contact.getFixtureA());
        RigidBody2D rb2 = Game.getRigidBody2DByFixture(contact.getFixtureB());

        if (rb==null || rb2 == null) return;

        rb.onPostResolve(Game.getRigidBody2DByFixture(contact.getFixtureB()), impulse);
        rb2.onPostResolve(Game.getRigidBody2DByFixture(contact.getFixtureA()), impulse);
    }
}
