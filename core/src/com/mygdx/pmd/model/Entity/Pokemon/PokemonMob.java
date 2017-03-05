package com.mygdx.pmd.model.Entity.Pokemon;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.mygdx.pmd.controller.Controller;
import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.model.Behavior.Pokemon.PokeMob.MobLogic;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Floor.*;
import com.mygdx.pmd.model.Tile.Tile;
import com.mygdx.pmd.utils.AI.PathFind;
import com.mygdx.pmd.utils.AI.ShortestPath;
import com.mygdx.pmd.utils.AI.Wander;
import com.mygdx.pmd.utils.Constants;

public class PokemonMob extends Pokemon {
    public PathFind pathFind;

    public Wander wander;
    public ShortestPath sPath;
    public Array<Tile> path;

    public PokemonMob(Floor floor, PokemonName name){
        this(floor, 0, 0, name);
    }

    public PokemonMob(Floor floor, int x, int y, PokemonName pokemonName) {
        super(floor, x, y, pokemonName);
        this.aggression = Aggression.passive;
        this.target = floor.getPlayer();

        wander = new Wander(this);
        sPath = new ShortestPath(this);
        pathFind = wander;
        path = new Array<Tile>();
        logic = new MobLogic(this);
    }

    @Override
    public void render(SpriteBatch batch){
        super.render(batch);
        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(floor.getController().screen.getCamera().combined);
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.RED);
        for(Tile tile: path){
            renderer.rect(tile.x, tile.y, Constants.TILE_SIZE, Constants.TILE_SIZE);
        }
        renderer.end();
    }

    @Override
    public boolean isLegalToMoveTo(Tile tile) {
        if (tile == null) return false;

        if (tile.hasMovableEntity())
            return false;
        if (!tile.isWalkable)
            return false;
        return true;
    }

    public boolean isWithinRange(DynamicEntity pokemon) {
        int dR = this.getCurrentTile().row - pokemon.getCurrentTile().row;
        int dC = this.getCurrentTile().col - pokemon.getCurrentTile().col;

        if (dR * dR + dC * dC > 400) return false;
        return true;
    }

    public boolean canAttack() {
        return canSeeEnemy() && aggression == Aggression.aggressive;
    }

    public boolean canMove() {
        return true;
    }

    @Override
    public void takeDamage(Pokemon parent, int damage) {
        super.takeDamage(parent, damage);
        this.aggression = Aggression.aggressive;
        this.target = parent;
    }

    @Override
    public String toString() {
        return super.toString() + " mob";
    }

    @Override
    public void dispose() {
        super.dispose();

        this.setTurnState(Turn.COMPLETE);
        System.out.println("WOE IS ME I AM DEAD");
    }
}
