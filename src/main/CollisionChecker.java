package main;

import java.awt.Point;
import java.awt.Rectangle;

import entities.Bullet;
import entities.Entity;

public class CollisionChecker {

	GamePanel gp;
	
	public CollisionChecker(GamePanel gp) {
		this.gp = gp;
	}
	
	public void checkTile(Entity entity) {
		
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
                        entity.collision = true;
                    }
                }
		case "down" -> {
                    entityBotRow = (int) ((entityBotWorldY + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
                        entity.collision = true;
                    }
                }
		case "left" -> {
                    entityLeftCol = (int) ((entityLeftWorldX - entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityLeftCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityLeftCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
                        entity.collision = true;
                    }
                }
		case "right" -> {
                    entityRightCol = (int) ((entityRightWorldX + entity.speed) / gp.tileSize);
                    tileNum1 = gp.tileM.mapTileNumber[entityRightCol][entityTopRow];
                    tileNum2 = gp.tileM.mapTileNumber[entityRightCol][entityBotRow];
                    if(gp.tileM.tile[tileNum1].collision || gp.tileM.tile[tileNum2].collision)
                    {
                        entity.collision = true;
                    }
                }
		}
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
	
	@SuppressWarnings("unused")
	public void checkBulletHit(Bullet b, Entity entity) {
		if (entity != gp.player && entity != b &&
			b.worldX < entity.worldX + entity.hitbox.x + entity.hitbox.width &&
			b.worldX + b.width > entity.worldX + entity.hitbox.x &&
			b.worldY < entity.worldY + entity.hitbox.y + entity.hitbox.height&&
			b.worldY + b.height > entity.worldY - entity.hitbox.height)
		{
			System.out.println("Enemy hit");
			entity.health -= b.damage;
			b.die();


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
}
