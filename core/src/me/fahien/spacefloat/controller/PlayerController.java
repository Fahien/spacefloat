package me.fahien.spacefloat.controller;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.EntitySystem;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.InputMultiplexer;

import me.fahien.spacefloat.component.PlayerComponent;
import me.fahien.spacefloat.game.SpaceFloatGame;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Player {@link EntitySystem} Base Class
 *
 * @author Fahien
 */
public abstract class PlayerController extends EntitySystem {

	private Entity player;

	private InputMultiplexer inputMultiplexer;

	public PlayerController(int priority) {
		super(priority);
	}

	/**
	 * Returns the player
	 */
	public Entity getPlayer() {
		return player;
	}

	/**
	 * Returns the {@link InputMultiplexer}
	 */
	public InputMultiplexer getInputMultiplexer() {
		return inputMultiplexer;
	}

	/**
	 * Sets the {@link InputMultiplexer}
	 */
	public void setInputMultiplexer(InputMultiplexer inputMultiplexer) {
		this.inputMultiplexer = inputMultiplexer;
	}

	@Override
	public void addedToEngine(Engine engine) {
		super.addedToEngine(engine);

		// Get the player
		Family family = Family.all(PlayerComponent.class).get();
		ImmutableArray<Entity> entities = engine.getEntitiesFor(family);
		if (entities.size() > 0) {
			player = entities.get(0);
		} else {
			logger.error("Could not find the player");
		}
	}
}
