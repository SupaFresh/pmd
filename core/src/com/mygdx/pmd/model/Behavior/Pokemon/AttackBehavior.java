package com.mygdx.pmd.model.Behavior.Pokemon;

import com.mygdx.pmd.enumerations.Action;
import com.mygdx.pmd.enumerations.Aggression;
import com.mygdx.pmd.enumerations.Move;
import com.mygdx.pmd.enumerations.Turn;
import com.mygdx.pmd.model.Behavior.Pokemon.PokemonBehavior;
import com.mygdx.pmd.model.Entity.Pokemon.Pokemon;

/**
 * Created by Cameron on 11/14/2016.
 */
public class AttackBehavior extends PokemonBehavior {

    public AttackBehavior(Pokemon pokemon) {
        super(pokemon);
    }

    @Override
    public void execute() {
        if (pMob.children.size == 0 && pMob.currentAnimation.isFinished()) {
            pMob.setTurnState(Turn.COMPLETE);
            pMob.setActionState(Action.IDLE);
            pMob.behaviors[2] = pMob.noBehavior;
            pMob.currentAnimation.clear();
        }
    }
}
