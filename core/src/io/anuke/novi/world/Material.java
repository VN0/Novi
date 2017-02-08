package io.anuke.novi.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import io.anuke.novi.Novi;
import io.anuke.novi.effects.EffectType;
import io.anuke.novi.effects.Effects;
import io.anuke.novi.entities.enemies.Base;
import io.anuke.novi.entities.enemies.Drone;
import io.anuke.novi.items.ProjectileType;
import io.anuke.novi.utils.Draw;

public enum Material{
	air, 
	ironblock{
		public boolean solid(){
			return false;
		}
	},
	frame{
		public boolean solid(){
			return false;
		}
	},
	dronemaker{
		static final float buildtime = 60;
		static final int maxspawn = 20;

		public boolean solid(){
			return true;
		}
		
		public boolean drawTop(){
			return false;
		}

		public void draw(Block block, Base base, int x, int y){
			defaultDraw("dronemaker", block, base, x, y, false);
			
			//defaultDraw("drone", block, base, x, y, false).setColor(new Color(1, 1, 1, block.reload / buildtime)).rotate(180f).setLayer( -0.45f).translate(0, -1);
			
			
			//Draw.color();
			//Draw.rect("drone", worldx(base, x, y), worldy(base, x, y), base.rotation+180);
			
			//Layer bar = defaultDraw("dronemakerbar", block, base, x, y, false).setLayer( -0.4f);
			
			//Vector2 v = Angles.translation(base.rotation + 90f, (int)(Math.sin(Gdx.graphics.getFrameId() / 10f) * 6f - 1f));
			//bar.translate(v.x, v.y);
			//bar.translate(bar.x, bar.y + (int)(Math.sin(Gdx.graphics.getFrameId() / 10f) * 6f - 1f));
		}

		public void update(Block block, Base base){
			if(base.target == null || base.spawned > maxspawn) return;
			block.reload += Novi.delta();
			base.updateBlock(block.x, block.y);
			if(block.reload >= buildtime){
				Drone drone = (Drone)new Drone().set(worldx(base, block.x, block.y), worldy(base, block.x, block.y));
				drone.velocity.y = -3;
				drone.add().send();
				drone.base = base;
				block.reload = 0;
				base.spawned ++;
			}
		}

		public int health(){
			return 60;
		}
	},
	turret{
		static final float reloadtime = 40;

		public boolean solid(){
			return true;
		}

		public void update(Block block, Base base){
			if(base.target != null){
				block.rotation = base.autoPredictTargetAngle(worldx(base, block.x, block.y), worldy(base, block.x, block.y), 4f) + 90;
				base.updateBlock(block.x, block.y);
				block.reload += Novi.delta();
				
				if(block.reload >= reloadtime){
					
					base.getShoot(ProjectileType.redbullet, block.rotation + 90).set(worldx(base, block.x, block.y), worldy(base, block.x, block.y)).translate(3, 5).add().send();;
					base.getShoot(ProjectileType.redbullet, block.rotation + 90).set(worldx(base, block.x, block.y), worldy(base, block.x, block.y)).translate( -3, 5).add().send();;

					block.reload = 0;
				}
			}
		}

		public void draw(Block block, Base base, int x, int y){
			defaultDraw("turret", block, base, x, y, block.rotation);
		}

		public int health(){
			return 40;
		}
	},
	bigturret{
		static final float reloadtime = 100;

		public boolean solid(){
			return true;
		}

		public void update(Block block, Base base){
			if(base.target != null){
				block.rotation = MathUtils.lerpAngleDeg(block.rotation, base.autoPredictTargetAngle(worldx(base, block.x, block.y), worldy(base, block.x, block.y), 3f) + 90, 0.02f);
				base.updateBlock(block.x, block.y);
				block.reload += Novi.delta();
				
				if(block.reload >= reloadtime){
					base.getShoot(ProjectileType.explosivebullet, block.rotation + 90).set(worldx(base, block.x, block.y), worldy(base, block.x, block.y)).translate(0, 7).add().send();;
					block.reload = 0;
				}
			}
		}

		public void draw(Block block, Base base, int x, int y){
			//Vector2 vector = Angles.translation(block.rotation - 90, (1f - block.reload / reloadtime) * 4f);
			
			Draw.rect(name(), worldx(base, x, y), worldy(base, x, y), block.rotation);
		}

		public int health(){
			return 100;
		}
	};

	public void update(Block block, Base base){

	}

	static public final int blocksize = 16;

	public void destroyEvent(Base base, int x, int y){
		float wx = worldx(base, x, y), wy = worldy(base, x, y);
		
		Effects.effect(EffectType.shockwave, wx, wy);
		Effects.effect(EffectType.explosion, wx, wy);
		Effects.blockbreak(name(), wx, wy, 2.5f, base.velocity);
		
		base.blocks[x][y].setMaterial(Material.frame);
		
		Effects.shake(30f, 10f, worldx(base, x, y), worldy(base, x, y));
	}
	
	public void draw(Block block, Base base, int x, int y){
		
		Draw.colorl(block.healthfrac()/2f + 0.5f);
		
		defaultDraw(name(), block, base, x, y);
		
		Draw.color();
	}

	public void defaultDraw(String region, Block block, Base base, int x, int y){
		Draw.rect(region, worldx(base, x, y), worldy(base, x, y), base.rotation);
	}
	
	public void defaultDraw(String region, Block block, Base base, int x, int y, float rotation){
		Draw.rect(region, worldx(base, x, y), worldy(base, x, y), rotation);
	}

	public void defaultDraw(String region, Block block, Base base, int x, int y, float offsetx, float offsety){
		Draw.rect(region, worldx(base, x, y) + offsetx, worldy(base, x, y) + offsety, base.rotation);
	}

	public void defaultDraw(String region, Block block, Base base, int x, int y, boolean damage){
		
		if(damage) Draw.color(block.healthfrac() + 0.3f, block.healthfrac() + 0.3f, block.healthfrac() + 0.3f);
		defaultDraw(region, block, base, x, y);
		if(damage) Draw.color();
	}

	float worldx(Base base, int x, int y){
		Vector2 v = base.world(x, y);
		return v.x;
	}

	float worldy(Base base, int x, int y){
		Vector2 v = base.world(x, y);
		return v.y;
	}

	public boolean solid(){
		return false;
	}
	
	public boolean drawTop(){
		return solid();
	}

	public int health(){
		return 10;
	}
}
