package com.platformer.handlers;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactListener implements com.badlogic.gdx.physics.box2d.ContactListener{

    private int numOfContacts;

    @Override
    public void beginContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getUserData().equals("feet")) {
            numOfContacts++;
        }
        if(fb.getUserData().equals("feet")) {
            numOfContacts++;
        }

        if(fa.getUserData().equals("player_front")) {
            System.out.printf("collision");
        }

        if(fb.getUserData().equals("player_front") && fa.getUserData().equals("obs_front")) {
            System.out.printf("collision");
        }
    }

    @Override
    public void endContact(Contact contact) {
        Fixture fa = contact.getFixtureA();
        Fixture fb = contact.getFixtureB();

        if(fa.getUserData().equals("feet")) {
            numOfContacts--;
        }
        if(fb.getUserData().equals("feet")) {
            numOfContacts--;
        }

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }

    public boolean canPlayerJump() {
        return numOfContacts > 0;
    }

}
