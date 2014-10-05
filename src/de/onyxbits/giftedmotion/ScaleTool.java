package de.onyxbits.giftedmotion;

import java.awt.Point;
import java.awt.geom.Point2D;

public class ScaleTool extends TransformTool
{
	
	@Override
	public void transform(SingleFrame img, Point mousePos)
	{
		img.scaleX = mousePos.x - img.position.x;
		if (!shiftPressed) img.scaleY = mousePos.y - img.position.y;
		else img.scaleY = (img.scaleX / img.raw.getWidth()) * img.raw.getHeight();
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
	}

	@Override
	public void endTransform(SingleFrame img, Point mousePos)
	{
	}
}
