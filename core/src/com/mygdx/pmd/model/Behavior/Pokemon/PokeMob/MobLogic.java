package com.mygdx.pmd.model.Behavior.Pokemon.PokeMob;

import com.mygdx.pmd.PMD;
import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.exceptions.PathFindFailureException;
import com.mygdx.pmd.model.Behavior.*;
import com.mygdx.pmd.model.Behavior.Pokemon.*;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Entity.Pokemon.PokemonMob;

/**
 * Created by Cameron on 1/20/2017.
 */
public class MobLogic implements Logic {
    private PokemonMob mob;

    public MobLogic(PokemonMob mob) {
        this.mob = mob;
    }

    @Override
    public void execute() {
        if (mob.getHP() <= 0) {
            mob.shouldBeDestroyed = true;
        }

        if (mob.shouldBeDestroyed) {
            return;
        }

        //ensure that when this runs the pokemon's turn is always waiting
        if (canAct()) {
            //will turn to face the player if the mob is aggressive
            if (mob.isAggressive()) {
                mob.setDirection(mob.target.getCurrentTile());
                mob.setFacingTile(mob.getDirection());

                if (mob.target.shouldBeDestroyed) {
                    mob.target = mob.floor.getPlayer();
                    mob.aggression = Aggression.passive;
                    mob.pathFind = mob.wander;
                }
            }

            if (mob.canAttack()) {
                mob.instructions.add(new AttackInstruction(mob));

                mob.setTurnState(Turn.PENDING);
                mob.setActionState(Action.ATTACKING);

            } else if (mob.canMove()) {
                // set the next tile based on if the mob has been forced to move or not
                if (mob.isForcedMove) {
                    System.out.println("forced move");
                    mob.setSpeed(1);
                    mob.isForcedMove = false;
                } else {
                    if (mob.isAggressive()) {
                        mob.pathFind = mob.sPath;
                    }
                    //see if it can pathfind, meaning there was no error thrown
                    if (pathFind()) {
                        this.mob.setFacingTile(mob.possibleNextTile);
                        this.mob.setDirection(mob.possibleNextTile);

                        if (this.mob.isLegalToMoveTo(this.mob.possibleNextTile)) {
                            this.mob.setNextTile(this.mob.possibleNextTile);
                            this.mob.possibleNextTile = null;
                        }

                        this.determineSpeed();
                    }
                }

                if (PMD.isKeyPressed(Key.s)) {
                    mob.setSpeed(5);
                }

                //tell the mob to go to to the next tile
                mob.instructions.add(new MoveInstruction(mob, mob.getNextTile()));
                mob.setTurnState(Turn.COMPLETE);
            }
        }
    }

    private void determineSpeed() {
        if (mob.isWithinRange(mob.floor.getPlayer())) {
            mob.setSpeed(1);
        } else {
            mob.setSpeed(25);
        }
    }

    private boolean pathFind() {
        try {
            mob.path = mob.pathFind.pathFind(mob.target.getNextTile());
        } catch (PathFindFailureException e) {
            System.out.println("Failed to pathfind");
        }

        if (mob.path.size <= 0) {
            return false;
        }

        this.mob.possibleNextTile = mob.path.first();
        mob.path.removeValue(this.mob.possibleNextTile, true);

        return true;
    }

    private boolean canAct() {
        return mob.getTurnState() == Turn.WAITING && mob.getActionState() == Action.IDLE && mob.instructions.isEmpty() && mob.currentInstruction == Entity.NO_INSTRUCTION;
    }
}
