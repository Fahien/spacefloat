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

/**
 * The Loading {@link TwoDimensionsScreen}
 *
 * @author Fahien
 */
public class LoadingScreen extends TwoDimensionsScreen {
	private String LOADING_TEXT = " %";

	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ImmutableArray<Entity> entities;

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
	 * Loads the {@link GameObject}s
	 */
	private void loadObjects(Engine engine) {
		GameObjectFactory factory = new GameObjectFactory();
		Array<GameObject> objects = factory.loadObjects();
		for (GameObject object : objects) {
			engine.addEntity(object);
		}
	}

	/**
	 * Load the {@link Model}s
	 */
	private void loadModels(Engine engine) {
		Family family = Family.all(GraphicComponent.class).get();
		entities = engine.getEntitiesFor(family);
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if(graphic.getInstance() == null) {
				assetManager.load(GraphicComponent.MODELS_DIR + graphic.getName(), Model.class);
			}
		}
	}

	@Override
	public void update(float delta) {
		assetManager.update();
		progress = assetManager.getProgress();
		if (progress != 1.0f) {
			loadingActor.setText((int) (progress * 100) + LOADING_TEXT);
		} else {
			for (Entity entity : entities) {
				GraphicComponent graphic = graphicMapper.get(entity);
				if (graphic.getInstance() == null) {
					Model model = assetManager.get(GraphicComponent.MODELS_DIR + graphic.getName(), Model.class);
					ModelInstance instance = new ModelInstance(model);
					graphic.setInstance(instance);
				}
			}
			getGame().setScreen(ScreenEnumerator.MAIN);
		}
	}
}
