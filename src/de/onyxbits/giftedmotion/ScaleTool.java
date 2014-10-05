package de.onyxbits.giftedmotion;

import java.awt.Point;

public class ScaleTool extends TransformTool
{
	
	private float lastScaleX;
	private float lastScaleY;
	
	@Override
	public void transform(SingleFrame img, Point mousePos)
	{
		img.scaleX = mousePos.x - img.position.x - lastScaleX;
		if (!shiftPressed) img.scaleY = mousePos.y - img.position.y - lastScaleY;
		else img.scaleY = ((img.scaleX / img.raw.getWidth()) * img.raw.getHeight());
	}
	
	@Override
	public String getStatus(SingleFrame img)
	{
		Integer[] status = {(int)img.scaleX, (int)Math.floor(img.scaleY)};
		return Dict.get("core.componentresized", status);
	}

	@Override
	public void beginTransform(SingleFrame img, Point mousePos)
	{
		lastScaleX = img.scaleX;
		lastScaleY = img.scaleY;
		transform(img, mousePos);
		endTransform(img, mousePos);
		transform(img, mousePos);
	}

	@Override
	public void endTransform(SingleFrame img, Point mousePos)
	{
		lastScaleX = img.scaleX;
		lastScaleY = img.scaleY;
	}
}
