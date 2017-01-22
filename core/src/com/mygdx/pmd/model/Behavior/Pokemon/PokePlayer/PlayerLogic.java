package com.mygdx.pmd.model.Behavior.Pokemon.PokePlayer;

import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.model.Behavior.Entity.MoveFastBehavior;
import com.mygdx.pmd.model.Behavior.Pokemon.PokemonBehavior;
import com.mygdx.pmd.model.Entity.Pokemon.PokemonPlayer;

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
        if (player.turnState != Turn.WAITING || !player.equals(player.currentTile)) return;

        player.handleExtraInput();

        if (player.canAttack()) {
            player.attack(Move.INSTANT_KILLER);
            player.setActionState(Action.ATTACKING);
            player.turnState = Turn.PENDING;

            player.behaviors[2] = player.attackBehavior;
        } else
        if (player.canMove()) {
            player.turnState = Turn.COMPLETE;
            player.setActionState(Action.MOVING);
            if (controller.isKeyPressed(Key.s)) {
                player.behaviors[2] = new MoveFastBehavior(player);
            } else player.behaviors[2] = player.moveBehavior;
        }
    }

    @Override
    public boolean canExecute() {
        return false;
    }
}