package org.cdlod.terrain;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.cdlod.terrain.material.TerrainMaterial;
import org.cdlod.terrain.material.addon.daynight.DayNightCycleAddon;
import org.cdlod.terrain.panel.DebugPanel;
import org.cdlod.terrain.panel.TerrainInfoPanel;
import org.cdlod.terrain.panel.UIHelper;
import org.cdlod.terrain.panel.WorldInfoPanel;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(fluent = true)
public class TerrainDemo extends ApplicationAdapter {
	private PerspectiveCamera cam;
	private OrthographicCamera uiCamera;
	private TerrainWrapper terrain;

	private Stage stage;
	private Skin skin;

	private FirstPersonController controller;
	private ShapeRenderer shapeRenderer;

	private WorldInfoPanel worldInfoPanel;
	private TerrainInfoPanel terrainInfoPanel;
	private DebugPanel debugPanel;

	private boolean showAxis = true;

	@Override
	public void create() {
		uiCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		stage = new Stage(new ScreenViewport(uiCamera));

		// Camera
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		cam.position.set(0.0f, 400.0f, 0.0f);
		cam.lookAt(2048.0f, 0, 2048.0f);
		cam.near = 0.1f;
		cam.far = 5000f;
		cam.update();

		shapeRenderer = new ShapeRenderer();

		controller = new FirstPersonController(cam);

		InputMultiplexer inputMultiplexer = new InputMultiplexer(stage, controller);
		Gdx.input.setInputProcessor(inputMultiplexer);

		terrain = new TerrainWrapper(Terrain.builder()
				.material(TerrainMaterial.builder()
						.textures(
								Gdx.files.classpath("asset/terrain/grand-mountain/height.png"),
								Gdx.files.classpath("asset/terrain/grand-mountain/diffuse.png")
								//Gdx.files.classpath("asset/terrain/canyon/height.png"),
								//Gdx.files.classpath("asset/terrain/canyon/diffuse.png"),
								//Gdx.files.classpath("asset/terrain/canyon/normal.png")
						)
						.terrain(
								8192.0f,
								//0.07f,
								//-1200.0f,
								0.1f,
								0.0f,
								64
						)
						.addon(DayNightCycleAddon.builder().enabled(false).build())
						.build())
				.maxLOD(6).fog(true, true)
				.build());

		skin = new Skin(Gdx.files.classpath("asset/skin/uiskin.json"));
		UIHelper uiHelper = new UIHelper(skin);

		worldInfoPanel = new WorldInfoPanel(uiHelper);
		stage.addActor(worldInfoPanel);
		terrainInfoPanel = new TerrainInfoPanel(uiHelper);
		stage.addActor(terrainInfoPanel);
		debugPanel = new DebugPanel(uiHelper, this, terrain);
		stage.addActor(debugPanel);

		worldInfoPanel.setPosition(1122.0f, 588.0f);
		terrainInfoPanel.setPosition(901.0f, 559.0f);
		debugPanel.setPosition(0.0f, 574.0f);

		//Gdx.gl.glEnable(GL20.GL_CULL_FACE);
		Gdx.gl.glDisable(GL20.GL_CULL_FACE);
		Gdx.gl.glCullFace(GL20.GL_BACK);
		Gdx.gl.glFrontFace(GL20.GL_CW);

		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
		Gdx.gl.glDepthFunc(GL20.GL_LEQUAL);
	}

	private Vector3 origo = new Vector3(0.0f, 0.0f, 0.0f);
	private Vector3 xAxis = new Vector3(4096.0f, 0.0f, 0.0f);
	private Vector3 zAxis = new Vector3(0.0f, 0.0f, 4096.0f);
	private Vector3 yAxis = new Vector3(0.0f, 4096.0f, 0.0f);

	@Override
	public void render() {
		update(Gdx.graphics.getDeltaTime());
		ScreenUtils.clear(0.2f, 0.3f, 0.4f, 1f);

		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glFrontFace(GL20.GL_CCW);
		terrain.render(cam);
		Gdx.gl.glActiveTexture(GL20.GL_TEXTURE0);
		Gdx.gl.glFrontFace(GL20.GL_CW);

		if (showAxis) {
			shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
			shapeRenderer.setProjectionMatrix(cam.combined);
			shapeRenderer.setColor(Color.RED);
			shapeRenderer.line(origo, xAxis);
			shapeRenderer.setColor(Color.GREEN);
			shapeRenderer.line(origo, zAxis);
			shapeRenderer.setColor(Color.BLUE);
			shapeRenderer.line(origo, yAxis);
			shapeRenderer.end();
		}
		stage.draw();
	}

	private void update(float deltaTime) {
		terrain.update(deltaTime);
		controller.update(deltaTime);
		stage.act(deltaTime);
		worldInfoPanel.update(cam.position, controller.prevSpeed());
		terrainInfoPanel.update(terrain);
	}

	@Override
	public void dispose() {
		super.dispose();
		skin.dispose();
	}
}
