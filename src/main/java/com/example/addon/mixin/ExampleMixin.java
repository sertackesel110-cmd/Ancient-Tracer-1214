package com.example.addon.mixin;

import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(TitleScreen.class)
public class ExampleMixin {
    // Empty class to prevent startup crashes and black screen
}
