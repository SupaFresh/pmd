package com.mygdx.pmd.model.Tile;


import com.mygdx.pmd.PMD;
import com.mygdx.pmd.model.Floor.Floor;

/**
 * Created by Cameron on 6/25/2016.
 */
public class RoomTile extends Tile {

    public RoomTile(int r, int c, Floor floor) {
        super(r, c, floor, "ROOM");
        this.isWalkable = true;
        rc.setSprite(PMD.sprites.get("bricktile"));

    }

    @Override
    public void runLogic() {

    }
}
