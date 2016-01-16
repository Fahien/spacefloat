package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.loader.G3dModelLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffect;
import com.badlogic.gdx.graphics.g3d.particles.ParticleEffectLoader;
import com.badlogic.gdx.graphics.g3d.particles.ParticleSystem;
import com.badlogic.gdx.graphics.g3d.particles.batches.ParticleBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.JsonReader;

import me.fahien.spacefloat.actor.FontActor;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.GameObjectFactory;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Loading {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class LoadingScreen extends SpaceFloatScreen {
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
	private boolean loadInternal;
	private boolean forceReload;

	/**
	 * Set load internal
	 */
	public void setLoadInternal(boolean loadInternal) {
		this.loadInternal = loadInternal;
	}

	/**
	 * Sets force reload of game objects
	 */
	public void setForceReload(boolean forceReload) {
		this.forceReload = forceReload;
	}

	@Override
	public void populate(Stage stage) {
		m_loadingActor = new FontActor(getFont(), m_progress + LOADING_TEXT);
		m_loadingActor.setPosition(SpaceFloatScreen.WIDTH / 2, SpaceFloatScreen.HEIGHT / 2);
		m_loadingActor.setHalign(FontActor.Halign.CENTER);
		stage.addActor(m_loadingActor);
		localResolver = new LocalFileHandleResolver();
		internalResolver = new InternalFileHandleResolver();
		Engine engine = getEngine();
		loadObjects(engine);
		loadModels(engine);
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
		GameObjectFactory factory = getGameObjectFactory();
		Array<GameObject> objects;
		/*
		if (loadInternal) {
			objects = factory.loadInternalObjects();
		} else {
			objects = factory.loadObjects();
		}
		 */
		objects = factory.loadObjects(forceReload);
		if (objects != null) {
			for (GameObject object : objects) {
				if (object.isPlayer()) {
					getGame().setPlayer(object);
				}
				engine.addEntity(object);
			}
		} else {
			logger.error("Could not load objects");
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
		// Load parcel model
		m_assetManager.load(GraphicComponent.MODELS_DIR + MissionFactory.PARCEL_GRAPHIC, Model.class);
	}

	/**
	 * Loads the {@link ParticleEffect}s
	 *
	 * @see <a href="https://github.com/libgdx/libgdx/wiki/3D-Particle-Effects">3D Particle Effects</a>
	 */
	protected void loadParticles(Engine engine, ParticleSystem particleSystem) {
		logger.debug("Loading reactor particle effects");
		// Get all entities with a reactor component
		entities = engine.getEntitiesFor(all(ReactorComponent.class).get());
		// Initialize loaders
		ParticleEffectLoader localLoader = new ParticleEffectLoader(localResolver);
		ParticleEffectLoader internalLoader = new ParticleEffectLoader(internalResolver);
		Array<ParticleBatch<?>> batches = particleSystem.getBatches();
		if (batches == null || batches.size == 0) {
			throw new GdxRuntimeException("Particle Batches are null");
		}
		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(batches);
		m_assetManager.setLoader(ParticleEffect.class, localLoader);
		for (Entity entity : entities) {
			ReactorComponent reactorComponent = reactorMapper.get(entity);
			if(reactorComponent.getReactor() == null) {
				String name = reactorComponent.getName();
				if (name != null) {
					try {
						m_assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
						m_assetManager.finishLoadingAsset(ReactorComponent.PARTICLES_DIR + name);
					} catch (GdxRuntimeException e) {
						m_assetManager.setLoader(ParticleEffect.class, internalLoader);
						m_assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
						try {
							m_assetManager.finishLoadingAsset(ReactorComponent.PARTICLES_DIR + name);
						} catch (GdxRuntimeException ex) {
							logger.error("Could not load " + ReactorComponent.PARTICLES_DIR + name + ": " + e.getMessage());
						}
						m_assetManager.setLoader(ParticleEffect.class, localLoader);
					}
				} else {
					logger.error("Error loading particle effects: a reactor has no name");
				}
			}
		}
		logger.info("Loaded " + entities.size() + " reactor particle effects");
	}

	/**
	 * Injects a {@link ModelInstance} in every {@link GraphicComponent}
	 */
	protected void injectGraphics(ImmutableArray<Entity> entities) {
		logger.debug("Injecting graphics into entities");
		for (Entity entity : entities) {
			GraphicComponent graphic = graphicMapper.get(entity);
			if (graphic.getInstance() == null) {
				Model model = m_assetManager.get(GraphicComponent.MODELS_DIR + graphic.getName(), Model.class);
				ModelInstance instance = new ModelInstance(model);
				graphic.setInstance(instance);
			}
		}
		// Inject parcel model instance
		Model model = m_assetManager.get(GraphicComponent.MODELS_DIR + MissionFactory.PARCEL_GRAPHIC, Model.class);
		ModelInstance instance = new ModelInstance(model);
		MissionFactory.INSTANCE.getParcelGraphic().setInstance(instance);
	}

	/**
	 * Injects a {@link ParticleEffect} in every {@link ReactorComponent}
	 */
	protected void injectReactors(ImmutableArray<Entity> entities) {
		logger.debug("Injecting reactors into entities");
		for (Entity entity : entities) {
			ReactorComponent reactor = reactorMapper.get(entity);
			if (reactor.getReactor() == null) {
				String particleName = ReactorComponent.PARTICLES_DIR + reactor.getName();
				ParticleEffect particleEffect = m_assetManager.get(particleName, ParticleEffect.class);
				reactor.setReactor(particleEffect);
			}
		}
	}

	/**
	 * Loads {@link Mission}s
	 */
	private void loadMissions(MissionFactory missionFactory) {
		logger.debug("Loading missions");
		/*
		if (loadInternal) {
			missionFactory.loadInternalMissions();
		} else {
			missionFactory.loadMissions();
		}
		*/
		missionFactory.loadMissions(forceReload);
		missionFactory.loadNextMission();
	}

	@Override
	public void update(float delta) {
		super.update(delta);
		if (m_progress < 1.0f) {
			m_assetManager.update();
			m_progress = m_assetManager.getProgress();
			m_loadingActor.setText((int) (m_progress * 100) + LOADING_TEXT);
		}
		if (m_progress >= 1.0f) {
			logger.info("Loaded " + entities.size() + " models");
			try {
				injectGraphics(entities);
				Engine engine = getEngine();
				loadParticles(engine, getParticleSystem());
				injectReactors(engine.getEntitiesFor(all(ReactorComponent.class).get()));
			} catch (GdxRuntimeException e) {
				logger.error("Could not inject reactors: " + e.getMessage());
				m_loadingActor.setText("Could not inject reactors, please restart SpaceFloat");
			}
			loadMissions(MissionFactory.INSTANCE);
			SpaceFloat.GAME.setScreen(ScreenEnumerator.GAME);
		}
	}

	@Override
	public void hide() {
		super.hide();
		m_progress = 0.0f;
	}
}
