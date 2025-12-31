package org.cdlod.terrain.panel;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import org.cdlod.terrain.TerrainDemo;
import org.cdlod.terrain.TerrainWrapper;
import org.cdlod.terrain.material.TerrainMaterial;
import org.cdlod.terrain.material.addon.daynight.DayNightCycleAddon;

public class DebugPanel extends Window {

	private final CheckBox showAxis;
	private final CheckBox showQuadTree;
	private final CheckBox showVisibleQuads;
	private final CheckBox dayNightEnabled;
	private final CheckBox distantFogEnabled;
	private final CheckBox atmosphericScatterEnabled;

	public DebugPanel(UIHelper uiHelper, TerrainDemo demo, TerrainWrapper terrain) {
		super("Debug", uiHelper.skin());
		showAxis = uiHelper.addCheckBox(this, " show axis", true);
		showAxis.addListener(event -> {
			demo.showAxis(showAxis.isChecked());
			return true;
		});
		showQuadTree = uiHelper.addCheckBox(this, " show quads per lod (0..6)", false);
		showQuadTree.addListener(event -> {
			terrain.debugQuadTree(showQuadTree.isChecked());
			return true;
		});
		showVisibleQuads = uiHelper.addCheckBox(this, " show visible quads", false);
		showVisibleQuads.addListener(event -> {
			terrain.debugVisibleQuads(showVisibleQuads.isChecked());
			return true;
		});
		dayNightEnabled = uiHelper.addCheckBox(this, " day night cycle", true);
		dayNightEnabled.addListener(event -> {
			TerrainMaterial material = terrain.terrain().material();
			DayNightCycleAddon dayNightCycleAddon = (DayNightCycleAddon) material.addons().getFirst();
			dayNightCycleAddon.enabled(dayNightEnabled.isChecked());
			if (!dayNightEnabled.isChecked()) {
				material.resetToDefault();
			}
			material.updateLightSettings(true);
			return true;
		});
		distantFogEnabled = uiHelper.addCheckBox(this, " distant fog", true);
		distantFogEnabled.addListener(event -> {
			terrain.terrain().material().distantFogEnabled(distantFogEnabled.isChecked());
			return true;
		});
		atmosphericScatterEnabled = uiHelper.addCheckBox(this, " atmospheric scatter fog", true);
		atmosphericScatterEnabled.addListener(event -> {
			terrain.terrain().material().atmosphericScatterEnabled(atmosphericScatterEnabled.isChecked());
			return true;
		});
		pack();
	}
}
