package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;

import me.fahien.spacefloat.actor.FontActor;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.GameObjectService;
import me.fahien.spacefloat.game.SpaceFloat;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Loading {@link StagedScreen}
 *
 * @author Fahien
 */
public class LoadingScreen extends StagedScreen {
	private String LOADING_TEXT = " %";

	private ComponentMapper<GraphicComponent> graphicMapper = ComponentMapper.getFor(GraphicComponent.class);
	private ComponentMapper<ReactorComponent> reactorMapper = ComponentMapper.getFor(ReactorComponent.class);
	private ImmutableArray<Entity> entities;

	private LocalFileHandleResolver localResolver;
	private InternalFileHandleResolver internalResolver;

	// Loop variables
	private FontActor m_loadingActor;
	private AssetManager m_assetManager;
	private float m_progress;

	@Override
	public void populate(Stage stage) {
		m_loadingActor = new FontActor(getFont(), m_progress + LOADING_TEXT);
		m_loadingActor.setPosition(SpaceFloatScreen.WIDTH / 2, SpaceFloatScreen.HEIGHT / 2);
		m_loadingActor.setHalign(FontActor.Halign.CENTER);
		stage.addActor(m_loadingActor);
		m_assetManager = getAssetManager();
		localResolver = new LocalFileHandleResolver();
		internalResolver = new InternalFileHandleResolver();
		loadObjects(getEngine());
		loadModels(getEngine());
	}

	/**
	 * Sets the {@link AssetManager}
	 */
	public void setAssetManager(AssetManager assetManager) {
		super.setAssetManager(assetManager);
		this.m_assetManager = assetManager;
	}

	/**
	 * Loads the {@link GameObject}s
	 */
	protected void loadObjects(Engine engine) {
		GameObjectService factory = new GameObjectService();
		Array<GameObject> objects = factory.loadObjects();
		for (GameObject object : objects) {
			engine.addEntity(object);
		}
	}

	/**
	 * Loads the {@link Model}s
	 */
	protected void loadModels(Engine engine) {
		Family family = all(GraphicComponent.class).get();
		entities = engine.getEntitiesFor(family);
		G3dModelLoader loader = new G3dModelLoader(new JsonReader(), localResolver);
		m_assetManager.setLoader(Model.class, loader);
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if(graphic.getInstance() == null) {
				String name = graphic.getName();
				if (name != null) {
					m_assetManager.load(GraphicComponent.MODELS_DIR + name, Model.class);
				} else {
					logger.error("Error loading models: a graphic has no name");
				}
			}
		}
	}

	/**
	 * Loads the {@link ParticleEffect}s
	 *
	 * @see <a href="https://github.com/libgdx/libgdx/wiki/3D-Particle-Effects">3D Particle Effects</a>
	 */
	protected void loadParticles(Engine engine, ParticleSystem particleSystem) {
		// Get all entities with a reactor component
		entities = engine.getEntitiesFor(all(ReactorComponent.class).get());
		// Initialize loaders
		ParticleEffectLoader localLoader = new ParticleEffectLoader(localResolver);
		ParticleEffectLoader internalLoader = new ParticleEffectLoader(internalResolver);
		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(particleSystem.getBatches());
		m_assetManager.setLoader(ParticleEffect.class, localLoader);
		for (Entity entity : entities) {
			ReactorComponent reactorComponent = reactorMapper.get(entity);
			if(reactorComponent.getReactor() == null) {
				String name = reactorComponent.getName();
				if (name != null) {
					try {
						m_assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
						m_assetManager.finishLoading();
					} catch (GdxRuntimeException e) {
						m_assetManager.setLoader(ParticleEffect.class, internalLoader);
						m_assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
						m_assetManager.finishLoading();
						m_assetManager.setLoader(ParticleEffect.class, localLoader);
					}
				} else {
					logger.error("Error loading particle effects: a reactor has no name");
				}
			}
		}
		logger.info("Loaded " + entities.size() + " reactor particle effects");
	}

	@Override
	public void prerender(float delta) {
		logger.debug("Updating asset manager");
		m_assetManager.update();
		logger.debug("Getting asset manager progress");
		m_progress = m_assetManager.getProgress();
		if (m_progress != 1.0f) {
			logger.debug("Setting loading text");
			m_loadingActor.setText((int) (m_progress * 100) + LOADING_TEXT);
		} else {
			logger.info("Loaded " + entities.size() + " models");
			logger.debug("Injecting graphics into entities");
			injectGraphics(entities);
			logger.debug("Loading reactor particle effects");
			loadParticles(getEngine(), getParticleSystem());
			logger.debug("Injecting reactors into entities");
			injectReactors(getEngine().getEntitiesFor(all(ReactorComponent.class).get()));
			logger.debug("Changing screen");
			SpaceFloat.GAME.setScreen(ScreenEnumerator.MAIN);
		}
	}

	/**
	 * Injects a {@link ModelInstance} in every {@link GraphicComponent}
	 */
	protected void injectGraphics(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if (graphic.getInstance() == null) {
				Model model = m_assetManager.get(GraphicComponent.MODELS_DIR + graphic.getName(), Model.class);
				ModelInstance instance = new ModelInstance(model);
				graphic.setInstance(instance);
			}
		}
	}

	/**
	 * Injects a {@link ParticleEffect} in every {@link ReactorComponent}
	 */
	protected void injectReactors(ImmutableArray<Entity> entities) {
		for (Entity entity : entities) {
			ReactorComponent reactor = reactorMapper.get(entity);
			if (reactor.getReactor() == null) {
				ParticleEffect particleEffect = m_assetManager.get(ReactorComponent.PARTICLES_DIR + reactor.getName(), ParticleEffect.class);
				reactor.setReactor(particleEffect);
			}
		}
	}
}
