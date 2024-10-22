package main;

import entities.Bullet;
import entities.Entity;

public class CollisionChecker {

	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public boolean checkTile(Entity entity) {
		
		double entityLeftWorldX = entity.worldX + entity.hitbox.x;
		double entityRightWorldX = entity.worldX + entity.hitbox.x + entity.hitbox.width;
		double entityTopWorldY = entity.worldY + entity.hitbox.y;
		double entityBotWorldY = entity.worldY + entity.hitbox.y + entity.hitbox.height;
		
		int entityLeftCol = (int) (entityLeftWorldX / gp.tileSize);
		int entityRightCol = (int) (entityRightWorldX / gp.tileSize);
		int entityTopRow = (int) (entityTopWorldY / gp.tileSize);
		int entityBotRow = (int) (entityBotWorldY / gp.tileSize);
		
		int tileNum1, tileNum2;
		
		switch(entity.direction) {
		case "up" -> {
                    entityTopRow = (int) ((entityTopWorldY - entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
						return true;
                    }
                }
		case "down" -> {
                    entityBotRow = (int) ((entityBotWorldY + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
						return true;
                    }
                }
		case "left" -> {
                    entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
						return true;
                    }
                }
		case "right" -> {
                    entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
						return true;
                    }
                }
		case "upleft" -> {
				entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
				entityTopRow = (int) ((entityTopWorldY - entity.speed) / gp.tileSize);
				tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
				tileNum2 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
				if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
				{
					return true;
				}
			}
		case "upright" -> {
			entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
			entityTopRow = (int) ((entityTopWorldY - entity.speed) / gp.tileSize);
			tileNum1 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
			tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
			{
				return true;
			}
		}
		case "downleft" -> {
			entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
			entityBotRow = (int) ((entityBotWorldY + entity.speed) / gp.tileSize);
			tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
			tileNum2 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
			if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
			{
				return true;
			}
		}
		case "downright" -> {
				entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
				entityBotRow = (int) ((entityBotWorldY + entity.speed) / gp.tileSize);
				tileNum1 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
				tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
				if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
				{
					return true;
				}
			}
		}
		return false;
	}

	public boolean checkEntities(Entity entity){
		for(Entity e : gp.entityManager.entityList) {
			// Skip checking collision with itself
			if(entity == e) continue;

			//Calculate position after moving
			double nextX = entity.worldX;
			double nextY = entity.worldY;

			switch(entity.direction) {
				case "up" -> nextY -= entity.speed;
				case "down" -> nextY += entity.speed;
				case "left" -> nextX -= entity.speed;
				case "right" -> nextX += entity.speed;
				case "upleft" -> {
					nextX -= entity.speed;
					nextY -= entity.speed;
				}
				case "upright" -> {
					nextX += entity.speed;
					nextY -= entity.speed;
				}
				case "downleft" -> {
					nextX -= entity.speed;
					nextY += entity.speed;
				}
				case "downright" -> {
					nextX += entity.speed;
					nextY += entity.speed;
				}
			}

			// Check if the hitboxes overlap
			if(nextX + entity.hitbox.x < e.worldX + e.hitbox.x + e.hitbox.width &&
					nextX + entity.hitbox.x + entity.hitbox.width > e.worldX + e.hitbox.x &&
					nextY + entity.hitbox.y < e.worldY + e.hitbox.y + e.hitbox.height &&
					nextY + entity.hitbox.y + entity.hitbox.height > e.worldY + e.hitbox.y &&
				e.collidable)
			{
				return true;
			}
		}
		return false;
	}

	public void checkDamage(Entity entity1, Entity entity2) {
			if (entity1 != entity2 &&
				entity1.worldX < entity2.worldX + entity2.hitbox.x + entity2.hitbox.width &&
				entity1.worldX + entity1.width > entity2.worldX + entity2.hitbox.x &&
				entity1.worldY < entity2.worldY + entity2.hitbox.y + entity2.hitbox.height&&
				entity1.worldY + entity1.height > entity2.worldY - entity2.hitbox.height)
			{
				System.out.println("Enemy hit");
				entity2.health -= entity1.damage;
				
				/*
				Game.infoBox(
						"explosionX: " + gp.getMousePosOnMap().x +
						"   enemyX: " + entity2.worldX +
						"\nexplosionY: " + gp.getMousePosOnMap().y +
						"   enemyY: " + entity2.worldY,
						"Mouse and Enemy Position");
				*/
			}
	}

	public void checkBulletHit(Bullet b, Entity entity) {
		if (entity != gp.player && entity != b &&
			b.worldX < entity.worldX + entity.hitbox.x + entity.hitbox.width &&
			b.worldX + b.width > entity.worldX + entity.hitbox.x &&
			b.worldY < entity.worldY + entity.hitbox.y + entity.hitbox.height&&
			b.worldY + b.height > entity.worldY - entity.hitbox.height)
		{
			System.out.println("Enemy hit");
			entity.takeDamage(b.damage);
			b.die();
		}
	}
}
