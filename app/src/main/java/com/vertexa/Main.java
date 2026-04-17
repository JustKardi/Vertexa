package com.vertexa;

import com.vertexa.vertexa.Core.Engine;

public class Main {
    public static void main(String[] args) {
        Engine engine = Engine.get();
        engine.start();
    }
}
