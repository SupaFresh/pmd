package com.mygdx.pmd.model.instructions;

/**
 * Created by Cameron on 2/21/2017.
 */
public interface Instruction {
    void onInit();
    void onFinish();

    void execute();
    boolean isFinished();
}
