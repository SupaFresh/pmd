package com.mygdx.pmd.utils.AI;

import com.badlogic.gdx.utils.Array;
import com.mygdx.pmd.exceptions.PathFindFailureException;
import com.mygdx.pmd.model.Entity.DynamicEntity;
import com.mygdx.pmd.model.Entity.Entity;
import com.mygdx.pmd.model.Tile.Tile;

/**
 * Created by Cameron on 11/11/2016.
 */
public abstract class PathFind {
    public DynamicEntity dEntity;
    public Tile[][] tileBoard;

    Array<Tile> openNodeList;
    Array<Tile> closedNodeList;
    Array<Tile> solutionNodeList;

    Tile currentTile;

    public PathFind(DynamicEntity dEntity){
        this.dEntity = dEntity;
        this.tileBoard = this.dEntity.tileBoard;
        this.currentTile = dEntity.currentTile;
        this.solutionNodeList = new Array<Tile>();
    }

    public Array<Tile> pathFind(Tile tile) throws PathFindFailureException{
        return null;
    }
}