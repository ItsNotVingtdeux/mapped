package net.itsvingtdeux.mapped;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MaterialColor;

public class TestHud {
    private static final ResourceLocation RESOURCE_WHITE = new ResourceLocation(mapped.MOD_ID,
            "textures/gui/minimap/white.png");
    private static final ResourceLocation RESOURCE_WIDGETS = new ResourceLocation(mapped.MOD_ID,
            "textures/gui/minimap/widgets.png");

    public static final IGuiOverlay HUD_TEST = ((gui, poseStack, partialTick, width, height) -> {
        int pixelSize = 6;
        int pixelsInMap = 11;
        LocalPlayer myPlayer = Minecraft.getInstance().player;
        float myAngle = (myPlayer.getYRot() - 180) % 360;
        if(myAngle < 0){
            myAngle += 360;
        }
        float myRad = myAngle * (float)Math.PI / 180.0F;
        mapped.LOGGER.info(String.format("player rot: %f", myAngle));
        for(int x = 0; x < pixelsInMap; x++) {
            for(int y = 0; y < pixelsInMap; y++) {
                float searchX = x - pixelsInMap / 2;
                float searchY = y - pixelsInMap / 2;
                double searchXR = searchX * Math.cos(myRad) - searchY * Math.sin(myRad);
                double searchYR = searchY * Math.cos(myRad) + searchX * Math.sin(myRad);
                double flX = myPlayer.getX() + searchXR;
                double flY = myPlayer.getY();
                double flZ = myPlayer.getZ() + searchYR;
                BlockPos myPosition = new BlockPos(flX, flY, flZ);
                BlockState myState = Minecraft.getInstance().level.getBlockState(myPosition);
                while(myState.getBlock() == Blocks.AIR) {
                    flY--;
                    myPosition = new BlockPos(flX, flY, flZ);
                    myState = Minecraft.getInstance().level.getBlockState(myPosition);
                    if(flY < -64){
                        break;
                    }
                }
                int myColor = myState.getMaterial().getColor().calculateRGBColor(MaterialColor.Brightness.NORMAL);
                float myR = (myColor & 0xFF) / 255.0F;
                float myG = ((myColor & 0xFF00) >>> 8) / 255.0F;
                float myB = ((myColor & 0xFF0000) >>> 16) / 255.0F;
                RenderSystem.setShader(GameRenderer::getPositionTexShader);
                RenderSystem.setShaderColor(myR, myG, myB, 1.0F);
                RenderSystem.setShaderTexture(0, RESOURCE_WHITE);
                GuiComponent.blit(poseStack, x * pixelSize + 3, y * pixelSize + 3, 0, 0, pixelSize, pixelSize, 1, 1);
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, RESOURCE_WIDGETS);
        GuiComponent.blit(poseStack, 0, 0, 0, 0, 72, 72, 256, 256);
    });
}