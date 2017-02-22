package com.mygdx.pmd.model.Behavior.Pokemon.PokePlayer;

import com.mygdx.pmd.PMD;
import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.model.Behavior.*;
import com.mygdx.pmd.model.Behavior.Pokemon.PokemonBehavior;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Entity.Pokemon.PokemonPlayer;
import com.mygdx.pmd.utils.PUtils;

/**
 * Created by Cameron on 1/21/2017.
 */
public class PlayerLogic extends PokemonBehavior {
    private PokemonPlayer player;

    public PlayerLogic(PokemonPlayer player) {
        super(player);
        this.player = player;
    }

    @Override
    public void execute() {
        if(player.finishedInstructionsExecution() && player.getActionState() != Action.IDLE){
            player.setActionState(Action.IDLE);
        }

        if (player.getHP() <= 0) {
            player.shouldBeDestroyed = true;
        }

        if (player.getTurnState() == Turn.WAITING && player.equals(player.getCurrentTile())) {
            player.handleInput();

            if (player.canAttack()) {
                player.attack(player.currentMove);
                player.currentMove = null;

                player.setActionState(Action.ATTACKING);
                player.setTurnState(Turn.PENDING);

                player.behaviors[2] = player.attackBehavior;
            } else if (player.canMove()) {
                player.setNextTile(player.possibleNextTile);
                player.possibleNextTile = null;

                if (player.getNextTile().hasMovableEntity()) {
                    for (DynamicEntity dEntity : PUtils.getObjectsOfType(DynamicEntity.class, player.getNextTile().getEntityList())) {
                        if (dEntity != player) {
                            dEntity.forceMoveToTile(player.getCurrentTile());
                            dEntity.setDirection(player.getDirection().getOppositeDirection());
                        }
                    }
                }

                //player.behaviors[2] = player.moveBehavior;
                player.instructions.add(new MoveInstruction(player, player.getNextTile()));
                player.instructions.add(new MoveInstruction(player,player.getCurrentTile()));


                player.setTurnState(Turn.COMPLETE);
                player.setActionState(Action.MOVING);
                if (PMD.isKeyPressed(Key.s)) {
                    player.setSpeed(5);
                } else player.setSpeed(1);
            }

        } else if (player.getActionState() == Action.IDLE) {
            System.out.println(player.getTurnState());
            System.out.println(player.getActionState());
        }
    }
}
