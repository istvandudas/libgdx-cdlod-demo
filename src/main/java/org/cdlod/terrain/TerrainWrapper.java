package org.cdlod.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(fluent = true)
public class TerrainWrapper {
	private final Terrain terrain;

	private final Color[] debugColors = {Color.GRAY, Color.WHITE, Color.BLUE, Color.GREEN, Color.YELLOW, Color.ORANGE, Color.RED};
	private final ShapeRenderer debugShapeRenderer = new ShapeRenderer();
	private boolean debugQuadTree = false;
	private boolean debugVisibleQuads = false;
	private int debugLod;

	public void update(float deltaTime) {
		terrain.update(deltaTime);
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_0)) {
			debugLod = 0;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
			debugLod = 1;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
			debugLod = 2;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)) {
			debugLod = 3;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)) {
			debugLod = 4;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_5)) {
			debugLod = 5;
		}
		if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_6)) {
			debugLod = 6;
		}
	}
	public void debugTreeNode(int lod, Camera cam, QuadTreeNode node, ShapeRenderer renderer) {
		renderer.begin(ShapeRenderer.ShapeType.Line);
		renderer.setProjectionMatrix(cam.combined);
		debugChildTreeNode(lod, node, renderer);
		renderer.end();
	}

	private void debugChildTreeNode(int lod, QuadTreeNode node, ShapeRenderer renderer) {
		if (node.lod() == lod) {
			renderer.setColor(debugColors[lod]);
			drawBoundingBox(renderer, node.bounds());
		} else if (node.lod() < lod) {
			for (int i = 0; i < 4; i++) {
				QuadTreeNode child = node.children()[i];
				debugChildTreeNode(lod, child, renderer);
			}
		}
	}

	public void debugVisibleNodes(Camera cam, List<QuadTreeNode> nodes, ShapeRenderer renderer) {
		renderer.setProjectionMatrix(cam.combined);
		renderer.begin(ShapeRenderer.ShapeType.Line);
		for (QuadTreeNode node : nodes) {
			renderer.setColor(debugColors[node.lod()]);
			drawBoundingBox(renderer, node.bounds());
		}
		renderer.end();
	}

	private void drawBoundingBox(ShapeRenderer renderer, BoundingBox box) {
		Vector3 min = box.min;
		Vector3 max = box.max;
		renderer.box(
				min.x, min.y, min.z,
				max.x - min.x,
				max.y - min.y,
				min.z - max.z
		);
	}

	public void render(Camera cam) {
		terrain.render(cam);
		if (debugQuadTree) {
			debugTreeNode(debugLod, cam, terrain.quadTree().root(), debugShapeRenderer);
		}
		if (debugVisibleQuads) {
			debugVisibleNodes(cam, terrain.visibleNodes(), debugShapeRenderer);
		}
	}
}
