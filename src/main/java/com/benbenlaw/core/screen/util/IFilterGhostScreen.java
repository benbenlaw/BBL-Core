package com.benbenlaw.core.screen.util;

import net.minecraft.world.inventory.AbstractContainerMenu;

public interface IFilterGhostScreen {

    //Implementing this class allows the screen to use the ghost item and fluid functions to allow JEI to be able to drag item and fluids into filter slots

    AbstractContainerMenu getMenu();
    int getGuiLeft();
    int getGuiTop();
}