package me.infinity.minigame.meetup.Utils.Kit;

import org.bukkit.inventory.ItemStack;

import java.util.List;

public class Kit{
        private ItemStack helmet;
        private ItemStack chestplate;
        private ItemStack leggings;
        private ItemStack boots;
        private ItemStack sword;
        private ItemStack bow;
        private Integer goldenApples;
        private Integer goldenHeads;
        private Integer cobweb;
        private Boolean speed;
        private Boolean fire;
        private List<ItemStack> miscellanous;

        public Kit(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots, ItemStack sword, ItemStack bow, Integer goldenApples, Integer goldenHeads, Integer cobweb, Boolean speed, Boolean fire){
            this.helmet = helmet;
            this.chestplate = chestplate;
            this.leggings = leggings;
            this.boots = boots;
            this.sword = sword;
            this.goldenHeads = goldenHeads;
            this.goldenApples = goldenApples;
            this.cobweb = cobweb;
            this.speed = speed;
            this.fire = fire;
            this.bow = bow;
        }


        public ItemStack getHelmet() {
            return helmet;
        }

        public void setHelmet(ItemStack helmet) {
            this.helmet = helmet;
        }

        public ItemStack getChestplate() {
            return chestplate;
        }

        public void setChestplate(ItemStack chestplate) {
            this.chestplate = chestplate;
        }

        public ItemStack getLeggings() {
            return leggings;
        }

        public void setLeggings(ItemStack leggings) {
            this.leggings = leggings;
        }

        public ItemStack getBoots() {
            return boots;
        }

        public void setBoots(ItemStack boots) {
            this.boots = boots;
        }


        public ItemStack getSword() {
            return sword;
        }

        public void setSword(ItemStack sword) {
            this.sword = sword;
        }

        public Integer getGoldenApples() {
            return goldenApples;
        }

        public void setGoldenApples(Integer goldenApples) {
            this.goldenApples = goldenApples;
        }

        public Integer getGoldenHeads() {
            return goldenHeads;
        }

        public void setGoldenHeads(Integer goldenHeads) {
            this.goldenHeads = goldenHeads;
        }

        public ItemStack getBow() {
            return bow;
        }

        public void setBow(ItemStack bow) {
            this.bow = bow;
        }

        public Integer getCobweb() {
            return cobweb;
        }

        public void setCobweb(Integer cobweb) {
            this.cobweb = cobweb;
        }
        public Boolean getSpeed() {
            return speed;
        }

        public void setSpeed(Boolean speed) {
            this.speed = speed;
        }

        public Boolean getFire() {
            return fire;
        }

        public void setFire(Boolean fire) {
            this.fire = fire;
        }

        public List<ItemStack> getMiscellanous() {
            return miscellanous;
        }

        public void setMiscellanous(List<ItemStack> miscellanous) {
            this.miscellanous = miscellanous;
        }


}
