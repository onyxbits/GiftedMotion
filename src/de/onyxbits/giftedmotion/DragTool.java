package de.onyxbits.giftedmotion;

import java.awt.Point;

public class DragTool extends TransformTool
{
	
	@Override
	public void transform(SingleFrame img, Point mousePos)
	{
		img.position.x=mousePos.x-offset.x;
	    img.position.y=mousePos.y-offset.y;
	}

	@Override
	public void beginTransform(SingleFrame img, Point mousePos)
	{
	}

	@Override
	public void endTransform(SingleFrame img, Point mousePos)
	{
	}
	
	@Override
	public String getStatus(SingleFrame img)
	{
		Integer[] status = {img.position.x, img.position.y};
		return Dict.get("core.imagedragged", status);
	}
}
