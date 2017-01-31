package io.anuke.novi.modules;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.Input.Keys;

import io.anuke.novi.Novi;
import io.anuke.novi.entities.base.Player;
import io.anuke.novi.network.packets.InputPacket;
import io.anuke.novi.utils.InputType;
import io.anuke.ucore.modules.Module;

public class Input extends Module<Novi>{
	Player player;

	public void init(){
		player = getModule(ClientData.class).player;
		Gdx.input.setInputProcessor(this);
	}

	@Override
	public void update(){
		if(Gdx.input.isKeyPressed(Keys.ESCAPE)){
			Gdx.app.exit();
		}
		
		if(player.isDead()) return;
		
		float angle = -9;
		if(up()) angle = 90;
		if(left()) angle = 180;
		if(down()) angle = 270;
		if(right()) angle = 0;
		if(up() && right()) angle = 45;
		if(up() && left()) angle = 135;
		if(down() && right()) angle = 315;
		if(down() && left()) angle = 225;
		if(angle > -1) player.move(angle);
		if(Gdx.input.isButtonPressed(Buttons.LEFT) && !Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)){
			player.shooting = true;
		}else{
			player.shooting = false;
		}
	}

	boolean left(){
		return Gdx.input.isKeyPressed(Keys.A);
	}

	boolean right(){
		return Gdx.input.isKeyPressed(Keys.D) || Gdx.input.isKeyPressed(Keys.E);
	}

	boolean up(){
		return Gdx.input.isKeyPressed(Keys.W) || Gdx.input.isKeyPressed(Keys.COMMA);
	}

	boolean down(){
		return Gdx.input.isKeyPressed(Keys.S) || Gdx.input.isKeyPressed(Keys.O);
	}

	void sendInput(InputType type){
		InputPacket input = new InputPacket();
		input.input = type;
		getModule(Network.class).client.sendTCP(input);
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button){
		//new BreakEffect("titanship").set(player.x+30,player.y+30).add();
		player.rotation = player.velocity.angle();
		player.valigned = false;
		sendInput(button == Buttons.LEFT ? InputType.LEFT_CLICK_DOWN : InputType.RIGHT_CLICK_DOWN);
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button){
		sendInput(button == Buttons.LEFT ? InputType.LEFT_CLICK_UP : InputType.RIGHT_CLICK_UP);
		if(player.velocity.isZero(0.01f)){
			player.velocity.x = 0.01f;
			player.velocity.setAngle(player.rotation);
		}
		return false;
	}

	@Override
	public boolean scrolled(int amount){
		getModule(Renderer.class).zoom(amount / 10f);
		return false;
	}

	float ForwardDistance(float angle1, float angle2){
		if(angle1 > angle2){
			return angle1 - angle2;
		}else{
			return angle2 - angle1;
		}
	}

}
