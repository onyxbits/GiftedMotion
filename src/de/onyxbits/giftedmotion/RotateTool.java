package de.onyxbits.giftedmotion;

import java.awt.Point;

public class RotateTool extends TransformTool
{
	private double lastRotationDeg;
	
	@Override
	public void transform(SingleFrame img, Point mousePos)
	{
		float x = mousePos.x-img.position.x-(img.scaleX/2);
		float y = mousePos.y-img.position.y-(img.scaleY/2);
		if (x != 0) img.rotationDegrees = Math.atan2(y, x) - lastRotationDeg;
	}
	
	@Override
	public String getStatus(SingleFrame img)
	{
		Double[] status = {Math.toDegrees(img.rotationDegrees) % 360};
		return Dict.get("core.imagerotated", status);
	}

	@Override
	public void beginTransform(SingleFrame img, Point mousePos)
	{
		lastRotationDeg = img.rotationDegrees;
		transform(img, mousePos);
		endTransform(img, mousePos);
		transform(img, mousePos);
	}

	@Override
	public void endTransform(SingleFrame img, Point mousePos)
	{
		lastRotationDeg = img.rotationDegrees;
	}

}
