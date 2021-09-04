package doggytalents.client.entity.render.world;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import doggytalents.DoggyTalents;
import doggytalents.common.entity.DogEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderWorldLastEvent;

import java.util.Optional;

public class BedFinderRenderer {

    public static void onWorldRenderLast(RenderWorldLastEvent event) {
        Player player = Minecraft.getInstance().player;
        for (Entity passenger : player.getPassengers()) {
            if (passenger instanceof DogEntity) {
                DogEntity dog = (DogEntity) passenger;
                Optional<BlockPos> bedPosOpt = dog.getBedPos();

                if (bedPosOpt.isPresent()) {
                    BlockPos bedPos = bedPosOpt.get();
                    int level = dog.getLevel(DoggyTalents.BED_FINDER);
                    double distance = (level * 200D) - Math.sqrt(bedPos.distSqr(dog.blockPosition()));
                    if (level == 5 || distance >= 0.0D) {
                        PoseStack stack = event.getMatrixStack();

                        AABB boundingBox = new AABB(bedPos).inflate(0.5D);
                        drawSelectionBox(stack, boundingBox);
                    }
                }
            }
        }
    }

    public static void drawSelectionBox(PoseStack stack, AABB boundingBox) {
        // TODO RenderSystem.disableAlphaTest();
        // TODO RenderSystem.disableLighting(); //Make the line see thought blocks
        RenderSystem.depthMask(false);
        RenderSystem.disableDepthTest(); //Make the line see thought blocks
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        //TODO Used when drawing outline of bounding box
        RenderSystem.lineWidth(2.0F);

        RenderSystem.disableTexture();
        Vec3 vec3d = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
        double d0 = vec3d.x();
        double d1 = vec3d.y();
        double d2 = vec3d.z();

        BufferBuilder buf = Tesselator.getInstance().getBuilder();
        buf.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        LevelRenderer.renderLineBox(stack, buf, boundingBox.move(-d0, -d1, -d2), 1F, 1F, 0, 1F);
        Tesselator.getInstance().end();
        RenderSystem.setShaderColor(0.0F, 0.0F, 0.0F, 0.3F);
        RenderSystem.enableDepthTest(); //Make the line see thought blocks
        RenderSystem.depthMask(true);
        RenderSystem.enableTexture();
        // TODO RenderSystem.enableLighting(); //Make the line see thought blocks
        RenderSystem.disableBlend();
        // TODO RenderSystem.enableAlphaTest();
    }
}
