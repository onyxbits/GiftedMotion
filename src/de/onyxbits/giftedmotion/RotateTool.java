package de.onyxbits.giftedmotion;

import java.awt.Point;

public class RotateTool extends TransformTool
{
	private double lastRotationDeg;
	
	@Override
	public void transform(SingleFrame img, Point mousePos)
	{
		int x = mousePos.x-img.position.x-(img.raw.getWidth()/2);
		int y = mousePos.y-img.position.y-(img.raw.getHeight()/2);
		if (x != 0) img.rotationDegrees = Math.atan2(y, x) - lastRotationDeg;
	}
	
	@Override
	public String getStatus(SingleFrame img)
	{
		Double[] status = {Math.toDegrees(img.rotationDegrees)};
		return Dict.get("core.imagerotated", status);
	}

	@Override
	public void beginTransform(SingleFrame img, Point mousePos)
	{
		lastRotationDeg = img.rotationDegrees;
		transform(img, mousePos);
		endTransform(img, mousePos);
	}

	@Override
	public void endTransform(SingleFrame img, Point mousePos)
	{
		lastRotationDeg = img.rotationDegrees;
	}

}
