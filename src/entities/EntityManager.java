package entities;

import java.awt.*;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;

import main.GamePanel;
import main.KeyHandler;

public class EntityManager {
	GamePanel gp;
	KeyHandler keyH;
	public List<Entity> entityList;
	int randomCounter;
	int randomIntervalInSeconds;
	int state;
	boolean active;

	public EntityManager(GamePanel gp, KeyHandler keyH) {
		this.gp = gp;
		this.keyH = keyH;
		this.entityList = new CopyOnWriteArrayList<>(); // Thread-sichere Liste verwenden
		entityList.add(gp.player);
		entityList.add(gp.yoshi);
		randomIntervalInSeconds = 5;
		state = 0;
		active = true;
	}

	public void newRandomState() {
		Random rnd = new Random();
		switch (rnd.nextInt(1, 1)) {
			case 1:
				state = 1;
				fourEnemysEncirclePlayer();
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				break;
		}
	}

	public void fourEnemysEncirclePlayer() {
		double x = gp.player.worldX;
		double y = gp.player.worldY;
		int spawnDistance = 3;
		Enemy e1 = new Enemy(x + gp.tileSize * spawnDistance, y + gp.tileSize * spawnDistance, "greyGhost", gp, keyH);
		entityList.add(e1);
		Enemy e2 = new Enemy(x + gp.tileSize * spawnDistance, y - gp.tileSize * spawnDistance, "greyGhost", gp, keyH);
		entityList.add(e2);
		Enemy e3 = new Enemy(x - gp.tileSize * spawnDistance, y + gp.tileSize * spawnDistance, "greyGhost", gp, keyH);
		entityList.add(e3);
		Enemy e4 = new Enemy(x - gp.tileSize * spawnDistance, y - gp.tileSize * spawnDistance, "greyGhost", gp, keyH);
		entityList.add(e4);
		active = false;
	}

	private void drawOrderBubbleSort() {
		List<Entity> entityArrayList = new CopyOnWriteArrayList<>(entityList);
		boolean swapped = true;
		while (swapped) {
			swapped = false;
			for (int i = 0; i < entityArrayList.size() - 1; i++) {
				Entity lowerPriority = entityArrayList.get(i);
				Entity higherPriority = entityArrayList.get(i + 1);
				if (lowerPriority.worldY + lowerPriority.height > higherPriority.worldY + higherPriority.height) {
					entityArrayList.set(i, higherPriority);
					entityArrayList.set(i + 1, lowerPriority);
					swapped = true;
				}
			}
		}
		entityList = new CopyOnWriteArrayList<>(entityArrayList);
	}

	public void update() {
		if (active) {
			randomCounter++;
			if (randomCounter > gp.tickRate * randomIntervalInSeconds) {
				fourEnemysEncirclePlayer();
				randomCounter = 0;
			}
		}

		int aliveCounter = 0;
		for (Entity e : entityList) {
			aliveCounter++;
		}

		if (aliveCounter <= 2) {
			active = true;
		}

		for (Entity entity : entityList) {
			entity.update();
		}

		drawOrderBubbleSort();
	}

	public void draw(Graphics2D g2) {
		switch (gp.gamestate) {
			case 1, 3, 31 -> entityList.forEach(entity -> {
				entity.draw(g2);
				if (entity.isHostile) {
					entity.drawHealthbar(entity, g2);
				}
			});
			case 2 -> entityList.forEach(entity -> {
				entity.draw(g2);
				entity.drawHealthbar(entity, g2);
				g2.setColor(Color.red);
				g2.setStroke(new BasicStroke(3));
				g2.drawRect(entity.screenX + entity.hitbox.x, entity.screenY + entity.hitbox.y,
						entity.hitbox.width, entity.hitbox.height);
			});
		}
	}
}