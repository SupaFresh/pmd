package com.mygdx.pmd.Model.TileType;


import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.pmd.Controller.Controller;
import com.mygdx.pmd.Model.FloorComponent.Floor;
import com.mygdx.pmd.Screen.DungeonScreen;

/**
 * Created by Cameron on 7/3/2016.
 */
public class GenericTile extends Tile {

    public GenericTile(Floor floor, int row, int col) {
        super(floor, row, col);
        this.setWalkable(false);
        this.sprite = DungeonScreen.sprites.get("generictilesprite");
    }

    @Override
    public boolean isLegal() {
        int row = this.row;
        int col = this.col;

        boolean down = false;
        boolean up = false;
        boolean right = false;
        boolean left = false;

        if (row - 1 > 0) {
            if ((tileBoard[row - 1][col] instanceof RoomTile)) {
                up = true;
            }
        }

        if (col - 1 > 0) {
            if ((tileBoard[row][col - 1] instanceof RoomTile)) {
                left = true;
            }
        }

        if (col + 1 < Controller.windowCols) {
            if ((tileBoard[row][col + 1] instanceof RoomTile)) {
                right = true;
            }
        }

        if (row + 1 < Controller.windowRows) {
            if ((tileBoard[row + 1][col] instanceof RoomTile)) {
                down = true;
            }
        }


        if (right && down && up && left) {
            return false;
        } else return true;

    }


    public String getClassifier() {
        return "GenericTile";
    }
}
