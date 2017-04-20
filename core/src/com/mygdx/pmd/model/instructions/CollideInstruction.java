package com.mygdx.pmd.model.instructions;

import com.badlogic.gdx.audio.Sound;
import com.mygdx.pmd.PMD;
import com.mygdx.pmd.enumerations.*;
import com.mygdx.pmd.model.Entity.*;
import com.mygdx.pmd.model.components.*;

/**
 * Created by Cameron on 4/1/2017.
 */
public class CollideInstruction implements Instruction{
    private Entity entity;
    private boolean isFinished;
    private ActionComponent ac;
    private PositionComponent pc;

    public CollideInstruction(Entity entity){
        this.entity = entity;
        this.ac = (ActionComponent) this.entity.getComponent(ActionComponent.class);
        this.pc = (PositionComponent) this.entity.getComponent(PositionComponent.class);
    }

    @Override
    public void onInit() {
        PMD.manager.get("sfx/wallhit.wav", Sound.class).play();
        ac.setActionState(Action.COLLISION);
        pc.removeFromCurrentTile();
    }

    @Override
    public void onFinish() {

    }

    @Override
    public void execute() {
        if(entity.currentAnimation.isFinished()){
            isFinished = true;
        }
    }

    @Override
    public boolean isFinished() {
        return isFinished;
    }
}
