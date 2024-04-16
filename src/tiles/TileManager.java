package tiles;

import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Random;
import javax.imageio.ImageIO;

import main.GamePanel;

public class TileManager
{
	GamePanel gp;
	Random rd;
	public Tile[] tile;
	public int[][] mapTileNumber;
	public int tileNum;
	public int spriteCounter = 0;
	public int spriteNum = 1;

	public TileManager(GamePanel gp)
	{
		this.gp = gp;
		rd = new Random();
		tile = new Tile[1000];
		mapTileNumber = new int[100][50];
		getTileImage();
		loadMap("/maps/map01.txt");
	}

	private synchronized void getTileImage()
	{
		try {
			tile[100] = new Tile();
			tile[100].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass/Grass_1.png")));
			tile[101] = new Tile();
			tile[101].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass/Grass_2.png")));
			tile[102] = new Tile();
			tile[102].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/grass/Grass_3.png")));
			
			tile[200] = new Tile();
			tile[200].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt/Dirt_1.png")));
			tile[201] = new Tile();
			tile[201].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt/Dirt_2.png")));
			tile[202] = new Tile();
			tile[202].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirt/Dirt_3.png")));
			
			tile[300] = new Tile();
			tile[300].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/sand/Sand_1.png")));
			tile[301] = new Tile();
			tile[301].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/sand/Sand_2.png")));
			tile[302] = new Tile();
			tile[302].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/sand/Sand_3.png")));
			
			//tile[400] = new Tile();
			//tile[400].image = ImageIO.read(getClass().getResourceAsStream("/tiles/wall.png"));
			tile[700] = new Tile();
			tile[700].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water/Water_1_a1.png")));
			tile[700].collision = true;
			tile[701] = new Tile();
			tile[701].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/water/Water_1_a2.png")));
			tile[701].collision = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected synchronized void spriteCounter(int num)
	{
		spriteCounter++;
		int time = num;
		switch (num) {
			case 2 :
				time = 30;
				break;
			case 3 :
				time = 25;
				break;
			case 4 :
				time = 20;
				break;
			case 8 :
				time = 10;
				break;
		}

		if (spriteCounter > time) {
			spriteNum++;
			spriteCounter = 0;
		}

		if (spriteNum > num) {
			spriteNum = 1;
		}
	}

	private synchronized void loadMap(String filePath)
	{
		try
		{
			InputStream is = getClass().getResourceAsStream(filePath);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			int col = 0;
			int row = 0;

			while (col < gp.maxWorldCol && row < gp.maxWorldRow) {
				
				for (String line = br.readLine(); col < gp.maxWorldCol; col++) {
					String[] numbers = line.split(" ");
					int num = Integer.parseInt(numbers[col]);
					mapTileNumber[col][row] = num;
					if(num == 100)
					{
						switch(rd.nextInt(0,4)) 
						{
						default:
							mapTileNumber[col][row] += 1;
							break;
						case 3:
							mapTileNumber[col][row] += 0;
							break;
						case 4:
							mapTileNumber[col][row] += 2;
							break;
						}
					}
					
					if(num == 200)
					{
						switch(rd.nextInt(0,4)) 
						{
						default:
							mapTileNumber[col][row] += 0;
							break;
						case 2:
							mapTileNumber[col][row] += 1;
							break;
						case 3:
							mapTileNumber[col][row] += 2;
							break;
						}
					}
					
					if(num == 300)
					{
						switch(rd.nextInt(0,5)) 
						{
						default:
							mapTileNumber[col][row] += 0;
							break;
						case 3:
							mapTileNumber[col][row] += 1;
							break;
						case 4:
							mapTileNumber[col][row] += 2;
							break;
						}
					}
				}
				
				if (col == gp.maxWorldCol) {
					col = 0;
					row++;
				}
			}

			br.close();
		}
		catch (Exception e) {e.printStackTrace(); }
	}

	public synchronized void update()
	{
		spriteCounter(2);
	}
	
	public synchronized void draw(Graphics2D g2) {
		
		for (int row = 0; row < gp.maxWorldRow; row++)
		{
			for (int col = 0; col < gp.maxWorldCol; col++)
			{
				tileNum = mapTileNumber[col][row];
				
				double worldX = col * gp.tileSize;
				double worldY = row * gp.tileSize;
				int screenX = (int) (worldX - gp.player.worldX + gp.player.screenX);
				int screenY = (int) (worldY - gp.player.worldY + gp.player.screenY); 
				
				if (worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
					worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
					worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
					worldY - gp.tileSize < gp.player.worldY + gp.player.screenY)
				{
					switch (tileNum)
					{
						case 700 :
							if (spriteNum == 1) {
								tileNum = 700;
							}
							if (spriteNum == 2) {
								tileNum = 701;
							}
					}
					
					g2.drawImage(tile[tileNum].image, screenX, screenY,
							gp.tileSize, gp.tileSize, null);
				}
			}
		}
	}
}