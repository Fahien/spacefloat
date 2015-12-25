package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import me.fahien.spacefloat.actor.FontActor;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.GameObjectFactory;

import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Loading {@link StagedScreen}
 *
 * @author Fahien
 */
public class LoadingScreen extends StagedScreen {
	private String LOADING_TEXT = " %";

	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ImmutableArray<Entity> entities;

	// Temp variables
	private FontActor loadingActor;
	private AssetManager assetManager;
	private float progress;

	@Override
	public void populate(Stage stage) {
		loadingActor = new FontActor(getFont(), progress + LOADING_TEXT);
		loadingActor.setPosition(SpaceFloatScreen.WIDTH / 2, SpaceFloatScreen.HEIGHT / 2);
		loadingActor.setHalign(FontActor.Halign.CENTER);
		stage.addActor(loadingActor);
		assetManager = getAssetManager();
		loadObjects(getEngine());
		loadModels(getEngine());
	}

	/**
	 * Sets the {@link AssetManager}
	 */
	public void setAssetManager(AssetManager assetManager) {
		super.setAssetManager(assetManager);
		this.assetManager = assetManager;
	}

	/**
	 * Loads the {@link GameObject}s
	 */
	protected void loadObjects(Engine engine) {
		GameObjectFactory factory = new GameObjectFactory();
		Array<GameObject> objects = factory.loadObjects();
		for (GameObject object : objects) {
			engine.addEntity(object);
		}
	}

	/**
	 * Loads the {@link Model}s
	 */
	protected void loadModels(Engine engine) {
		Family family = Family.all(GraphicComponent.class).get();
		entities = engine.getEntitiesFor(family);
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if(graphic.getInstance() == null) {
				String name = graphic.getName();
				if (name != null) {
					assetManager.load(GraphicComponent.MODELS_DIR + name, Model.class);
				} else {
					logger.error("Error loading models: a graphic has no name");
				}
			}
		}
	}

	@Override
	public void prerender(float delta) {
		assetManager.update();
		progress = assetManager.getProgress();
		if (progress != 1.0f) {
			loadingActor.setText((int) (progress * 100) + LOADING_TEXT);
		} else {
			injectInstances(entities);
			// Change screen
			getGame().setScreen(ScreenEnumerator.MAIN);
		}
	}

	/**
	 * Injects instances in every {@link GraphicComponent}
	 */
	protected void injectInstances(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if (graphic.getInstance() == null) {
				Model model = assetManager.get(GraphicComponent.MODELS_DIR + graphic.getName(), Model.class);
				ModelInstance instance = new ModelInstance(model);
				graphic.setInstance(instance);
			}
		}
	}
}
