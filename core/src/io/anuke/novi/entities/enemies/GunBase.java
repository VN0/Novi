package io.anuke.novi.entities.enemies;

import io.anuke.novi.world.Material;

public class GunBase extends Base{

	@Override
	void generateBlocks(){
		for(int x = 0;x < size;x ++){
			for(int y = 0;y < size;y ++){
				blocks[x][y].setMaterial(Material.ironblock);
				
				if(Math.random() < 0.1) blocks[x][y].setMaterial(Material.turret);
				if(Math.random() < 0.1) blocks[x][y].setMaterial(Material.bigturret);
				
				if(Math.random() < 0.2) blocks[x][y].setMaterial(Material.dronemaker);
			}
		}
		
	}
}