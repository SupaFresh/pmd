package com.mygdx.pmd.model.logic;

import com.mygdx.pmd.PMD;
import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.exceptions.PathFindFailureException;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Entity.Pokemon.PokemonMob;
import com.mygdx.pmd.model.Tile.*;
import com.mygdx.pmd.model.components.*;
import com.mygdx.pmd.model.instructions.*;

/**
 * Created by Cameron on 1/20/2017.
 */
public class MobLogic extends PokemonLogic {
    private PokemonMob mob;
    private boolean skipTurn;

    public MobLogic(PokemonMob mob) {
        super(mob);
        this.mob = mob;
    }

    @Override
    public void execute() {
        if (mob.cc.getHp() <= 0) {
            mob.shouldBeDestroyed = true;
        }

        if (mob.shouldBeDestroyed) {
            return;
        }

        if (mob.mc.isForcedMove()) {
            System.out.println("forced move");

            this.determineSpeed();
            tc.setTurnState(Turn.COMPLETE);
            skipTurn = true;
            return;
        }

        //ensure that when this runs the pokemon's turn is always waiting
        if (canAct()) {
            //variable to skip turn //TODO make it a function instead
            if(skipTurn){
                skipTurn = false;
                tc.setTurnState(Turn.COMPLETE);
                return;
            }
            //will turn to face the player if the mob is aggressive
            if (mob.cc.isAggressive()) {
                PositionComponent targetPC = (PositionComponent) mob.target.getComponent(PositionComponent.class);
                DirectionComponent targetDC = (DirectionComponent) mob.target.getComponent(DirectionComponent.class);

                dc.setDirection(targetPC.getCurrentTile());
                mc.setFacingTile(dc.getDirection());

                if (mob.target.shouldBeDestroyed) {
                    mob.target = mob.floor.getPlayer();
                    mob.cc.setAggressionState(Aggression.passive);
                    mob.pathFind = mob.wander;
                }
            }

            if (canAttack()) {
                mob.resetMove();

                if (isEnemyAdjacent()) {
                    attack();
                    //return is used to prevent the mob from moving
                    return;
                } else {
                    Tile enemyTile = findEnemyTile();
                    Move rangedMove = mob.getRandomRangedMove();
                    int dist = enemyTile.dist(mob.pc.getCurrentTile());

                    if (rangedMove != null && dist <= rangedMove.range) {
                        mob.setMove(mob.getRandomRangedMove());
                        attack();
                    }
                }
            }

            if (canMove()) {
                move();
                return;
            }
        }
    }

    private void attack(Move move) {
        tc.setTurnState(Turn.PENDING);
        mob.instructions.add(new AttackInstruction(mob, move));
    }

    @Override
    void attack() {
        if (mob.getMove() == null) {
            attack(mob.getRandomMove());
        } else {
            attack(mob.getMove());
        }
    }

    boolean canAttack() {
        return mob.canSeeEnemy() && mob.cc.isAggressive();
    }

    @Override
    void move() {
        // set the next tile based on if the mob has been forced to move or not

        if (mob.cc.isAggressive()) {
            mob.pathFind = mob.sPath;
        }
        //see if it can pathfind, meaning there was no error thrown
        if (pathFind()) {
            //this method depends on current tile not move component
            dc.setDirection(mc.possibleNextTile);

            if (this.mob.isLegalToMoveTo(mc.possibleNextTile)) {
                mc.setNextTile(mc.possibleNextTile);
                mc.possibleNextTile = null;
            }
        }

        if (mc.getNextTile() != null && mc.getNextTile() != pc.getCurrentTile()) {
            mob.instructions.add(new MoveInstruction(mob, mc.getNextTile()));

            //actually need this
            ac.setActionState(Action.MOVING);

            dc.setDirection(mc.getNextTile());
            this.determineSpeed();
        } else {
            ac.setActionState(Action.IDLE);
        }
        tc.setTurnState(Turn.COMPLETE);
        //tell the mob to go to to the next tile
    }

    @Override
    boolean canMove() {
        return tc.getTurnState() == Turn.WAITING;
    }

    private void determineSpeed() {
        if (mob.isWithinRange(mob.floor.getPlayer())) {
            mc.setSpeed(1);
            if (PMD.isKeyPressed(Key.s)) {
                mc.setSpeed(5);
            }
        } else {
            mc.setSpeed(25);
        }
    }

    private boolean pathFind() {
        try {
            // not one behind change back to movement component later
            MoveComponent targetMC = (MoveComponent) mob.target.getComponent(MoveComponent.class);
            mob.path = mob.pathFind.pathFind(targetMC.getNextTile());
        } catch (PathFindFailureException e) {
            System.out.println("Failed to pathfind");
        }

        if (mob.path.size <= 0) {
            return false;
        }

        mc.possibleNextTile = mob.path.first();
        mob.path.removeValue(mc.possibleNextTile, true);

        return true;
    }

    @Override
    boolean canAct() {
        return tc.getTurnState() == Turn.WAITING && ac.getActionState() == Action.IDLE && mob.instructions.isEmpty()
                && mob.currentInstruction == Entity.NO_INSTRUCTION && !mc.isForcedMove();
    }
}
