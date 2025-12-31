package org.cdlod.terrain.panel;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;

public class WorldInfoPanel extends Window {
	private static final String DEFAULT_VALUE = "0";
	private final Label fpsLabel;
	private final Label posXLabel;
	private final Label posYLabel;
	private final Label posZLabel;
	private final Label speedLabel;

	public WorldInfoPanel(UIHelper uiHelper) {
		super("World Info", uiHelper.skin());
		uiHelper.with(40.0f, 80.0f);
		fpsLabel = uiHelper.addLabelValue(this, "fps", DEFAULT_VALUE);
		posXLabel = uiHelper.addLabelValue(this, "x", DEFAULT_VALUE);
		posYLabel = uiHelper.addLabelValue(this, "y", DEFAULT_VALUE);
		posZLabel = uiHelper.addLabelValue(this, "z", DEFAULT_VALUE);
		speedLabel = uiHelper.addLabelValue(this, "spd", DEFAULT_VALUE);
		pack();
	}

	public void update(Vector3 worldPos, float speed) {
		fpsLabel.setText(Gdx.graphics.getFramesPerSecond());
		posXLabel.setText(Float.toString(worldPos.x));
		posYLabel.setText(Float.toString(worldPos.y));
		posZLabel.setText(Float.toString(worldPos.z));
		speedLabel.setText(speed + " m/s");
	}

}
