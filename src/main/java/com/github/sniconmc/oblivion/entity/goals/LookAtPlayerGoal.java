package com.github.sniconmc.oblivion.entity.goals;

import com.github.sniconmc.oblivion.OblivionMain;
import com.github.sniconmc.oblivion.entity.OblivionBody;
import net.minestom.server.entity.Entity;
import net.minestom.server.entity.EntityCreature;
import net.minestom.server.entity.Player;
import net.minestom.server.entity.ai.GoalSelector;

public final class LookAtPlayerGoal extends GoalSelector {
    private Entity target;
    private final double range;  // Range within which the NPC should look at players
    private final float originalYaw;
    private final float originalPitch;

    public LookAtPlayerGoal(EntityCreature entityCreature, double range) {
        super(entityCreature);
        this.range = range;
        this.originalYaw = entityCreature.getPosition().yaw(); // Capture the original yaw
        this.originalPitch = entityCreature.getPosition().pitch();
    }

    @Override
    public boolean shouldStart() {
        if (!((OblivionBody) entityCreature).shouldLookAtPlayers()) {
            return false;
        }
        target = findTarget();
        return target != null;
    }

    @Override
    public void start() {
        // No action needed on start
    }

    @Override
    public void tick(long time) {
        if (!((OblivionBody) entityCreature).shouldLookAtPlayers()) {
            target = null;
            return;
        }

        // Update the target to the closest player within range
        target = findTarget();

        if (target == null || entityCreature.getDistanceSquared(target) > range * range ||
                entityCreature.getInstance() != target.getInstance()) {
            target = null;
            // Reset the entity's yaw to the original yaw and pitch without teleporting
            resetHeadRotation();
            return;
        }

        entityCreature.lookAt(target);
    }

    @Override
    public boolean shouldEnd() {
        return target == null || !((OblivionBody) entityCreature).shouldLookAtPlayers();
    }

    @Override
    public void end() {
        // Reset to the original yaw and pitch without teleporting
        resetHeadRotation();
    }
    private void resetHeadRotation() {
        // Ensure the head rotation is set correctly without teleporting the entity
        entityCreature.refreshPosition(entityCreature.getPosition().withView(originalYaw, originalPitch));
        OblivionMain.logger.info("Resetting Pos to {}", entityCreature.getPosition().withView(originalYaw, originalPitch));
    }


    public Entity findTarget() {
        // Always find and return the closest player within the specified range
        return entityCreature.getInstance().getEntities()
                .stream()
                .filter(entity -> entity instanceof Player && entityCreature.getDistanceSquared(entity) <= range * range)
                .min((e1, e2) -> Double.compare(entityCreature.getDistanceSquared(e1), entityCreature.getDistanceSquared(e2)))
                .orElse(null);
    }
}
