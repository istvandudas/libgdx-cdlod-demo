package org.cdlod.terrain;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(fluent = true, chain = false)
public class FirstPersonController implements InputProcessor {

	private final PerspectiveCamera cam;

	private float baseSpeed = 10f;
	private float speedAcceleration = 1.0f;
	private float speed;
	private float prevSpeed;
	private float move;

	private float mouseSensitivity = 0.2f;

	private boolean rightDown = false;
	private int lastX, lastY;

	private float yaw;    // rotate around world up
	private float pitch;  // rotate around camera right

	public FirstPersonController(PerspectiveCamera cam) {
		this.cam = cam;
		Vector3 d = cam.direction.cpy();
		yaw = (float)Math.toDegrees(Math.atan2(d.x, d.z));
		pitch = (float)Math.toDegrees(Math.asin(d.y));
	}

	public void update(float delta) {
		speedAcceleration = 1.0f;
		if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT) && Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speedAcceleration = 100.0f;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
			speedAcceleration = 50.0f;
		}
		else if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
			speedAcceleration = 10.0f;
		}
		speed = baseSpeed * speedAcceleration;
		move = speed * delta;
		int pressedKeyCount = 0;
		Vector3 forward = cam.direction.cpy().nor();

		if (Gdx.input.isKeyPressed(Input.Keys.W)) {
			cam.position.add(forward.scl(move));
			pressedKeyCount++;
		}
		if (Gdx.input.isKeyPressed(Input.Keys.S)) {
			cam.position.add(forward.scl(-move));
			pressedKeyCount++;
		}

		Vector3 flatForward = new Vector3(cam.direction.x, 0, cam.direction.z).nor();
		Vector3 right = flatForward.crs(Vector3.Y).nor();

		if (Gdx.input.isKeyPressed(Input.Keys.D)) {
			cam.position.add(right.scl(move));
			pressedKeyCount++;
		}

		if (Gdx.input.isKeyPressed(Input.Keys.A)) {
			cam.position.add(right.scl(-move));
			pressedKeyCount++;
		}

		if (pressedKeyCount == 0) {
			prevSpeed = 0.0f;
		}
		else {
			prevSpeed = speed;
		}

		cam.update();
	}

	@Override
	public boolean touchDown(int x, int y, int pointer, int button) {
		if (button == Input.Buttons.RIGHT) {
			rightDown = true;
			lastX = x;
			lastY = y;
		}
		return true;
	}

	@Override
	public boolean touchUp(int x, int y, int pointer, int button) {
		if (button == Input.Buttons.RIGHT) {
			rightDown = false;
		}
		return true;
	}

	@Override
	public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int x, int y, int pointer) {
		if (!rightDown) return false;

		int dx = x - lastX;
		int dy = y - lastY;

		lastX = x;
		lastY = y;

		// Horizontal mouse → yaw around WORLD UP
		yaw -= dx * mouseSensitivity;

		// Vertical mouse → pitch around camera RIGHT
		pitch -= dy * mouseSensitivity;

		// Clamp pitch
		pitch = Math.max(-89f, Math.min(89f, pitch));

		// Convert yaw/pitch to direction vector
		float yawRad = (float)Math.toRadians(yaw);
		float pitchRad = (float)Math.toRadians(pitch);

		cam.direction.set(
				(float)(Math.cos(pitchRad) * Math.sin(yawRad)),
				(float)(Math.sin(pitchRad)),
				(float)(Math.cos(pitchRad) * Math.cos(yawRad))   // <-- NEGATE Z
		).nor();


		// Keep camera upright
		cam.up.set(0, 1, 0);

		cam.update();
		return true;
	}

	@Override public boolean mouseMoved(int x, int y) { return false; }
	@Override public boolean keyDown(int keycode) { return false; }
	@Override public boolean keyUp(int keycode) { return false; }
	@Override public boolean keyTyped(char character) { return false; }
	@Override public boolean scrolled(float amountX, float amountY) { return false; }
}
