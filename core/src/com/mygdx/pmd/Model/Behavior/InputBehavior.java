package com.mygdx.pmd.Model.Behavior;

import com.mygdx.pmd.Enumerations.Direction;
import com.mygdx.pmd.Enumerations.Key;
import com.mygdx.pmd.Enumerations.Turn;
import com.mygdx.pmd.Model.Pokemon.Pokemon;
import com.mygdx.pmd.Model.TileType.GenericTile;

/**
 * Created by Cameron on 11/8/2016.
 */
public class InputBehavior extends Behavior {

    public InputBehavior(Pokemon pokemon) {
        super(pokemon);
    }

    @Override
    public void execute() {
        if (pokemon.equals(pokemon.currentTile) && pokemon.turnState == Turn.WAITING) {
            try {
                if (controller.isKeyPressed(Key.down) && controller.isKeyPressed(Key.right)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row - 1][pokemon.currentTile.col + 1]);
                    pokemon.direction = Direction.downright;
                } else if (controller.isKeyPressed(Key.up) && controller.isKeyPressed(Key.right)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row + 1][pokemon.currentTile.col + 1]);
                    pokemon.direction = Direction.upright;
                } else if (controller.isKeyPressed(Key.up) && controller.isKeyPressed(Key.left)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row + 1][pokemon.currentTile.col - 1]);
                    pokemon.direction = Direction.upleft;
                } else if (controller.isKeyPressed(Key.down) && controller.isKeyPressed(Key.left)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row - 1][pokemon.currentTile.col - 1]);
                    pokemon.direction = Direction.downleft;
                } else if (controller.isKeyPressed(Key.down)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row - 1][pokemon.currentTile.col]);
                    pokemon.direction = Direction.down;
                } else if (controller.isKeyPressed(Key.left)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row][pokemon.currentTile.col - 1]);
                    pokemon.direction = Direction.left;
                } else if (controller.isKeyPressed(Key.right)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row][pokemon.currentTile.col + 1]);
                    pokemon.direction = Direction.right;
                } else if (controller.isKeyPressed(Key.up)) {
                    pokemon.setNextTile(tileBoard[pokemon.currentTile.row + 1][pokemon.currentTile.col]);
                    pokemon.direction = Direction.up;
                }
                else pokemon.setNextTile(null);
            } catch (ArrayIndexOutOfBoundsException e) {
            }

            if(!pokemon.isLegalToMoveTo(pokemon.nextTile)){
                pokemon.nextTile = null;
            }


            if (controller.isKeyPressed(Key.space)) {
                controller.getCurrentFloor().getFloorGenerator().generateFloor();
                controller.getCurrentFloor().getFloorGenerator().controller.randomizeAllPokemonLocation();
            }
            if (controller.isKeyPressed(Key.a)) {
                pokemon.turnState = Turn.COMPLETE;
                pokemon.nextTile = null;
            }

           /* if (controller.isKeyPressed(Key.a) && controller.isKeyPressed(Key.s) && this.turnBehavior.isTurnComplete()) {
                this.turnBehavior.setTurnState(Turn.COMPLETE);
            }*/

            if (controller.isKeyPressed(Key.b)) {
                controller.controllerScreen.switchMenus("defaultMenu");
            }
        }
    }
}
