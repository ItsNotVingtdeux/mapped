package net.itsvingtdeux.mapped;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class TestHud {
    private static final ResourceLocation RESOURCE_WHITE = new ResourceLocation(mapped.MOD_ID,
            "textures/gui/minimap/white.png");

    public static final IGuiOverlay HUD_TEST = ((gui, poseStack, partialTick, width, height) -> {
        int pixelSize = 5;
        int pixelsInMap = 11;
        for(int x = 0; x < pixelsInMap; x++) {
            for(int y = 0; y < pixelsInMap; y++) {
                double flX = Minecraft.getInstance().player.getX() + x - pixelsInMap / 2;
                double flY = Minecraft.getInstance().player.getY() - 0.5;
                double flZ = Minecraft.getInstance().player.getZ() + y - pixelsInMap / 2;
                BlockPos myPosition = new BlockPos(flX, flY, flZ);
                BlockState myState = Minecraft.getInstance().level.getBlockState(myPosition);
                int myColor = myState.getMaterial().getColor().calculateRGBColor(MaterialColor.Brightness.NORMAL);
                float myR = (myColor & 0xFF) / 255.0F;
                float myG = ((myColor & 0xFF00) >>> 8) / 255.0F;
                float myB = ((myColor & 0xFF0000) >>> 16) / 255.0F;
                mapped.LOGGER.info(String.format("block color %d", myColor));
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(myR, myG, myB, 1.0F);
                RenderSystem.setShaderTexture(0, RESOURCE_WHITE);
                GuiComponent.blit(poseStack, x * pixelSize, y * pixelSize, 0, 0, pixelSize, pixelSize, 1, 1);
            }
        }
    });
}