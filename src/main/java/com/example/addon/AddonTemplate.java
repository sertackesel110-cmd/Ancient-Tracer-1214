package com.example.addon;

import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.events.render.Render3DEvent;
import meteordevelopment.meteorclient.utils.render.color.SettingColor;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.List;
import java.util.ArrayList;

public class AddonTemplate extends MeteorAddon {
    // 1.21.4'te hatasız başlatma için zorunlu Logger
    public static final Logger LOG = LoggerFactory.getLogger("UniversalTracer");
    public static final Category CATEGORY = new Category("Custom");

    @Override
    public void onInitialize() {
        LOG.info("Universal Tracer Eklentisi Başlatıldı!");
        Modules.get().add(new UniversalTracer());
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.example.addon";
    }

    public static class UniversalTracer extends Module {
        private final SettingGroup sgGeneral = settings.getDefaultGroup();

        // --- 1. ÖZELLİK: Eşya Seçme Listesi ---
        // Menüde "items" kısmına tıklayıp istediğin blokları ekleyebilirsin.
        private final Setting<List<Item>> items = sgGeneral.add(new ItemListSetting.Builder()
            .name("items")
            .description("Hangi eşyaların takip edileceğini seç.")
            .defaultValue(new ArrayList<>())
            .build()
        );

        // --- 2. ÖZELLİK: Renk Ayarı ---
        private final Setting<SettingColor> color = sgGeneral.add(new ColorSetting.Builder()
            .name("color")
            .description("Çizgi rengi.")
            .defaultValue(new SettingColor(0, 255, 255, 255)) // Varsayılan: Turkuaz
            .build()
        );

        public UniversalTracer() {
            super(CATEGORY, "universal-tracer", "Yerdeki seçili eşyaları çizgiyle gösterir.");
        }

        @EventHandler
        private void onRender(Render3DEvent event) {
            // Dünya veya oyuncu yüklenmediyse işlemi durdur (Çökme önleyici)
            if (mc.world == null || mc.player == null) return;

            // Yerdeki tüm varlıkları tara
            for (Entity entity : mc.world.getEntities()) {
                // Eğer varlık bir "Yerdeki Eşya" (ItemEntity) ise
                if (entity instanceof ItemEntity item) {
                    // Ve bu eşya senin menüden seçtiğin listede varsa
                    if (items.get().contains(item.getStack().getItem())) {
                        // Çizgiyi çek
                        event.renderer.line(
                            mc.player.getX(), mc.player.getEyeY(), mc.player.getZ(),
                            item.getX(), item.getY(), item.getZ(),
                            color.get()
                        );
                    }
                }
            }
        }
    }
}
