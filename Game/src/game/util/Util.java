package game.util;

import engine.image.Images;
import engine.window.GamePanel;
import engine.window.gameAid.Utility;
import engine.window.gameAid.Window;
import game.GamePanelExtended;
import game.content.Loading;
import game.entity.block.Block;
import game.entity.inventory.IInventory;
import game.item.ItemStack;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Random;


public class Util {

	public static void decreaseStack(IInventory inv, int slot, int amount){

		inv.getStackInSlot(slot).stackSize-=amount;
		if(inv.getStackInSlot(slot).stackSize <= 0)
			inv.setStackInSlot(slot, null);
	}

	public static void drawToolTipWindow(Graphics2D g, int[] pos, List<String> toolTip){

		FontMetrics metrics = g.getFontMetrics(Constants.FONT_ITEMS);

		int h = (metrics.getHeight()) * toolTip.size();
		int w = 0;

		for(String text : toolTip){
			if(metrics.stringWidth(text)+10 > w)
				w = metrics.stringWidth(text)+10;
		}

		g.setColor(new Color(0.35f, 0.15f, 0.3f, 0.5f));
		g.fillRoundRect(pos[0], pos[1], w, h, 10, 10);

		g.setColor(new Color(0.7f, 0.7f, 0.8f, 0.9f));
		g.drawRoundRect(pos[0], pos[1], w, h, 10, 10);

	}

	public static void drawToolTipText(Graphics2D g, List<String> list, int pos[], Color top, Color shadow){
		g.setFont(Constants.FONT_ITEMS);

		if(top == null)
			top = new Color(1f, 1f, 0.95f);
		if(shadow == null)
			shadow = new Color(0.2f, 0.2f, 0.2f);

		for(int index = 0; index < list.size(); index++){
			Utility.drawStringWithShadow(list.get(index), pos[0] + 5, pos[1] + 11 + (index * 10), g, top, shadow);
		}
	}

	public static void drawToolTipText(Graphics2D g, ItemStack stack, int pos[]){

		if(stack != null){

			g.setFont(Constants.FONT_ITEMS);

			for(int index = 0; index < stack.getToolTip().size(); index++){
				if(index == 0) // item name
					Utility.drawStringWithShadow(stack.getToolTip().get(index), pos[0] + 5, pos[1] + 11 + (index * 10), g);
				else
					Utility.drawStringWithShadow(stack.getToolTip().get(index), pos[0] + 5, pos[1] + 14 + (index * 10), g, new Color(0.4f, 0.5f, 0.7f), new Color(0.2f, 0.2f, 0.5f));

			}
		}
	}

	private static Color top = new Color(0.4f, 0.7f, 0.3f);
	private static Color shadow = new Color(0.1f, 0.35f, 0.2f);

	public static void drawToolTipText(Graphics2D g, Block block, int pos[]){

		if(block != null){

			g.setFont(Constants.FONT_ITEMS);

			for(int index = 0; index < block.getBlockInfo().size(); index++){
				if(index == 0) // item name
					Utility.drawStringWithShadow(block.getBlockInfo().get(index), pos[0] + 5, pos[1] + 11 + (index * 10), g, top, shadow );
				else
					Utility.drawStringWithShadow(block.getBlockInfo().get(index), pos[0] + 5, pos[1] + 14 + (index * 10), g, top, shadow);
			}
		}
	}

	/**
	 * @return BufferedImage the size of the gameScreen
	 */
	public static BufferedImage generateStalactiteBackGround(){

		double width = Loading.stalactites[0].getWidth();
		double max = Window.getWidth() / width;

		BufferedImage img = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = img.createGraphics();

		g.setColor(Color.red);

		for(int i = 0; i < max; i++){
			BufferedImage theImg = Loading.stalactites[new Random().nextInt(Loading.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), 0, null);
		}

		for(int i = 0; i < max; i++){
			BufferedImage theImg = Loading.stalactites[new Random().nextInt(Loading.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), GamePanel.HEIGHT, theImg.getWidth(), -theImg.getHeight(),   null);
		}

		return img;
	}

	/**
	 * @return Bufferedimage with a height size of the tile map, and a screensize width.
	 * @param y : height of the tilemap
	 */
	public static BufferedImage generateGeneralBackground(){

		double width = 32;
		double maxX = (Window.getWidth() / width) + 2; //+2 is error margin
		double maxY = (Window.getHeight() / width) + 2; //+2 is error margin

		BufferedImage img = new BufferedImage(GamePanel.WIDTH, GamePanel.HEIGHT, BufferedImage.TYPE_INT_ARGB);

		BufferedImage tile = Images.loadImage("/background/cave_bg_tile.png");

		Graphics2D g = img.createGraphics();

		for(int j = 0; j < maxY; j++)
			for(int i = 0; i < maxX; i++){
				g.drawImage(tile, (int) (width*i), (int)(width*j), null);
			}

		return img;
	}

	/**
	 * @return Bufferedimage with a height size of the tile map, and a screensize width.
	 * @param y : height of the tilemap
	 * @param offset : texture offset to unmatch it with the background
	 */
	public static BufferedImage generateStalactiteBackGround(int y , int offset){

		double width = Loading.stalactites[0].getWidth();
		double max = Window.getWidth() / width;

		BufferedImage img = new BufferedImage(GamePanel.WIDTH, y, BufferedImage.TYPE_INT_ARGB);

		Graphics2D g = img.createGraphics();

		for(int i = 0; i < max; i++){
			BufferedImage theImg = Loading.stalactites[new Random().nextInt(Loading.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), 0, null);
		}

		for(int i = 0; i < max; i++){
			BufferedImage theImg = Loading.stalactites[new Random().nextInt(Loading.stalactites.length)];
			g.drawImage(theImg, (int) (width*i), y, theImg.getWidth(), -theImg.getHeight(),   null);
		}

		return img;
	}

	/**
	 * rotates a bufferedimage.
	 * 
	 * returns the rotated instance
	 * 
	 * image has to be square
	 */
	public static BufferedImage rotateImage(BufferedImage item, double rotation){
		return rotateImage(item, rotation, item.getWidth() + 20, 20/2);
	}

	public static BufferedImage rotateImage(BufferedImage item, double rotation, int size, int extra){

		//create blank canvas that is bigger the the image drawn.
		BufferedImage canvas = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);

		//get grapchics from the canvas
		Graphics2D g2d = (Graphics2D) canvas.getGraphics();

		//		g2d.setColor(new Color(0,0,1,0.5f));
		//		g2d.fillRect(0, 0, size, size);

		//rotate canvas internally
		if(rotation < 0)
			g2d.rotate(-Math.toRadians(rotation), size/2, size/2);
		else
			g2d.rotate(Math.toRadians(rotation), size/2, size/2);

		//		g2d.setColor(new Color(0,1,0,0.5f));
		//		g2d.fillRect(0, 0, size, size);
		//		
		//		g2d.drawRect(0, size/2, size, 1);
		//		g2d.drawRect(size/2, 0, 1, size);

		//draw image centered, extra/2
		g2d.drawImage(item, extra, extra, null);

		return canvas;
	}

	/**
	 * Colors an image with specified color.
	 * @param r Red value. Between 0 and 1
	 * @param g Green value. Between 0 and 1
	 * @param b Blue value. Between 0 and 1
	 * @param src The image to color
	 * @return The colored image
	 */
	//thanks stackoverflow's HardcodedCat
	public static BufferedImage color(float r, float g, float b, BufferedImage src) {

		// Copy image ( who made that so complicated :< )
		BufferedImage newImage = new BufferedImage(src.getWidth(), src.getHeight(), BufferedImage.TRANSLUCENT);
		Graphics2D graphics = newImage.createGraphics();
		graphics.drawImage(src, 0, 0, null);
		graphics.dispose();

		// Color image
		for (int i = 0; i < newImage.getWidth(); i++) {
			for (int j = 0; j < newImage.getHeight(); j++) {
				int ax = newImage.getColorModel().getAlpha(newImage.getRaster().getDataElements(i, j, null));
				int rx = newImage.getColorModel().getRed(newImage.getRaster().getDataElements(i, j, null));
				int gx = newImage.getColorModel().getGreen(newImage.getRaster().getDataElements(i, j, null));
				int bx = newImage.getColorModel().getBlue(newImage.getRaster().getDataElements(i, j, null));
				rx *= r;
				gx *= g;
				bx *= b;
				newImage.setRGB(i, j, (ax << 24) | (rx << 16) | (gx << 8) | (bx << 0));
			}
		}
		return newImage;
	}

	public static void startLoadIcon(){
		GamePanelExtended.drawLoadingIcon = true;
	}

	public static void stopLoadIcon(){
		GamePanelExtended.drawLoadingIcon = false;
	}


}
