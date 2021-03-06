package com.mygdx.pmd.utils.AI;

import com.badlogic.gdx.utils.Array;
import com.mygdx.pmd.exceptions.PathFindFailureException;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Tile.Tile;
import com.mygdx.pmd.model.components.*;


/**
 * Created by Cameron on 12/10/2016.
 */
public class Wander extends PathFind {

    public Wander(Entity entity) {
        super(entity);
    }

    @Override
    public Array<Tile> pathFind(Tile tile) throws PathFindFailureException {
        Tile currentTile = pc.getCurrentTile();
        Tile nextTile = this.chooseRandomTile(currentTile);

        while(!nextTile.isWalkable){
            nextTile = chooseRandomTile(currentTile);
        }

        if (mc.isLegalToMoveTo(nextTile)) {
            solutionNodeList.add(nextTile);
        }
        return solutionNodeList;
    }

    /**
     *
     * @param curTile
     * @return A non-null tile bordering curTile
     */
    public Tile chooseRandomTile(Tile curTile) {
        int rand = (int) (4 * Math.random()) + 1;

        int r = curTile.row;
        int c = curTile.col;

        try {
            switch (rand) {
                case 1:
                    return tileBoard[r][c + 1];
                case 2:
                    return tileBoard[r][c - 1];
                case 3:
                    return tileBoard[r + 1][c];
                case 4:
                    return tileBoard[r - 1][c];
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            return chooseRandomTile(curTile);
        }
        return null;
    }


}
