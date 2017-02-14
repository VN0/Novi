package io.anuke.novi.utils;

import static io.anuke.ucore.UCore.clamp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;

import io.anuke.novi.modules.Renderer;
import io.anuke.novi.modules.World;
import io.anuke.ucore.graphics.Atlas;
import io.anuke.ucore.graphics.ShapeUtils;

public class Draw{
	private static Renderer rend;
	private static Color temp = new Color();
	private static Vector2 vector = new Vector2();
	
	public static void init(Renderer renderer){
		rend = renderer;
	}
	
	public static Batch batch(){
		return rend.batch;
	}
	
	public static void rect(String name, float x, float y){
		TextureRegion reg = region(name);
		rect(reg, x, y, 0);
	}
	
	public static void rect(String name, float x, float y, float rotation){
		TextureRegion reg = region(name);
		rect(reg, x, y, rotation);
	}
	
	public static void rect(String name, float x, float y, float scalex, float scaley, float rotation){
		TextureRegion reg = region(name);
		
		x = overlapx(x, reg.getRegionWidth());
		y = overlapy(y, reg.getRegionHeight());
		
		rend.batch.draw(reg, x - reg.getRegionWidth()/2, y - reg.getRegionHeight()/2, reg.getRegionWidth()/2, reg.getRegionHeight()/2, reg.getRegionWidth(), reg.getRegionHeight(), scalex, scaley, rotation);
	}
	
	public static void crect(String name, float x, float y, float width, float height){
		TextureRegion reg = region(name);

		rend.batch.draw(reg, x, y,width, height);
	}
	
	public static void rect(TextureRegion reg, float x, float y, float rotation){
		
		x = overlapx(x, reg.getRegionWidth());
		y = overlapy(y, reg.getRegionHeight());
		
		rend.batch.draw(reg, x - reg.getRegionWidth()/2, y - reg.getRegionHeight()/2, reg.getRegionWidth()/2, reg.getRegionHeight()/2, reg.getRegionWidth(), reg.getRegionHeight(), 1f, 1f, rotation);
	}
	
	public static void line(float x, float y, float x2, float y2, float thickness){
		
		ShapeUtils.thickness = thickness;
		
		//yes that's a lot of draw calls
		//TODO fix this mess
		
		for(int cx = -1; cx <= 1; cx ++){
			for(int cy = -1; cy <= 1; cy ++){
				ShapeUtils.line(rend.batch, x + cx*World.worldSize, y + cy*World.worldSize, x2 + cx*World.worldSize, y2 + cy*World.worldSize);
			}
		}
	}
	
	public static void text(String text, float x, float y){
		text(text, x, y, Align.center);
	}
	
	public static void text(String text, float x, float y, int align){
		x = overlapx(x, 80);
		y = overlapy(y, 30);
		
		rend.font.draw(rend.batch, text, x, y, 0, align, false);
	}
	
	public static void color(String hex){
		rend.batch.setColor(Color.valueOf(hex));
	}
	
	public static void color(Color color){
		rend.batch.setColor(color);
	}
	
	public static void color(float r, float g, float b){
		rend.batch.setColor(clamp(r), clamp(g), clamp(b), 1f);
	}
	
	/**Lightness color.*/
	public static void colorl(float l){
		color(l, l, l);
	}
	
	/**Lightness color, alpha.*/
	public static void colorl(float l, float a){
		color(l, l, l, a);
	}
	
	public static void color(float r, float g, float b, float a){
		rend.batch.setColor(clamp(r), clamp(g), clamp(b), clamp(a));
	}
	
	/**Alpha color.*/
	public static void color(float a){
		rend.batch.setColor(1f, 1f, 1f, a);
	}
	
	public static void color(int rgba){
		rend.batch.setColor(temp.set(rgba).toFloatBits());
	}
	
	/**Resets the color to white.*/
	public static void color(){
		rend.batch.setColor(Color.WHITE);
	}
	
	public static void tcolor(Color color){
		rend.font.setColor(color);
	}
	
	public static void tcolor(){
		rend.font.setColor(Color.WHITE);
	}
	
	public static TextureRegion region(String name){
		return rend.atlas.findRegion(name);
	}
	
	public static Atlas atlas(){
		return rend.atlas;
	}
	
	public static Vector2 overlapxPair(float x1, float x2){
		
		float i = (x1 + x2)/2f;
		float r = Math.abs(x1 - x2);
		
		vector.set(x1, x2);
		
		if(MathUtils.isEqual(i, rend.camera.position.x, (rend.camera.viewportWidth * rend.camera.zoom) / 2f + 10f + r)){
			return vector;
		}else{
			if(i < World.worldSize / 2f){
				vector.set(x1 + World.worldSize, x2 + World.worldSize);
			}else{
				vector.set(x1 - World.worldSize, x2 - World.worldSize);
			}
			return vector;
		}
	}

	public static Vector2 overlapyPair(float y1, float y2){
		
		float i = (y1 + y2)/2f;
		float r = Math.abs(y1 - y2);
		
		vector.set(y1, y2);
		
		if(MathUtils.isEqual(i, rend.camera.position.y, (rend.camera.viewportHeight * rend.camera.zoom) / 2f + 10f + r)){
			return vector;
		}else{
			if(i < World.worldSize / 2f){
				vector.set(y1 + World.worldSize, y2 + World.worldSize);
			}else{
				vector.set(y1 - World.worldSize, y2 - World.worldSize);
			}
			return vector;
		}
	}
	
	public static float overlapx(float i, float r){
		if(MathUtils.isEqual(i, rend.camera.position.x, (rend.camera.viewportWidth * rend.camera.zoom) / 2f + 10f + r)){
			return i;
		}else{
			return i < World.worldSize / 2f ? i + World.worldSize : i - World.worldSize;
		}
	}

	public static float overlapy(float i, float r){
		if(MathUtils.isEqual(i, rend.camera.position.y, (rend.camera.viewportHeight * rend.camera.zoom) / 2f + 10 + r)){
			return i;
		}else{
			return i < World.worldSize / 2f ? i + World.worldSize : i - World.worldSize;
		}
	}
}
