package org.katarine.listeners;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import org.katarine.components.phyiscs.RigidBody2D;

public class CollisionsListener implements ContactListener {
    @Override
    public void beginContact(Contact contact) {
        ((RigidBody2D) contact.getFixtureA().getUserData()).onCollisionEnter(((RigidBody2D) contact.getFixtureB().getUserData()));
        ((RigidBody2D) contact.getFixtureB().getUserData()).onCollisionEnter(((RigidBody2D) contact.getFixtureA().getUserData()));
    }

    @Override
    public void endContact(Contact contact) {
        ((RigidBody2D) contact.getFixtureA().getUserData()).onCollisionExit(((RigidBody2D) contact.getFixtureB().getUserData()));
        ((RigidBody2D) contact.getFixtureB().getUserData()).onCollisionExit(((RigidBody2D) contact.getFixtureA().getUserData()));
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {
        ((RigidBody2D) contact.getFixtureA().getUserData()).onCollisionPreSolve(((RigidBody2D) contact.getFixtureB().getUserData()), oldManifold);
        ((RigidBody2D) contact.getFixtureB().getUserData()).onCollisionPreSolve(((RigidBody2D) contact.getFixtureA().getUserData()), oldManifold);
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {
        ((RigidBody2D) contact.getFixtureA().getUserData()).onCollisionPostSolve(((RigidBody2D) contact.getFixtureB().getUserData()), impulse);
        ((RigidBody2D) contact.getFixtureB().getUserData()).onCollisionPostSolve(((RigidBody2D) contact.getFixtureA().getUserData()), impulse);
    }
}
