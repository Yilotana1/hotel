package com.example.hotel.controller.factory.action;

import com.example.hotel.controller.action.Action;
import com.example.hotel.controller.factory.action.impl.ActionFactoryImpl;

public abstract class ActionFactory {
    private static ActionFactory actionFactory;

    public abstract Action getAction(final String uri);

    public static ActionFactory getInstance() {
        if (actionFactory == null) {
            synchronized (ActionFactory.class) {
                if (actionFactory == null) {
                    var temp = new ActionFactoryImpl();
                    actionFactory = temp;
                }
            }
        }
        return actionFactory;
    }
}
