package xyz.solodyankin;

import com.badlogic.gdx.maps.tiled.*;

import java.util.ArrayList;
import java.util.Random;

public class GameMap {

    TiledMap tiledMap;
    TiledMapTileLayer newLayer;
    float mapWidth;
    float mapHeight;

    TiledMapTileSet bushesTiles;
    TiledMapTile singleBush;
    ArrayList<TiledMapTile> horizontal;
    ArrayList<TiledMapTile> vertical;
    TiledMapTile hl;
    TiledMapTile hr;

    TiledMapTile vu;
    TiledMapTile vd;

    TiledMapTile utr;
    TiledMapTile utl;

    TiledMapTile dtr;
    TiledMapTile dtl;

    String[][] layerRepresentation;

    Random random;

    public GameMap(String mapName, float mapWidth, float mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
        tiledMap = new TmxMapLoader().load(mapName);
        layerRepresentation = new String[32][32];
        for(int i = 0; i < 32; i++) {
            for(int j = 0; j < 32; j++) {
                layerRepresentation[i][j] = "s";
            }
        }

        random = new Random();
        initTiles();
    }

    private void initTiles() {
        bushesTiles = tiledMap.getTileSets().getTileSet("Bushes");
        singleBush = bushesTiles.getTile(4);

        horizontal = new ArrayList<>();
        horizontal.add(bushesTiles.getTile(7));
        horizontal.add(bushesTiles.getTile(11));
        horizontal.add(bushesTiles.getTile(20));
        horizontal.add(bushesTiles.getTile(21));
        horizontal.add(bushesTiles.getTile(22));

        vertical = new ArrayList<>();
        vertical.add(bushesTiles.getTile(2));
        vertical.add(bushesTiles.getTile(19));
        vertical.add(bushesTiles.getTile(23));
        vertical.add(bushesTiles.getTile(24));
        vertical.add(bushesTiles.getTile(25));

        hl = bushesTiles.getTile(16);
        hr = bushesTiles.getTile(8);

        vu = bushesTiles.getTile(5);
        vd = bushesTiles.getTile(3);

        utl = bushesTiles.getTile(12);
        utr = bushesTiles.getTile(6);

        dtl = bushesTiles.getTile(18);
        dtr = bushesTiles.getTile(9);
    }

    void generateNewMapLayer() {
        newLayer = new TiledMapTileLayer((int) mapWidth, (int) mapHeight, 16, 16);

        for(int  i = 0; i < layerRepresentation.length; i++) {
            for(int j = 0; j < layerRepresentation.length; j++) {
                System.out.print(layerRepresentation[i][j]);
                TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                switch(layerRepresentation[i][j]) {
                    case "v":
                        cell.setTile(vertical.get(0));
                        newLayer.setCell(j,i, cell);
                        break;
                    case "vu":
                        cell.setTile(vd);
                        newLayer.setCell(j,i, cell);
                        break;
                    case "vd":
                        cell.setTile(vu);
                        newLayer.setCell(j,i, cell);
                        break;
                    case "h":
                        cell.setTile(horizontal.get(0));
                        newLayer.setCell(j,i, cell);
                        break;
                    case "hl":
                        cell.setTile(hl);
                        newLayer.setCell(j,i, cell);
                        break;
                    case "hr":
                        cell.setTile(hr);
                        newLayer.setCell(j,i, cell);
                        break;
                    case "dtr":
                        cell.setTile(dtr);
                        newLayer.setCell(j,i, cell);
                        break;
                }
            }
            System.out.println();
        }
        tiledMap.getLayers().add(newLayer);
    }

    void generateBushes() {
        int bushesNum = random.nextInt(5) + 10;

        for(int i = 0; i < bushesNum; i++) {
            int length = random.nextInt(5) + 3;
            int xStart = random.nextInt(31 - length);
            int yStart = random.nextInt(31 - length);

            int bushType = random.nextInt(3);

            if(bushType == 0) {
                genVertical(length, xStart, yStart);
            }

            if(bushType == 1) {
                genHorizontal(length, xStart, yStart);
            }

            if(bushType == 2) {
                genCorner(length, xStart, yStart);
            }
        }
    }

    private void genVertical(int length, int xStart, int yStart) {
        for(int i = xStart + 1, j = 0; j < length - 1; i++, j++) {
            layerRepresentation[i][yStart] = "v";
        }

        layerRepresentation[xStart][yStart] = "vu";
        layerRepresentation[xStart + length][yStart] = "vd";
    }

    private void genHorizontal(int length, int xStart, int yStart) {
        for(int i = yStart + 1, j = 0; j < length - 1; i++, j++) {
            layerRepresentation[xStart][i] = "h";
        }

        layerRepresentation[xStart][yStart] = "hl";
        layerRepresentation[xStart][yStart + length] = "hr";
    }

    private void genCorner(int length, int xStart, int yStart) {
        genVertical(length, xStart, yStart);
        genHorizontal(length, xStart, yStart);
        layerRepresentation[xStart][yStart] = "dtr";
    }
}
