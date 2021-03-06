package com.mygdx.pmd.model.logic;

import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.Entity.Pokemon.*;
import com.mygdx.pmd.model.Entity.Projectile.Projectile;
import com.mygdx.pmd.model.components.*;
import com.mygdx.pmd.utils.*;

/**
 * Created by Cameron on 11/17/2016.
 */
public class ProjectileLogic implements Logic {
    private Projectile projectile;
    private Pokemon parent;
    private AnimationComponent anc;

    //TODO FIX PROJECTILE LOGIC WITH DIRECTION COMPONENTS

    private ActionComponent ac;
    private PositionComponent pc;
    private MoveComponent mc;

    /**
     * This class has one job, to find when a ranged projectile interacts with an entity or a unwalkable tile
     *
     * @param projectile the projectile to be acted upon
     */
    public ProjectileLogic(Projectile projectile) {
        this.projectile = projectile;
        this.parent = projectile.parent;
        this.anc = projectile.getComponent(AnimationComponent.class);
        this.ac = projectile.ac;
        this.pc = projectile.pc;
        this.mc = projectile.mc;
    }

    /**
     * Set of rules to check if projectile has collided yet
     */
    public void execute() {
        if (anc.isAnimationFinished() && ac.getActionState() == Action.COLLISION) {
            for(Entity entity: mc.getNextTile().getEntityList()){
                if(entity.hasComponent(CombatComponent.class)){
                    entity.getComponent(CombatComponent.class).takeDamage(parent, projectile.move.damage);
                }
            }

            if (projectile.move.equals(Move.INSTANT_KILLER)) {
                System.out.println("RKO OUT OF NOWHERE");
            }

            //let parent know that the attack has finished
            projectile.shouldBeDestroyed = true;
        }
    }
}
