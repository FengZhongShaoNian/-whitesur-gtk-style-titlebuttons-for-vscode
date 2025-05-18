package org.example;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Base64;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        List<String> buttons = List.of("close", "maximize", "minimize", "restore");
        List<String> states = List.of("", "Hover", "Pressed", "Inactive");
        List<String> colors = List.of("", "_dark");

        for (String button : buttons) {
            for (String state : states) {
                for (String color : colors) {
                    String iconFileName = button + state + color + ".svg";

                    String cssButton = button;

                    if(cssButton.equals("maximize") || cssButton.equals("restore")) {
                        cssButton = "max-restore";
                    }

                    String cssState = state.toLowerCase();
                    if(cssState.equals("hover")) {
                        cssState = ":hover";
                    }
                    else if(cssState.equals("pressed")) {
                        cssState = ":active";
                    }

                    StringBuilder cssBuilder = new StringBuilder();

                    if(cssState.equals("inactive")) {
                        cssBuilder.append(".monaco-workbench .part.titlebar.inactive .window-controls-container > .window-icon.");
                        cssBuilder.append("window-").append(cssButton);
                        if(button.equals("restore")) {
                            cssBuilder.append(".codicon.codicon-chrome-restore");
                        } else if (button.equals("maximize")) {
                            cssBuilder.append(".codicon.codicon-chrome-maximize");
                        }
                        cssBuilder.append("{\n");
                        setCssContent(cssBuilder, iconFileName);
                        cssBuilder.append("}\n");
                    }else {
                        cssBuilder.append(".monaco-workbench .part.titlebar .window-controls-container > .window-icon.");
                        cssBuilder.append("window-").append(cssButton);
                        if(button.equals("restore")) {
                            cssBuilder.append(".codicon.codicon-chrome-restore");
                        } else if (button.equals("maximize")) {
                            cssBuilder.append(".codicon.codicon-chrome-maximize");
                        }
                        cssBuilder.append(cssState);
                        cssBuilder.append("{\n");
                        setCssContent(cssBuilder, iconFileName);
                        cssBuilder.append("}\n");
                    }
                    System.out.println(cssBuilder);
                    writeCss(cssBuilder);
                }
            }
        }
    }

    private static void setCssContent(StringBuilder cssBuilder, String svgFilename) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("whitesur-gtk-icons");
        String folder = resource.getPath();
        String svgContent = Files.readString(Path.of(folder, svgFilename));
        String svgBase64 = Base64.getEncoder().encodeToString(svgContent.getBytes("UTF-8"));
        cssBuilder.append("    background-color: rgba(255, 255, 255, 0) !important;").append("\n");
        cssBuilder.append("    background-image: url(\"");
        cssBuilder.append("data:image/svg+xml;base64,");
        cssBuilder.append(svgBase64);
        cssBuilder.append("\") !important; \n");
        cssBuilder.append("    background-repeat: no-repeat;\n");
        cssBuilder.append("    background-position: center;\n");
    }

    private static void writeCss(StringBuilder cssBuilder) throws IOException {
        ClassLoader classLoader = Main.class.getClassLoader();
        URL resource = classLoader.getResource("whitesur-gtk-vscode.css");
        String filePath = resource.getPath();
        Files.write(Path.of(filePath), cssBuilder.toString().getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
    }
}