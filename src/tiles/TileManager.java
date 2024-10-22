package tiles;

import java.awt.*;
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

	private void getTileImage()
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

			tile[210] = new Tile();
			tile[210].image = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/tiles/dirtpath/Dirtpatht_1.png")));

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
			System.out.println("Success: Successfully loaded texture resources");
		} catch (IOException e) {
			System.out.println("Error: Couldn't load texture resources");
		}
	}

	protected void spriteCounter(int num)
	{
		spriteCounter++;
		int time = switch (num)
		{
            case 2 -> 30;
            case 3 -> 25;
            case 4 -> 20;
            case 8 -> 10;
            default -> 0;
        };

        if (spriteCounter > time) {
			spriteNum++;
			spriteCounter = 0;
		}

		if (spriteNum > num) {
			spriteNum = 1;
		}
	}

	private void loadMap(String filePath)
	{
		try
		{
			InputStream is = getClass().getResourceAsStream(filePath);
            assert is != null;
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
                            case 3:
							mapTileNumber[col][row] += 0;
							break;
						case 4:
							mapTileNumber[col][row] += 2;
							break;
                            default:
                                mapTileNumber[col][row] += 1;
                                break;
                        }
					}

					if(num == 200)
					{
						try {
							if (mapTileNumber[col][row + 1] >= 100 && mapTileNumber[col][row + 1] < 200) {
								switch (mapTileNumber[col][row + 1]) {
									case 200:
										mapTileNumber[col][row] = 211;
										break;
								}
							}
						} catch (Exception ignored){ }

						switch (rd.nextInt(0, 4))
						{
                            case 2:
								mapTileNumber[col][row] += 1;
								break;
							case 3:
								mapTileNumber[col][row] += 2;
								break;
                            default:
                                mapTileNumber[col][row] += 0;
                                break;
                        }
					}

					if(num == 300)
					{
						switch(rd.nextInt(0,5))
						{
                            case 3:
							mapTileNumber[col][row] += 1;
							break;
						case 4:
							mapTileNumber[col][row] += 2;
							break;
                            default:
                                mapTileNumber[col][row] += 0;
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

	void update()
	{
		spriteCounter(2);
	}

	public void draw(Graphics2D g2) {
		// Disable antialiasing for pixel-perfect rendering
		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
		g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_SPEED);
		g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

		// Calculate the offset for centering
		int offsetX = gp.screenWidth / 2 - gp.tileSize / 2;
		int offsetY = gp.screenHeight / 2 - gp.tileSize / 2;

		// Calculate the player's position in tile coordinates
		int playerWorldCol = (int) Math.floor(gp.player.worldX / gp.tileSize);
		int playerWorldRow = (int) Math.floor(gp.player.worldY / gp.tileSize);

		// Calculate the number of tiles that can fit on the screen
		int tilesAcross = gp.screenWidth / gp.tileSize + 2;
		int tilesDown = gp.screenHeight / gp.tileSize + 2;

		// Calculate the starting and ending columns and rows to render
		int startCol = playerWorldCol - tilesAcross / 2;
		int endCol = startCol + tilesAcross;
		int startRow = playerWorldRow - tilesDown / 2;
		int endRow = startRow + tilesDown;

		// Adjust for world boundaries
		startCol = Math.max(0, startCol);
		endCol = Math.min(gp.maxWorldCol - 1, endCol);
		startRow = Math.max(0, startRow);
		endRow = Math.min(gp.maxWorldRow - 1, endRow);

		// Calculate the pixel offset within the current tile
		int xOffset = (int) (gp.player.worldX % gp.tileSize);
		int yOffset = (int) (gp.player.worldY % gp.tileSize);

		for (int worldRow = startRow; worldRow <= endRow; worldRow++) {
			for (int worldCol = startCol; worldCol <= endCol; worldCol++) {
				int tileNum = mapTileNumber[worldCol][worldRow];

				int screenX = (worldCol - playerWorldCol) * gp.tileSize + offsetX - xOffset;
				int screenY = (worldRow - playerWorldRow) * gp.tileSize + offsetY - yOffset;

				// Handle animated tiles
				if (tileNum == 700) {
					tileNum = (spriteNum == 1) ? 700 : 701;
				}

				// Draw the tile
				g2.drawImage(tile[tileNum].image, screenX, screenY, gp.tileSize, gp.tileSize, null);
			}
		}
	}
}