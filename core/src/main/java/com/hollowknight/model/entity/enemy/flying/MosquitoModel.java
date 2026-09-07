package com.hollowknight.model.entity.enemy.flying;

import com.hollowknight.model.entity.enemy.EnemyModel;


public class MosquitoModel extends EnemyModel {

    public enum MState { PATROL, PREPARE_DASH, DASHING }

    private MState  mState        = MState.PATROL;
    private float   targetX, targetY;
    private float   prepareDuration = 0.4f;

    public MosquitoModel(float x, float y) {
        super(x, y, 22f, 22f, 8, 60f, EnemyType.MOSQUITO);
    }

    public MState getMState()             { return mState; }
    public void   setMState(MState s)     { mState = s; }
    public float  getTargetX()            { return targetX; }
    public float  getTargetY()            { return targetY; }
    public void   setTarget(float x, float y) { targetX = x; targetY = y; }
    public float  getPrepareDuration()    { return prepareDuration; }
}
