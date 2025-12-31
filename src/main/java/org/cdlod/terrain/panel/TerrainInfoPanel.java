package org.cdlod.terrain.panel;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import org.cdlod.terrain.TerrainWrapper;
import org.cdlod.terrain.material.TerrainMaterial;

public class TerrainInfoPanel extends Window {

	private final Label terrainSize;
	private final Label heightMapSize;
	private final Label diffuseMapSize;
	private final Label normalMapSize;
	private final Label heightScale;
	private final Label heightOffset;
	private final Label gridSize;

	public TerrainInfoPanel(UIHelper uiHelper) {
		super("Terrain Info", uiHelper.skin());

		uiHelper.with(100.0f, 100.0f);
		terrainSize = uiHelper.addLabelValue(this, "size", "0");
		heightMapSize = uiHelper.addLabelValue(this, "heightMap", "0");
		diffuseMapSize = uiHelper.addLabelValue(this, "diffuseMap", "0");
		normalMapSize = uiHelper.addLabelValue(this, "normalMap", "0");
		heightScale = uiHelper.addLabelValue(this, "height scale", "0");
		heightOffset = uiHelper.addLabelValue(this, "height offset", "0");
		gridSize = uiHelper.addLabelValue(this, "grid size", "0");

		pack();
	}

	public void update(TerrainWrapper terrain) {
		TerrainMaterial mat = terrain.terrain().material();
		terrainSize.setText(Float.toString(mat.terrainSize()));
		heightMapSize.setText(mat.height().getWidth() + "x" + mat.height().getHeight());
		diffuseMapSize.setText(mat.height().getWidth() + "x" + mat.height().getHeight());
		if (mat.hasNormalMap()) {
			normalMapSize.setText(mat.height().getWidth() + "x" + mat.height().getHeight());
		}
		else {
			normalMapSize.setText("0x0");
		}
		heightScale.setText(Float.toString(mat.heightScale()));
		heightOffset.setText(Float.toString(mat.heightOffset()));
		gridSize.setText(Float.toString(mat.gridResolution()));
	}

}
