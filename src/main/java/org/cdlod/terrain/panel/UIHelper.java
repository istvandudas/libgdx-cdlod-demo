package org.cdlod.terrain.panel;

import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = false)
public class UIHelper {
	private float labelWith = 100.0f;
	private float valueWidth = 80.0f;

	private final Skin skin;

	public void with(float label, float value) {
		labelWith = label;
		valueWidth = value;
	}

	public Label addLabelValue(Table parent, String label, String defaultValue) {
		Label value = new Label(defaultValue, skin);
		value.setText(defaultValue);
		Table row = new Table();
		row.add(new Label(label+":", skin)).width(labelWith);
		row.add(value).width(valueWidth);
		row.add().fillX().expandX();
		parent.add(row).fillX().expandX().row();
		return value;
	}

	public CheckBox addCheckBox(Table parent, String label, boolean defaultValue) {
		CheckBox value = new CheckBox(label,  skin);
		value.setChecked(defaultValue);
		Table row = new Table();
		row.add(value);
		row.add().fillX().expandX();
		parent.add(row).fillX().expandX().row();
		return value;
	}


}
