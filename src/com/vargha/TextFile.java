package com.vargha;

import java.nio.file.Path;
import java.util.List;

class TextFile {
    private final Path path;
    private final List<String> content;

    TextFile(Path path, List<String> content) {
        this.path = path;
        this.content = content;
    }

    Path getPath() {
        return path;
    }

    List<String> getContent() {
        return content;
    }
}
