package me.fahien.spacefloat.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.utils.ImmutableArray;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.LocalFileHandleResolver;
import com.badlogic.gdx.audio.Sound;
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
import me.fahien.spacefloat.audio.Audio;
import me.fahien.spacefloat.component.EnergyComponent;
import me.fahien.spacefloat.component.GraphicComponent;
import me.fahien.spacefloat.component.ParticleComponent;
import me.fahien.spacefloat.component.ReactorComponent;
import me.fahien.spacefloat.entity.GameObject;
import me.fahien.spacefloat.factory.GameObjectFactory;
import me.fahien.spacefloat.factory.MissionFactory;
import me.fahien.spacefloat.game.SpaceFloat;
import me.fahien.spacefloat.mission.Mission;

import static com.badlogic.ashley.core.Family.all;
import static com.badlogic.ashley.core.Family.one;
import static me.fahien.spacefloat.component.ComponentMapperEnumerator.energyMapper;
import static me.fahien.spacefloat.game.SpaceFloatGame.logger;

/**
 * The Loading {@link SpaceFloatScreen}
 *
 * @author Fahien
 */
public class LoadingScreen extends SpaceFloatScreen {
	private static final String WELCOME_MESSAGE = "Welcome, space courier! Do you want to earn some money? Follow the gray line to reach the station.";
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
		loadObjects(engine, getGameObjectFactory());
		loadModels(engine, getAssetManager(), localResolver);
		loadSounds(getAssetManager(), internalResolver);
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
	protected void loadObjects(final Engine engine, final GameObjectFactory factory) {
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
	protected void loadModels(final Engine engine, final AssetManager assetManager, final FileHandleResolver resolver) {
		Family family = all(GraphicComponent.class).get();
		entities = engine.getEntitiesFor(family);
		G3dModelLoader loader = new G3dModelLoader(new JsonReader(), resolver);
		assetManager.setLoader(Model.class, loader);
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
		// Load parcel model
		assetManager.load(GraphicComponent.MODELS_DIR + MissionFactory.PARCEL_GRAPHIC, Model.class);
	}

	/**
	 * Loads {@link Sound}s
	 */
	private void loadSounds(final AssetManager assetManager, final FileHandleResolver resolver) {
		SoundLoader loader = new SoundLoader(resolver);
		assetManager.setLoader(Sound.class, loader);
		assetManager.load(Audio.SOUNDS_DIR + Audio.REACTOR_SOUND, Sound.class);
		assetManager.load(Audio.SOUNDS_DIR + Audio.RECHARGE_SOUND, Sound.class);
		assetManager.load(Audio.SOUNDS_DIR + Audio.COLLISION_SOUND, Sound.class);
		assetManager.finishLoading();
	}

	/**
	 * Loads the {@link ParticleEffect}s
	 *
	 * @see <a href="https://github.com/libgdx/libgdx/wiki/3D-Particle-Effects">3D Particle Effects</a>
	 */
	protected void loadParticles(Engine engine, ParticleSystem particleSystem, final AssetManager assetManager) {
		logger.debug("Loading reactor particle effects");
		// Get all entities with a reactor component
		entities = engine.getEntitiesFor(one(ReactorComponent.class, EnergyComponent.class).get());
		// Initialize loaders
		ParticleEffectLoader localLoader = new ParticleEffectLoader(localResolver);
		ParticleEffectLoader internalLoader = new ParticleEffectLoader(internalResolver);
		Array<ParticleBatch<?>> batches = particleSystem.getBatches();
		if (batches == null || batches.size == 0) {
			throw new GdxRuntimeException("Particle Batches are null");
		}
		ParticleEffectLoader.ParticleEffectLoadParameter loadParam = new ParticleEffectLoader.ParticleEffectLoadParameter(batches);
		assetManager.setLoader(ParticleEffect.class, localLoader);
		for (Entity entity : entities) {
			ParticleComponent particleComponent = reactorMapper.get(entity);
			loadParticle(particleComponent, assetManager, loadParam, localLoader, internalLoader);
			particleComponent = energyMapper.get(entity);
			loadParticle(particleComponent, assetManager, loadParam, localLoader, internalLoader);
		}
		logger.info("Loaded " + entities.size() + " reactor particle effects");
	}

	private void loadParticle(final ParticleComponent particleComponent,
							  final AssetManager assetManager,
							  final ParticleEffectLoader.ParticleEffectLoadParameter loadParam,
							  final ParticleEffectLoader localLoader,
							  final ParticleEffectLoader internalLoader) {
		if(particleComponent != null && particleComponent.getEffect() == null) {
			String name = particleComponent.getName();
			if (name != null) {
				try {
					assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
					assetManager.finishLoadingAsset(ReactorComponent.PARTICLES_DIR + name);
				} catch (GdxRuntimeException e) {
					assetManager.setLoader(ParticleEffect.class, internalLoader);
					assetManager.load(ReactorComponent.PARTICLES_DIR + name, ParticleEffect.class, loadParam);
					try {
						assetManager.finishLoadingAsset(ReactorComponent.PARTICLES_DIR + name);
					} catch (GdxRuntimeException ex) {
						logger.error("Could not load " + ReactorComponent.PARTICLES_DIR + name + ": " + e.getMessage() + "\n\t" + e.getCause());
					}
					assetManager.setLoader(ParticleEffect.class, localLoader);
				}
			} else {
				logger.error("Error loading particle effects: a reactor has no name");
			}
		}
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
	protected void injectReactors(final ImmutableArray<Entity> entities, final AssetManager assetManager) {
		logger.debug("Injecting reactors into entities");
		for (Entity entity : entities) {
			ParticleComponent particle = reactorMapper.get(entity);
			injectEffect(particle, assetManager);
			particle = energyMapper.get(entity);
			injectEffect(particle, assetManager);
		}
	}

	private void injectEffect(final ParticleComponent particle, final AssetManager assetManager) {
		if (particle != null && particle.getEffect() == null) {
			String particleName = ReactorComponent.PARTICLES_DIR + particle.getName();
			ParticleEffect particleEffect = assetManager.get(particleName, ParticleEffect.class);
			particle.setEffect(particleEffect);
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
				loadParticles(engine, getParticleSystem(), getAssetManager());
				ImmutableArray<Entity> particleEntities = engine.getEntitiesFor(one(ReactorComponent.class, EnergyComponent.class).get());
				injectReactors(particleEntities, getAssetManager());
			} catch (GdxRuntimeException e) {
				logger.error("Could not inject reactors: " + e.getMessage());
				m_loadingActor.setText("Could not inject reactors, please restart SpaceFloat");
			}
			loadMissions(MissionFactory.INSTANCE);
			SpaceFloat.GAME.setScreen(ScreenEnumerator.GAME);
			if (forceReload) {
				getGame().enqueueMessage(WELCOME_MESSAGE);
			}
		}
	}

	@Override
	public void hide() {
		super.hide();
		m_progress = 0.0f;
	}
}
