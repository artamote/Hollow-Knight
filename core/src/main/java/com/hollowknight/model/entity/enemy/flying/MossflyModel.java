package com.hollowknight.model.entity.enemy.flying;

import com.hollowknight.model.entity.enemy.EnemyModel;


public class MossflyModel extends EnemyModel {

    public enum MossflyState { HIDDEN, REVEALING, FLYING }

    private MossflyState mossflyState = MossflyState.HIDDEN;
    private float revealDuration = 0.6f;

    public MossflyModel(float x, float y) {
        super(x, y, 28f, 26f, 10, 90f, EnemyType.MOSSFLY);
        this.detectionRange = 160f;
    }

    public MossflyState getMossflyState()        { return mossflyState; }
    public void setMossflyState(MossflyState s)   { mossflyState = s; }
    public float getRevealDuration()              { return revealDuration; }
}