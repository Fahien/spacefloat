package me.fahien.spacefloat;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.headless.HeadlessApplication;
import com.badlogic.gdx.backends.headless.HeadlessApplicationConfiguration;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.utils.Logger;

import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.HashMap;
import java.util.Map;

import static com.badlogic.gdx.Gdx.app;
import static org.mockito.Mockito.mock;

/**
 * Gdx Test Runner for gl-context dependent tests
 *
 * @author Fahien
 * @see <a href="http://badlogicgames.com/forum/viewtopic.php?f=17&t=1485">JUnit runner for GL context-dependent tests</a>
 */
public class GdxTestRunner extends BlockJUnit4ClassRunner implements ApplicationListener{
	private final Map<FrameworkMethod, RunNotifier> invokeInRender = new HashMap<>();

	/**
	 * This is the {@link Logger} which will be used throughout the whole testing
	 */
	public static final Logger logger = new Logger(GdxTestRunner.class.getSimpleName());

	public GdxTestRunner(Class<?> _class) throws InitializationError {
		super(_class);
		HeadlessApplicationConfiguration conf = new HeadlessApplicationConfiguration();
		new HeadlessApplication(this, conf);

		Gdx.gl = mock(GL20.class);
		Gdx.gl20 = mock(GL20.class);
		Gdx.gl30 = mock(GL30.class);

		logger.setLevel(Logger.INFO);
		app.setLogLevel(Application.LOG_INFO);
	}

	@Override public void create() {}
	@Override public void resume() {}
	@Override public void resize(int width, int height) {}
	@Override public void pause() {}
	@Override public void dispose() {}

	@Override public void render() {
		synchronized (invokeInRender) {
			for(Map.Entry<FrameworkMethod, RunNotifier> each : invokeInRender.entrySet()){
				super.runChild(each.getKey(), each.getValue());
			}
			invokeInRender.clear();
		}
	}

	@Override protected void runChild(FrameworkMethod method, RunNotifier notifier) {
		synchronized (invokeInRender) {
			invokeInRender.put(method, notifier);	//add for invoking in render phase, where gl context is available
		}
		waitUntilInvokedInRenderMethod();	//wait until that test was invoked
	}

	private void waitUntilInvokedInRenderMethod() {
		try {
			while (true) {
				Thread.sleep(10);
				synchronized (invokeInRender) {
					if (invokeInRender.isEmpty()) break;
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
