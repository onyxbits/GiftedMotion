package de.onyxbits.giftedmotion;

import java.awt.Point;

public abstract class TransformTool
{
	protected Point offset;
	
	public void setOffset(Point mousePos)
	{
		this.offset = mousePos;
	}
	
	public abstract void beginTransform(SingleFrame img, Point mousePos);
	public abstract void transform(SingleFrame img, Point mousePos);
	public abstract void endTransform(SingleFrame img, Point mousePos);
	public abstract String getStatus(SingleFrame img);
}
