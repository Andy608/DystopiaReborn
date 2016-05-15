package com.bountive.dystopia.camera;

import math.Vector3f;

import com.bountive.dystopia.component.callback.CursorPosCallback;
import com.bountive.dystopia.file.setting.ControlSettings;
import com.bountive.dystopia.math.MathHelper;
import com.bountive.dystopia.math.MatrixMathHelper;

public class FreeRoamCamera extends Camera {

	public FreeRoamCamera() {
		this(new Vector3f());
	}
	
	public FreeRoamCamera(Vector3f pos) {
		super(pos, new Vector3f());
	}
	
	public FreeRoamCamera(Vector3f pos, Vector3f rot) {
		super(pos, new Vector3f());
	}
	
	@Override
	public void update(float deltaTime) {
		acceleration.set(0, 0, 0);
		friction.set(0, 0, 0);
		moveCamera(deltaTime);
		rotateCamera();
		createViewMatrix();
	}
	
	@Override
	protected void createViewMatrix() {
		viewMatrix.setIdentity();
		viewMatrix.rotate((float)Math.toRadians(getPitch()), MatrixMathHelper.X_AXIS);
		viewMatrix.rotate((float)Math.toRadians(getYaw()), MatrixMathHelper.Y_AXIS);
		viewMatrix.rotate((float)Math.toRadians(getRoll()), MatrixMathHelper.Z_AXIS);
		viewMatrix.translate(new Vector3f(-position.x, -position.y, -position.z));
	}
	
	private void moveCamera(float deltaTime) {
		if (ControlSettings.moveForwardKey.isPressed()) {
			acceleration.x += (float)(MathHelper.sin(getYaw())/* * MathHelper.cos(getPitch())*/);
//			acceleration.y -= (float)(MathHelper.sin(getPitch()));
			acceleration.z -= (float)(MathHelper.cos(getYaw())/* * MathHelper.cos(getPitch())*/);
		}
		
		if (ControlSettings.moveBackwardKey.isPressed()) {
			acceleration.x -= (float)(MathHelper.sin(getYaw())/* * MathHelper.cos(getPitch())*/);
//			acceleration.y += (float)(MathHelper.sin(getPitch()));
			acceleration.z += (float)(MathHelper.cos(getYaw())/* * MathHelper.cos(getPitch())*/);
		}
		
		 if (ControlSettings.moveLeftKey.isPressed()) {
			 acceleration.x -= (float)(MathHelper.sin(getYaw() - 90));
			 acceleration.z += (float)(MathHelper.cos(getYaw() - 90));
		 }
		
		 if (ControlSettings.moveRightKey.isPressed()) {
			 acceleration.x += (float)(MathHelper.sin(getYaw() - 90));
			 acceleration.z -= (float)(MathHelper.cos(getYaw() - 90));
		 }
		 
		 if (ControlSettings.moveUpKey.isPressed()) {
			 acceleration.y += 1;
		 }
		 
		 if (ControlSettings.moveDownKey.isPressed()) {
			 acceleration.y -= 1;
		 }
		
		Vector3f.add(velocity, (Vector3f)(acceleration.scale((float)deltaTime)), velocity);
		
		float velocityLength = velocity.lengthSquared();
		if (velocityLength != 0) {
			friction.set(velocity).negate().scale(0.1f);
			Vector3f.add(velocity, friction, velocity);
			if (velocity.lengthSquared() < MIN_VELOCITY) velocity.set(0, 0, 0);
		}
		
		velocity.x = MathHelper.clampFloat(velocity.x, -MAX_SPEED, MAX_SPEED);
		velocity.y = MathHelper.clampFloat(velocity.y, -MAX_SPEED, MAX_SPEED);
		velocity.z = MathHelper.clampFloat(velocity.z, -MAX_SPEED, MAX_SPEED);
		Vector3f.add(position, velocity, position);
	}
	
	private void rotateCamera() {
		rotationAcceleration.set(ControlSettings.mouseSensitivity.getValue() * (float)Math.toRadians(CursorPosCallback.getMouseOffsetX()), ControlSettings.mouseSensitivity.getValue() * (float)Math.toRadians(CursorPosCallback.getMouseOffsetY()), 0);
		Vector3f.add(rotationVelocity, (Vector3f)rotationAcceleration, rotationVelocity);
		
		float rotVelLength = rotationVelocity.lengthSquared();
		if (rotVelLength != 0) {
			friction.set(rotationVelocity).negate().scale(0.5f);
			Vector3f.add(rotationVelocity, friction, rotationVelocity);
			if (rotationVelocity.lengthSquared() < MIN_VELOCITY) rotationVelocity.set(0, 0, 0);
		}
		
		rotation.y += rotationVelocity.x;
		rotation.x = MathHelper.clampFloat(rotation.x + rotationVelocity.y, -90, 90);
	}
}
